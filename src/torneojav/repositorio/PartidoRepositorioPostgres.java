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
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadoInscripcion;
import torneojav.modelo.EstadoPartido;
import torneojav.modelo.Partido;
import torneojav.persistencia.GestorConexionJDBC;

public class PartidoRepositorioPostgres implements PartidoRepositorio {

    /*
     * Un Partido guarda objetos Equipo (local y visitante), no solo sus ids.
     * Por eso el SELECT hace JOIN dos veces con la tabla equipo:
     * "l" es el equipo local y "v" el visitante.
     */
    private static final String SELECT = "SELECT p.id, p.torneo_id, p.fecha, p.ronda, p.estado, "
            + "p.goles_local, p.goles_visitante, p.acta_cerrada, "
            + "l.id AS l_id, l.nombre AS l_nombre, l.capitan_id AS l_capitan, l.torneo_id AS l_torneo, l.estado AS l_estado, "
            + "v.id AS v_id, v.nombre AS v_nombre, v.capitan_id AS v_capitan, v.torneo_id AS v_torneo, v.estado AS v_estado "
            + "FROM partido p "
            + "JOIN equipo l ON l.id = p.local_id "
            + "JOIN equipo v ON v.id = p.visitante_id ";

    @Override
    public Partido guardar(Partido partido) {
        if (partido.getId() == 0) {
            insertar(partido);
        } else {
            actualizar(partido);
        }
        return partido;
    }

    private void insertar(Partido p) {
        String sql = "INSERT INTO partido (torneo_id, local_id, visitante_id, fecha, ronda, estado, "
                + "goles_local, goles_visitante, acta_cerrada) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, p.getTorneoId());
            ps.setInt(2, p.getLocal().getId());
            ps.setInt(3, p.getVisitante().getId());
            ps.setDate(4, Date.valueOf(p.getFecha()));
            ps.setInt(5, p.getRonda());
            ps.setString(6, p.getEstado().name());
            ps.setInt(7, p.getGolesLocal());
            ps.setInt(8, p.getGolesVisitante());
            ps.setBoolean(9, p.isActaCerrada());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                p.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error guardando el partido", e);
        }
    }

    private void actualizar(Partido p) {
        String sql = "UPDATE partido SET estado = ?, goles_local = ?, goles_visitante = ?, acta_cerrada = ? "
                + "WHERE id = ?";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, p.getEstado().name());
            ps.setInt(2, p.getGolesLocal());
            ps.setInt(3, p.getGolesVisitante());
            ps.setBoolean(4, p.isActaCerrada());
            ps.setInt(5, p.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error actualizando el partido " + p.getId(), e);
        }
    }

    @Override
    public Optional<Partido> buscarPorId(int id) {
        List<Partido> lista = consultar(SELECT + "WHERE p.id = ?", id);
        return lista.isEmpty() ? Optional.empty() : Optional.of(lista.get(0));
    }

    @Override
    public List<Partido> buscarPorTorneo(int torneoId) {
        return consultar(SELECT + "WHERE p.torneo_id = ? ORDER BY p.ronda, p.fecha, p.id", torneoId);
    }

    private List<Partido> consultar(String sql, int parametro) {
        List<Partido> resultado = new ArrayList<>();
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, parametro);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error consultando partidos", e);
        }
        return resultado;
    }

    private Partido mapear(ResultSet rs) throws SQLException {
        Equipo local = mapearEquipo(rs, "l_");
        Equipo visitante = mapearEquipo(rs, "v_");
        Partido p = new Partido(rs.getInt("id"), rs.getInt("torneo_id"), local, visitante,
                rs.getDate("fecha").toLocalDate(), rs.getInt("ronda"));
        p.setEstado(EstadoPartido.valueOf(rs.getString("estado")));
        p.setGolesLocal(rs.getInt("goles_local"));
        p.setGolesVisitante(rs.getInt("goles_visitante"));
        p.setActaCerrada(rs.getBoolean("acta_cerrada"));
        return p;
    }

    private Equipo mapearEquipo(ResultSet rs, String prefijo) throws SQLException {
        Equipo e = new Equipo(rs.getInt(prefijo + "id"), rs.getString(prefijo + "nombre"),
                rs.getInt(prefijo + "capitan"), rs.getInt(prefijo + "torneo"));
        e.setEstado(EstadoInscripcion.valueOf(rs.getString(prefijo + "estado")));
        return e;
    }
}
