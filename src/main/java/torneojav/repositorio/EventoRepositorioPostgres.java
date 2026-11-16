package torneojav.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import torneojav.excepcion.PersistenciaException;
import torneojav.modelo.EventoPartido;
import torneojav.modelo.TipoEvento;
import torneojav.persistencia.GestorConexionJDBC;

public class EventoRepositorioPostgres implements EventoRepositorio {

    @Override
    public EventoPartido guardar(EventoPartido evento) {
        String sql = "INSERT INTO evento_partido (partido_id, tipo, minuto, jugador_id, equipo_id) "
                + "VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, evento.getPartidoId());
            ps.setString(2, evento.getTipo().name());
            ps.setInt(3, evento.getMinuto());
            ps.setInt(4, evento.getJugadorId());
            ps.setInt(5, evento.getEquipoId());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                evento.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error guardando el evento del partido", e);
        }
        return evento;
    }

    @Override
    public List<EventoPartido> buscarPorPartido(int partidoId) {
        String sql = "SELECT id, partido_id, tipo, minuto, jugador_id, equipo_id FROM evento_partido "
                + "WHERE partido_id = ? ORDER BY minuto, id";
        List<EventoPartido> resultado = new ArrayList<>();
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, partidoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new EventoPartido(rs.getInt("id"), rs.getInt("partido_id"),
                            TipoEvento.valueOf(rs.getString("tipo")), rs.getInt("minuto"),
                            rs.getInt("jugador_id"), rs.getInt("equipo_id")));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error consultando eventos del partido " + partidoId, e);
        }
        return resultado;
    }
}
