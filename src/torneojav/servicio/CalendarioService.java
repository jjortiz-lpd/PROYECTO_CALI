package torneojav.servicio;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.excepcion.FormatoNoDefinidoException;
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadoInscripcion;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Partido;
import torneojav.modelo.Rol;
import torneojav.modelo.Torneo;
import torneojav.repositorio.EquipoRepositorio;
import torneojav.repositorio.PartidoRepositorio;
import torneojav.repositorio.TorneoRepositorio;
import torneojav.servicio.calendario.GeneradorEliminacionDirecta;
import torneojav.servicio.calendario.GeneradorTodosContraTodos;
import torneojav.servicio.calendario.IGeneradorCalendario;

/**
 * HU-07: generación automática del calendario.
 * El servicio valida las reglas y escoge el generador según el formato del torneo;
 * el algoritmo de emparejamiento vive en cada generador.
 */
public class CalendarioService {

    private final TorneoRepositorio torneos;
    private final EquipoRepositorio equipos;
    private final PartidoRepositorio partidos;
    private final AutenticacionService auth;
    private final Map<FormatoTorneo, IGeneradorCalendario> generadores = new EnumMap<>(FormatoTorneo.class);
    private final GeneradorEliminacionDirecta eliminacion = new GeneradorEliminacionDirecta();

    public CalendarioService(TorneoRepositorio torneos, EquipoRepositorio equipos,
                             PartidoRepositorio partidos, AutenticacionService auth) {
        this.torneos = torneos;
        this.equipos = equipos;
        this.partidos = partidos;
        this.auth = auth;
        generadores.put(FormatoTorneo.TODOS_CONTRA_TODOS, new GeneradorTodosContraTodos());
        generadores.put(FormatoTorneo.ELIMINACION_DIRECTA, eliminacion);
    }

    public List<Partido> generarCalendario(int torneoId) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        Torneo torneo = buscarTorneo(torneoId);

        // CA-02 (y HU-03 CA-06): sin formato no hay calendario
        if (!torneo.tieneFormato()) {
            throw new FormatoNoDefinidoException("El torneo no tiene formato definido. Configúrelo antes de generar el calendario");
        }
        // CA-05: si ya hay partidos, generar otra vez los duplicaría
        if (!partidos.buscarPorTorneo(torneoId).isEmpty()) {
            throw new DatosInvalidosException("El calendario de este torneo ya fue generado");
        }

        // CA-01: solo equipos aprobados
        List<Equipo> aprobados = equiposAprobados(torneoId);
        if (aprobados.size() < 2) {
            throw new DatosInvalidosException("Se necesitan al menos 2 equipos aprobados para generar el calendario (hay "
                    + aprobados.size() + ")");
        }

        List<Partido> nuevos = generadores.get(torneo.getFormato()).generarPartidos(torneo, aprobados);
        return guardar(nuevos);
    }

    /** Solo para eliminación directa: arma la ronda siguiente con los ganadores. */
    public List<Partido> generarSiguienteRonda(int torneoId) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        Torneo torneo = buscarTorneo(torneoId);
        if (torneo.getFormato() != FormatoTorneo.ELIMINACION_DIRECTA) {
            throw new DatosInvalidosException("Las rondas siguientes solo existen en eliminación directa. "
                    + "En todos contra todos el calendario completo se genera de una vez");
        }
        List<Partido> nuevos = eliminacion.generarSiguienteRonda(torneo, equiposAprobados(torneoId),
                partidos.buscarPorTorneo(torneoId));
        return guardar(nuevos);
    }

    // CA-07
    public List<Partido> consultarCalendario(int torneoId) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        buscarTorneo(torneoId);
        return partidos.buscarPorTorneo(torneoId);
    }

    /** CA-05 y CA-06 se revisan aquí antes de guardar; CA-08: cada partido queda en la base. */
    private List<Partido> guardar(List<Partido> nuevos) {
        Set<String> parejas = new HashSet<>();
        for (Partido p : nuevos) {
            if (p.getLocal() == null || p.getVisitante() == null || p.getFecha() == null || p.getEstado() == null) {
                throw new DatosInvalidosException("Se generó un partido incompleto; no se guardó el calendario");
            }
            int menor = Math.min(p.getLocal().getId(), p.getVisitante().getId());
            int mayor = Math.max(p.getLocal().getId(), p.getVisitante().getId());
            if (!parejas.add(menor + "-" + mayor)) {
                throw new DatosInvalidosException("Se generó un partido duplicado; no se guardó el calendario");
            }
        }
        for (Partido p : nuevos) {
            partidos.guardar(p);
        }
        return nuevos;
    }

    private List<Equipo> equiposAprobados(int torneoId) {
        List<Equipo> lista = equipos.buscarPorTorneo(torneoId);
        lista.removeIf(e -> e.getEstado() != EstadoInscripcion.APROBADO);
        return lista;
    }

    private Torneo buscarTorneo(int torneoId) {
        return torneos.buscarPorId(torneoId)
                .orElseThrow(() -> new DatosInvalidosException("No existe un torneo con id " + torneoId));
    }
}
