package torneojav.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadoInscripcion;
import torneojav.modelo.Partido;
import torneojav.modelo.PosicionTabla;
import torneojav.modelo.Rol;
import torneojav.repositorio.EquipoRepositorio;
import torneojav.repositorio.PartidoRepositorio;
import torneojav.repositorio.PosicionRepositorio;
import torneojav.servicio.posiciones.ICriterioDesempate;

/**
 * HU-09: tabla de posiciones.
 *
 * La tabla se recalcula desde cero con todos los partidos de acta cerrada, en vez de
 * sumarle solo el último partido. Así siempre coincide con los resultados guardados
 * (CA-08), aunque se cierren actas en cualquier orden.
 */
public class TablaPosicionesService {

    private final PosicionRepositorio posiciones;
    private final PartidoRepositorio partidos;
    private final EquipoRepositorio equipos;
    private final ICriterioDesempate criterio;
    private final AutenticacionService auth;

    public TablaPosicionesService(PosicionRepositorio posiciones, PartidoRepositorio partidos,
                                  EquipoRepositorio equipos, ICriterioDesempate criterio,
                                  AutenticacionService auth) {
        this.posiciones = posiciones;
        this.partidos = partidos;
        this.equipos = equipos;
        this.criterio = criterio;
        this.auth = auth;
    }

    /**
     * CA-01: lo llama ResultadoService justo después de cerrar un acta.
     * CA-06: solo cuentan los partidos con el acta cerrada; un resultado a medias no mueve la tabla.
     */
    public List<PosicionTabla> recalcular(int torneoId) {
        Map<Integer, PosicionTabla> filas = filasEnCero(torneoId);

        for (Partido p : partidos.buscarPorTorneo(torneoId)) {
            if (!p.isActaCerrada()) {
                continue;
            }
            fila(filas, torneoId, p.getLocal()).registrarResultado(p.getGolesLocal(), p.getGolesVisitante());
            fila(filas, torneoId, p.getVisitante()).registrarResultado(p.getGolesVisitante(), p.getGolesLocal());
        }

        List<PosicionTabla> ordenada = criterio.ordenarPosiciones(List.copyOf(filas.values()));
        posiciones.reemplazarTabla(torneoId, ordenada);
        return ordenada;
    }

    /**
     * CA-07: la puede ver cualquier usuario que haya iniciado sesión.
     * Solo lee: si todavía no se ha cerrado ningún acta, muestra los equipos aprobados en cero
     * sin escribir nada en la base.
     */
    public List<PosicionTabla> consultar(int torneoId) {
        auth.verificarRol(Rol.ADMINISTRADOR, Rol.CAPITAN, Rol.ARBITRO);
        List<PosicionTabla> guardada = posiciones.buscarPorTorneo(torneoId);
        if (guardada.isEmpty()) {
            return criterio.ordenarPosiciones(List.copyOf(filasEnCero(torneoId).values()));
        }
        return criterio.ordenarPosiciones(guardada);
    }

    /** Una fila en cero por cada equipo aprobado, para que salgan aunque no hayan jugado. */
    private Map<Integer, PosicionTabla> filasEnCero(int torneoId) {
        Map<Integer, PosicionTabla> filas = new HashMap<>();
        for (Equipo e : equipos.buscarPorTorneo(torneoId)) {
            if (e.getEstado() == EstadoInscripcion.APROBADO) {
                filas.put(e.getId(), new PosicionTabla(torneoId, e));
            }
        }
        return filas;
    }

    private PosicionTabla fila(Map<Integer, PosicionTabla> filas, int torneoId, Equipo equipo) {
        return filas.computeIfAbsent(equipo.getId(), id -> new PosicionTabla(torneoId, equipo));
    }
}
