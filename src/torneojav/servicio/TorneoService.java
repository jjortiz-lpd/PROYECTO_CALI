package torneojav.servicio;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Rol;
import torneojav.modelo.Torneo;
import torneojav.repositorio.EquipoRepositorio;
import torneojav.repositorio.PartidoRepositorio;
import torneojav.repositorio.TorneoRepositorio;

/**
 * HU-02: gestión de torneos. Todas las operaciones son del Administrador.
 */
public class TorneoService {

    private final TorneoRepositorio repo;
    private final EquipoRepositorio equipos;
    private final PartidoRepositorio partidos;
    private final AutenticacionService auth;

    public TorneoService(TorneoRepositorio repo, EquipoRepositorio equipos,
                         PartidoRepositorio partidos, AutenticacionService auth) {
        this.repo = repo;
        this.equipos = equipos;
        this.partidos = partidos;
        this.auth = auth;
    }

    // CA-01: crear con nombre, fecha y formato
    public Torneo crear(String nombre, LocalDate fechaInicio, FormatoTorneo formato) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        Torneo torneo = new Torneo(0, nombre == null ? null : nombre.trim(), fechaInicio, formato);
        validar(torneo);
        return repo.guardar(torneo);
    }

    // CA-03: consultar
    public List<Torneo> listar() {
        auth.verificarRol(Rol.ADMINISTRADOR);
        return repo.listarTodos();
    }

    public Optional<Torneo> buscarPorId(int id) {
        return repo.buscarPorId(id);
    }

    /**
     * CA-04: modificar nombre y fecha de inicio.
     * El formato se cambia por FormatoService (HU-03), que revisa si ya hay calendario.
     */
    public Torneo actualizar(int id, String nuevoNombre, LocalDate nuevaFecha) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        Torneo torneo = buscar(id);

        if (nuevaFecha != null && !nuevaFecha.equals(torneo.getFechaInicio()) && tienePartidos(id)) {
            throw new DatosInvalidosException("No se puede cambiar la fecha de inicio: el calendario ya fue generado "
                    + "con la fecha actual");
        }
        torneo.setNombre(nuevoNombre == null ? null : nuevoNombre.trim());
        torneo.setFechaInicio(nuevaFecha);
        validar(torneo);
        return repo.guardar(torneo);
    }

    /**
     * CA-05: solo se elimina si no tiene información que lo impida (equipos inscritos o partidos).
     * La confirmación (CA-06) la pide la interfaz antes de llamar a este método.
     */
    public void eliminar(int id) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        buscar(id);
        if (tienePartidos(id)) {
            throw new DatosInvalidosException("No se puede eliminar: el torneo ya tiene partidos generados");
        }
        if (!equipos.buscarPorTorneo(id).isEmpty()) {
            throw new DatosInvalidosException("No se puede eliminar: el torneo tiene equipos inscritos");
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

    private Torneo buscar(int id) {
        return repo.buscarPorId(id)
                .orElseThrow(() -> new DatosInvalidosException("No existe un torneo con id " + id));
    }

    private boolean tienePartidos(int id) {
        return !partidos.buscarPorTorneo(id).isEmpty();
    }
}
