package torneojav.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import torneojav.excepcion.PersistenciaException;
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadoInscripcion;
import torneojav.modelo.PosicionTabla;
import torneojav.persistencia.GestorConexionJDBC;

public class PosicionRepositorioPostgres implements PosicionRepositorio {

    /**
     * Borra la tabla vieja del torneo y escribe la nueva dentro de UNA transacción.
     *
     * Sin transacción, si el programa falla después del DELETE y antes de terminar
     * los INSERT, la tabla quedaría vacía o incompleta. Con setAutoCommit(false) los
     * cambios solo se vuelven definitivos en commit(); si algo falla, rollback()
     * deja la base exactamente como estaba antes.
     */
    @Override
    public void reemplazarTabla(int torneoId, List<PosicionTabla> nuevaTabla) {
        String borrar = "DELETE FROM posicion_tabla WHERE torneo_id = ?";
        String insertar = "INSERT INTO posicion_tabla (torneo_id, equipo_id, pj, pg, pe, pp, gf, gc, pts) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = GestorConexionJDBC.obtenerConexion()) {
            c.setAutoCommit(false);
            try (PreparedStatement psBorrar = c.prepareStatement(borrar);
                 PreparedStatement psInsertar = c.prepareStatement(insertar)) {

                psBorrar.setInt(1, torneoId);
                psBorrar.executeUpdate();

                for (PosicionTabla p : nuevaTabla) {
                    psInsertar.setInt(1, torneoId);
                    psInsertar.setInt(2, p.getEquipo().getId());
                    psInsertar.setInt(3, p.getPartidosJugados());
                    psInsertar.setInt(4, p.getPartidosGanados());
                    psInsertar.setInt(5, p.getPartidosEmpatados());
                    psInsertar.setInt(6, p.getPartidosPerdidos());
                    psInsertar.setInt(7, p.getGolesFavor());
                    psInsertar.setInt(8, p.getGolesContra());
                    psInsertar.setInt(9, p.getPuntos());
                    psInsertar.addBatch(); // se acumulan y se mandan todos juntos
                }
                psInsertar.executeBatch();
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error actualizando la tabla de posiciones del torneo " + torneoId, e);
        }
    }

    @Override
    public List<PosicionTabla> buscarPorTorneo(int torneoId) {
        String sql = "SELECT pt.pj, pt.pg, pt.pe, pt.pp, pt.gf, pt.gc, pt.pts, "
                + "e.id, e.nombre, e.capitan_id, e.torneo_id, e.estado "
                + "FROM posicion_tabla pt JOIN equipo e ON e.id = pt.equipo_id "
                + "WHERE pt.torneo_id = ?";
        List<PosicionTabla> resultado = new ArrayList<>();
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, torneoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Equipo equipo = new Equipo(rs.getInt("id"), rs.getString("nombre"),
                            rs.getInt("capitan_id"), rs.getInt("torneo_id"));
                    equipo.setEstado(EstadoInscripcion.valueOf(rs.getString("estado")));
                    resultado.add(new PosicionTabla(torneoId, equipo, rs.getInt("pj"), rs.getInt("pg"),
                            rs.getInt("pe"), rs.getInt("pp"), rs.getInt("gf"), rs.getInt("gc"), rs.getInt("pts")));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error consultando la tabla de posiciones del torneo " + torneoId, e);
        }
        return resultado;
    }
}
