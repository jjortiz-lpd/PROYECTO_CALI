package torneojav.repositorio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import torneojav.excepcion.PersistenciaException;
import torneojav.modelo.Rol;
import torneojav.modelo.Usuario;
import torneojav.persistencia.GestorConexionJDBC;

public class UsuarioRepositorioPostgres implements UsuarioRepositorio {

    @Override
    public Optional<Usuario> buscarPorNombre(String nombreUsuario) {
        String sql = "SELECT id, nombre_usuario, contrasena, rol FROM usuario WHERE nombre_usuario = ?";
        try (Connection c = GestorConexionJDBC.obtenerConexion();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Usuario(rs.getInt("id"), rs.getString("nombre_usuario"),
                            rs.getString("contrasena"), Rol.valueOf(rs.getString("rol"))));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error buscando el usuario " + nombreUsuario, e);
        }
    }
}
