package torneojav.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.modelo.Rol;
import torneojav.modelo.Usuario;

public class UsuarioRepositorioMemoria implements UsuarioRepositorio {

    private final List<Usuario> usuarios = new ArrayList<>();

    public UsuarioRepositorioMemoria() {
        usuarios.add(new Usuario(1, "admin", "admin123", Rol.ADMINISTRADOR));
        usuarios.add(new Usuario(2, "capitan", "cap123", Rol.CAPITAN));
        usuarios.add(new Usuario(3, "arbitro", "arb123", Rol.ARBITRO));
        usuarios.add(new Usuario(4, "capitan2", "cap456", Rol.CAPITAN));
    }

    @Override
    public Optional<Usuario> buscarPorNombre(String nombreUsuario) {
        for (Usuario u : usuarios) {
            if (u.getNombreUsuario().equals(nombreUsuario)) {
                return Optional.of(u);
            }
        }
        return Optional.empty();
    }
}
