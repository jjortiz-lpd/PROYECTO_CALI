package torneojav.repositorio;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.excepcion.PersistenciaException;
import torneojav.modelo.Jugador;
import torneojav.persistencia.GestorConexionJDBC;

public class JugadorRepositorioPostgres implements JugadorRepositorio {

    private static final String SELECT = "SELECT id, nombre_completo, documento, fecha_nacimiento, "
            + "numero_camiseta, equipo_id FROM jugador ";

    @Override
    public Jugador guardar(Jugador jugador) {
        if (jugador.getId() == 0) {
            insertar(jugador);
        } else {
            actualizar(jugador);
        }
        return jugador;
    }

    private void insertar(Jugador j) {
        String sql = "INSERT INTO jugador (nombre_completo, documento, fecha_nacimiento, numero_camiseta, equipo_id) "
                + "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, j.getNombreCompleto());
            ps.setString(2, j.getDocumento());
            ps.setDate(3, Date.valueOf(j.getFechaNacimiento()));
            ps.setInt(4, j.getNumeroCamiseta());
            ps.setInt(5, j.getEquipoId());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                j.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error guardando el jugador " + j.getNombreCompleto(), e);
        }
    }

    private void actualizar(Jugador j) {
        String sql = "UPDATE jugador SET nombre_completo = ?, documento = ?, fecha_nacimiento = ?, "
                + "numero_camiseta = ? WHERE id = ?";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, j.getNombreCompleto());
            ps.setString(2, j.getDocumento());
            ps.setDate(3, Date.valueOf(j.getFechaNacimiento()));
            ps.setInt(4, j.getNumeroCamiseta());
            ps.setInt(5, j.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error actualizando el jugador " + j.getId(), e);
        }
    }

    @Override
    public Optional<Jugador> buscarPorId(int id) {
        List<Jugador> lista = consultar(SELECT + "WHERE id = ?", id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Jugador> buscarPorEquipo(int equipoId) {
        return consultar(SELECT + "WHERE equipo_id = ? ORDER BY numero_camiseta", equipoId);
    }

    @Override
    public void eliminar(int id) {
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement("DELETE FROM jugador WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error eliminando el jugador " + id
                    + " (puede tener eventos registrados en partidos)", e);
        }
    }

    private List<Jugador> consultar(String sql, int parametro) {
        List<Jugador> resultado = new ArrayList<>();
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new Jugador(rs.getInt("id"), rs.getString("nombre_completo"),
                            rs.getString("documento"), rs.getDate("fecha_nacimiento").toLocalDate(),
                            rs.getInt("numero_camiseta"), rs.getInt("equipo_id")));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error consultando jugadores", e);
        }
        return resultado;
    }
}
