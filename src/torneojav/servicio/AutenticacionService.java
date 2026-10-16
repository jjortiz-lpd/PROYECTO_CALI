package torneojav.servicio;

import java.util.Optional;
import torneojav.excepcion.AccesoDenegadoException;
import torneojav.excepcion.CredencialesInvalidasException;
import torneojav.modelo.Rol;
import torneojav.modelo.Usuario;
import torneojav.repositorio.UsuarioRepositorio;

public class AutenticacionService {

    private final UsuarioRepositorio repositorio;
    private Usuario usuarioActual;

    public AutenticacionService(UsuarioRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasena) {
        Optional<Usuario> encontrado = repositorio.buscarPorNombre(nombreUsuario);
        if (encontrado.isEmpty() || !encontrado.get().getContrasena().equals(contrasena)) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }
        usuarioActual = encontrado.get();
        return usuarioActual;
    }

    public void cerrarSesion() {
        usuarioActual = null;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void verificarRol(Rol... permitidos) {
        if (usuarioActual == null) {
            throw new AccesoDenegadoException("Debe iniciar sesión");
        }
        for (Rol r : permitidos) {
            if (usuarioActual.getRol() == r) {
                return;
            }
        }
        throw new AccesoDenegadoException("No tiene permiso para esta acción");
    }
}
