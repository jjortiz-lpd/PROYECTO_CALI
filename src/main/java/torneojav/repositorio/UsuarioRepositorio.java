package torneojav.repositorio;

import java.util.Optional;
import torneojav.modelo.Usuario;

public interface UsuarioRepositorio {
    Optional<Usuario> buscarPorNombre(String nombreUsuario);
}
