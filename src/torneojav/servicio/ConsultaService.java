package torneojav.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadisticasTorneo;
import torneojav.modelo.EstadoPartido;
import torneojav.modelo.EventoPartido;
import torneojav.modelo.Partido;
import torneojav.modelo.PosicionTabla;
import torneojav.modelo.Rol;
import torneojav.modelo.TipoEvento;
import torneojav.modelo.Torneo;
import torneojav.repositorio.EventoRepositorio;
import torneojav.repositorio.PartidoRepositorio;
import torneojav.repositorio.TorneoRepositorio;

/**
 * HU-10: consulta de calendario, resultados, posiciones y estadísticas.
 *
 * CA-07: todos los métodos exigen haber iniciado sesión (cualquiera de los 3 roles).
 * CA-08: ningún método escribe en la base; solo leen.
 */
public class ConsultaService {

    private final TorneoRepositorio torneos;
    private final PartidoRepositorio partidos;
    private final EventoRepositorio eventos;
    private final TablaPosicionesService tabla;
    private final AutenticacionService auth;

    public ConsultaService(TorneoRepositorio torneos, PartidoRepositorio partidos, EventoRepositorio eventos,
                           TablaPosicionesService tabla, AutenticacionService auth) {
        this.torneos = torneos;
        this.partidos = partidos;
        this.eventos = eventos;
        this.tabla = tabla;
        this.auth = auth;
    }

    // CA-01: lista para que el usuario escoja el torneo
    public List<Torneo> listarTorneos() {
        verificarSesion();
        return torneos.listarTodos();
    }

    public Torneo seleccionarTorneo(int torneoId) {
        verificarSesion();
        return torneos.buscarPorId(torneoId)
                .orElseThrow(() -> new DatosInvalidosException("No existe un torneo con id " + torneoId));
    }

    // CA-02
    public List<Partido> partidosProgramados(int torneoId) {
        verificarSesion();
        List<Partido> resultado = new ArrayList<>();
        for (Partido p : partidos.buscarPorTorneo(torneoId)) {
            if (p.getEstado() != EstadoPartido.FINALIZADO) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // CA-03
    public List<Partido> partidosFinalizados(int torneoId) {
        verificarSesion();
        List<Partido> resultado = new ArrayList<>();
        for (Partido p : partidos.buscarPorTorneo(torneoId)) {
            if (p.getEstado() == EstadoPartido.FINALIZADO) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    // CA-04
    public List<PosicionTabla> tablaPosiciones(int torneoId) {
        return tabla.consultar(torneoId);
    }

    // CA-05
    public EstadisticasTorneo estadisticas(int torneoId) {
        verificarSesion();
        List<Partido> todos = partidos.buscarPorTorneo(torneoId);

        // (1) resumen de partidos y goles
        int jugados = 0;
        int programados = 0;
        int goles = 0;
        List<Partido> cerrados = new ArrayList<>();
        for (Partido p : todos) {
            if (p.isActaCerrada()) {
                jugados++;
                goles += p.getGolesLocal() + p.getGolesVisitante();
                cerrados.add(p);
            } else {
                programados++;
            }
        }

        // (2) y (3) salen de la tabla de posiciones (GF y GC de cada equipo)
        List<PosicionTabla> posiciones = tabla.consultar(torneoId);
        List<PosicionTabla> masGoleadores = new ArrayList<>();
        List<PosicionTabla> vallasMenosVencidas = new ArrayList<>();
        if (jugados > 0) {
            int maxGF = Integer.MIN_VALUE;
            int minGC = Integer.MAX_VALUE;
            for (PosicionTabla p : posiciones) {
                if (p.getPartidosJugados() == 0) {
                    continue; // un equipo que no ha jugado no cuenta como valla menos vencida
                }
                maxGF = Math.max(maxGF, p.getGolesFavor());
                minGC = Math.min(minGC, p.getGolesContra());
            }
            for (PosicionTabla p : posiciones) {
                if (p.getPartidosJugados() == 0) {
                    continue;
                }
                if (p.getGolesFavor() == maxGF) {
                    masGoleadores.add(p);
                }
                if (p.getGolesContra() == minGC) {
                    vallasMenosVencidas.add(p);
                }
            }
        }

        // (4) balance disciplinario: tarjetas de los partidos con acta cerrada
        int amarillas = 0;
        int rojas = 0;
        Map<String, int[]> porEquipo = new TreeMap<>(); // TreeMap: sale ordenado por nombre
        for (Partido p : cerrados) {
            for (EventoPartido e : eventos.buscarPorPartido(p.getId())) {
                if (e.getTipo() != TipoEvento.TARJETA_AMARILLA && e.getTipo() != TipoEvento.TARJETA_ROJA) {
                    continue;
                }
                String equipo = nombreEquipo(p, e.getEquipoId());
                int[] conteo = porEquipo.computeIfAbsent(equipo, k -> new int[2]);
                if (e.getTipo() == TipoEvento.TARJETA_AMARILLA) {
                    amarillas++;
                    conteo[0]++;
                } else {
                    rojas++;
                    conteo[1]++;
                }
            }
        }

        return new EstadisticasTorneo(jugados, programados, goles, masGoleadores, vallasMenosVencidas,
                amarillas, rojas, porEquipo);
    }

    private String nombreEquipo(Partido p, int equipoId) {
        Equipo e = p.getLocal().getId() == equipoId ? p.getLocal() : p.getVisitante();
        return e.getNombre();
    }

    private void verificarSesion() {
        auth.verificarRol(Rol.ADMINISTRADOR, Rol.CAPITAN, Rol.ARBITRO);
    }
}
