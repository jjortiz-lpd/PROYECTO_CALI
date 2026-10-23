package torneojav.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.excepcion.PersistenciaException;
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadoInscripcion;
import torneojav.persistencia.GestorConexionJDBC;

public class EquipoRepositorioPostgres implements EquipoRepositorio {

    private static final String SELECT = "SELECT id, nombre, capitan_id, torneo_id, estado FROM equipo ";

    @Override
    public Equipo guardar(Equipo equipo) {
        if (equipo.getId() == 0) {
            insertar(equipo);
        } else {
            actualizar(equipo);
        }
        return equipo;
    }

    private void insertar(Equipo e) {
        String sql = "INSERT INTO equipo (nombre, capitan_id, torneo_id, estado) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setInt(2, e.getCapitanId());
            ps.setInt(3, e.getTorneoId());
            ps.setString(4, e.getEstado().name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                e.setId(rs.getInt(1));
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Error guardando el equipo " + e.getNombre(), ex);
        }
    }

    private void actualizar(Equipo e) {
        String sql = "UPDATE equipo SET nombre = ?, estado = ? WHERE id = ?";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getEstado().name());
            ps.setInt(3, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new PersistenciaException("Error actualizando el equipo " + e.getId(), ex);
        }
    }

    @Override
    public Optional<Equipo> buscarPorId(int id) {
        List<Equipo> lista = consultar(SELECT + "WHERE id = ?", id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Equipo> buscarPorTorneo(int torneoId) {
        return consultar(SELECT + "WHERE torneo_id = ? ORDER BY id", torneoId);
    }

    @Override
    public List<Equipo> buscarPorCapitan(int capitanId) {
        return consultar(SELECT + "WHERE capitan_id = ? ORDER BY id", capitanId);
    }

    /** Ejecuta un SELECT que recibe un único parámetro entero. */
    private List<Equipo> consultar(String sql, int parametro) {
        List<Equipo> resultado = new ArrayList<>();
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error consultando equipos", e);
        }
        return resultado;
    }

    static Equipo mapear(ResultSet rs) throws SQLException {
        Equipo e = new Equipo(rs.getInt("id"), rs.getString("nombre"),
                rs.getInt("capitan_id"), rs.getInt("torneo_id"));
        e.setEstado(EstadoInscripcion.valueOf(rs.getString("estado")));
        return e;
    }
}
