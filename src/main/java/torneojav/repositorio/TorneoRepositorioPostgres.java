package torneojav.repositorio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.excepcion.PersistenciaException;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Torneo;
import torneojav.persistencia.GestorConexionJDBC;

public class TorneoRepositorioPostgres implements TorneoRepositorio {

    private static final String COLUMNAS = "id, nombre, fecha_inicio, formato";

    @Override
    public Torneo guardar(Torneo torneo) {
        if (torneo.getId() == 0) {
            insertar(torneo);
        } else {
            actualizar(torneo);
        }
        return torneo;
    }

    private void insertar(Torneo t) {
        String sql = "INSERT INTO torneo (nombre, fecha_inicio, formato) VALUES (?, ?, ?) RETURNING id";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            llenarParametros(ps, t);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                t.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error guardando el torneo", e);
        }
    }

    private void actualizar(Torneo t) {
        String sql = "UPDATE torneo SET nombre = ?, fecha_inicio = ?, formato = ? WHERE id = ?";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            llenarParametros(ps, t);
            ps.setInt(4, t.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error actualizando el torneo " + t.getId(), e);
        }
    }

    private void llenarParametros(PreparedStatement ps, Torneo t) throws SQLException {
        ps.setString(1, t.getNombre());
        ps.setDate(2, Date.valueOf(t.getFechaInicio()));
        if (t.getFormato() == null) {
            ps.setNull(3, Types.VARCHAR);
        } else {
            ps.setString(3, t.getFormato().name());
        }
    }

    @Override
    public List<Torneo> listarTodos() {
        String sql = "SELECT " + COLUMNAS + " FROM torneo ORDER BY id";
        List<Torneo> resultado = new ArrayList<>();
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error listando torneos", e);
        }
        return resultado;
    }

    @Override
    public Optional<Torneo> buscarPorId(int id) {
        String sql = "SELECT " + COLUMNAS + " FROM torneo WHERE id = ?";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error buscando el torneo " + id, e);
        }
    }

    @Override
    public void eliminar(int id) {
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement("DELETE FROM torneo WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error eliminando el torneo " + id
                    + " (puede tener equipos o partidos asociados)", e);
        }
    }

    private Torneo mapear(ResultSet rs) throws SQLException {
        String formato = rs.getString("formato");
        return new Torneo(rs.getInt("id"), rs.getString("nombre"),
                rs.getDate("fecha_inicio").toLocalDate(),
                formato == null ? null : FormatoTorneo.valueOf(formato));
    }
}
