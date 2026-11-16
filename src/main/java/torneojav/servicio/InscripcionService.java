package torneojav.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.excepcion.AccesoDenegadoException;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadoInscripcion;
import torneojav.modelo.Rol;
import torneojav.repositorio.EquipoRepositorio;

public class InscripcionService {

    private final EquipoRepositorio repositorio;
    private final AutenticacionService auth;

    public InscripcionService(EquipoRepositorio repositorio, AutenticacionService auth) {
        this.repositorio = repositorio;
        this.auth = auth;
    }

    public List<Equipo> listarPendientes(int torneoId) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        return filtrarPorEstado(torneoId, EstadoInscripcion.PENDIENTE);
    }

    public Equipo aprobar(int equipoId) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        return cambiarEstado(equipoId, EstadoInscripcion.APROBADO);
    }

    public Equipo rechazar(int equipoId) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        return cambiarEstado(equipoId, EstadoInscripcion.RECHAZADO);
    }

    public EstadoInscripcion consultarEstado(int equipoId) {
        auth.verificarRol(Rol.CAPITAN);
        Equipo equipo = buscarEquipo(equipoId);
        if (equipo.getCapitanId() != auth.getUsuarioActual().getId()) {
            throw new AccesoDenegadoException("Solo puede consultar el estado de su propio equipo");
        }
        return equipo.getEstado();
    }

    public List<Equipo> equiposAprobados(int torneoId) {
        return filtrarPorEstado(torneoId, EstadoInscripcion.APROBADO);
    }

    private Equipo cambiarEstado(int equipoId, EstadoInscripcion nuevoEstado) {
        Equipo equipo = buscarEquipo(equipoId);
        if (equipo.getEstado() != EstadoInscripcion.PENDIENTE) {
            throw new DatosInvalidosException("La inscripción del equipo ya fue resuelta (" + equipo.getEstado() + ")");
        }
        equipo.setEstado(nuevoEstado);
        return repositorio.guardar(equipo);
    }

    private Equipo buscarEquipo(int equipoId) {
        Optional<Equipo> encontrado = repositorio.buscarPorId(equipoId);
        if (encontrado.isEmpty()) {
            throw new DatosInvalidosException("No existe un equipo con id " + equipoId);
        }
        return encontrado.get();
    }

    private List<Equipo> filtrarPorEstado(int torneoId, EstadoInscripcion estado) {
        List<Equipo> resultado = new ArrayList<>();
        for (Equipo e : repositorio.buscarPorTorneo(torneoId)) {
            if (e.getEstado() == estado) {
                resultado.add(e);
            }
        }
        return resultado;
    }
}
