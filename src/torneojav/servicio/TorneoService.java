package torneojav.servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Torneo;
import torneojav.repositorio.TorneoRepositorio;

public class TorneoService {

    private final TorneoRepositorio repo;
    // private final AutenticacionService auth;   // se activa cuando llegue HU-01

    public TorneoService(TorneoRepositorio repo) {
        this.repo = repo;
    }

    // CA-01: crear con nombre, fecha y formato
    public Torneo crear(String nombre, LocalDate fechaInicio, FormatoTorneo formato) {
        // auth.verificarRol(Rol.ADMINISTRADOR);
        Torneo torneo = new Torneo(0, nombre, fechaInicio, formato);
        validar(torneo);
        return repo.guardar(torneo);
    }

    // CA-03: consultar
    public List<Torneo> listar() {
        return repo.listarTodos();
    }

    public Optional<Torneo> buscarPorId(int id) {
        return repo.buscarPorId(id);
    }

    // CA-04: modificar
    public Torneo actualizar(Torneo torneo) {
        // auth.verificarRol(Rol.ADMINISTRADOR);
        validar(torneo);
        if (repo.buscarPorId(torneo.getId()).isEmpty()) {
            throw new DatosInvalidosException("No existe un torneo con id " + torneo.getId());
        }
        return repo.guardar(torneo);
    }

    // CA-05: eliminar solo si se puede
    public void eliminar(int id) {
        // auth.verificarRol(Rol.ADMINISTRADOR);
        if (repo.buscarPorId(id).isEmpty()) {
            throw new DatosInvalidosException("No existe un torneo con id " + id);
        }
        if (tienePartidos(id)) {
            throw new DatosInvalidosException("No se puede eliminar: el torneo ya tiene partidos generados");
        }
        repo.eliminar(id);
    }

    // CA-02: campos obligatorios
    private void validar(Torneo torneo) {
        if (torneo.getNombre() == null || torneo.getNombre().isBlank()) {
            throw new DatosInvalidosException("El nombre es obligatorio");
        }
        if (torneo.getFechaInicio() == null) {
            throw new DatosInvalidosException("La fecha de inicio es obligatoria");
        }
        if (torneo.getFormato() == null) {
            throw new DatosInvalidosException("El formato es obligatorio");
        }
    }

    
    private boolean tienePartidos(int id) {
        return false;
    }
}