package torneojav.servicio;

import java.util.List;
import java.util.Optional;
import torneojav.excepcion.AccesoDenegadoException;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.excepcion.EquipoDuplicadoException;
import torneojav.modelo.Equipo;
import torneojav.modelo.Rol;
import torneojav.repositorio.EquipoRepositorio;

public class EquipoService {

    private final EquipoRepositorio repositorio;
    private final AutenticacionService auth;

    public EquipoService(EquipoRepositorio repositorio, AutenticacionService auth) {
        this.repositorio = repositorio;
        this.auth = auth;
    }

    public Equipo registrar(String nombre, int torneoId) {
        auth.verificarRol(Rol.CAPITAN);
        validarNombre(nombre);
        validarNoDuplicado(nombre, torneoId, 0);

        int capitanId = auth.getUsuarioActual().getId();
        Equipo nuevo = new Equipo(0, nombre.trim(), capitanId, torneoId);
        return repositorio.guardar(nuevo);
    }

    public List<Equipo> consultarMisEquipos() {
        auth.verificarRol(Rol.CAPITAN);
        return repositorio.buscarPorCapitan(auth.getUsuarioActual().getId());
    }

    public Equipo modificarNombre(int equipoId, String nuevoNombre) {
        auth.verificarRol(Rol.CAPITAN);
        Equipo equipo = buscarEquipoPropio(equipoId);

        validarNombre(nuevoNombre);
        validarNoDuplicado(nuevoNombre, equipo.getTorneoId(), equipoId);

        equipo.setNombre(nuevoNombre.trim());
        return repositorio.guardar(equipo);
    }

    private Equipo buscarEquipoPropio(int equipoId) {
        Optional<Equipo> encontrado = repositorio.buscarPorId(equipoId);
        if (encontrado.isEmpty()) {
            throw new DatosInvalidosException("No existe un equipo con id " + equipoId);
        }
        Equipo equipo = encontrado.get();
        if (equipo.getCapitanId() != auth.getUsuarioActual().getId()) {
            throw new AccesoDenegadoException("Solo puede modificar su propio equipo");
        }
        return equipo;
    }

    private void validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre del equipo es obligatorio");
        }
    }

    private void validarNoDuplicado(String nombre, int torneoId, int idIgnorado) {
        for (Equipo e : repositorio.buscarPorTorneo(torneoId)) {
            if (e.getId() != idIgnorado && e.getNombre().equalsIgnoreCase(nombre.trim())) {
                throw new EquipoDuplicadoException("Ya existe un equipo llamado '" + nombre.trim() + "' en este torneo");
            }
        }
    }
}
