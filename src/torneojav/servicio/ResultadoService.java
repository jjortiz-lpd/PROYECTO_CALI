package torneojav.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.excepcion.ActaCerradaException;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.modelo.EstadoPartido;
import torneojav.modelo.EventoPartido;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Jugador;
import torneojav.modelo.Partido;
import torneojav.modelo.Rol;
import torneojav.modelo.TipoEvento;
import torneojav.repositorio.EventoRepositorio;
import torneojav.repositorio.JugadorRepositorio;
import torneojav.repositorio.PartidoRepositorio;
import torneojav.repositorio.TorneoRepositorio;

public class ResultadoService {

    private final PartidoRepositorio partidos;
    private final EventoRepositorio eventos;
    private final JugadorRepositorio jugadores;
    private final TorneoRepositorio torneos;
    private final AutenticacionService auth;

    public ResultadoService(PartidoRepositorio partidos, EventoRepositorio eventos,
                            JugadorRepositorio jugadores, TorneoRepositorio torneos,
                            AutenticacionService auth) {
        this.partidos = partidos;
        this.eventos = eventos;
        this.jugadores = jugadores;
        this.torneos = torneos;
        this.auth = auth;
    }

    public List<Partido> partidosProgramados(int torneoId) {
        auth.verificarRol(Rol.ARBITRO);
        List<Partido> resultado = new ArrayList<>();
        for (Partido p : partidos.buscarPorTorneo(torneoId)) {
            if (p.getEstado() != EstadoPartido.FINALIZADO) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public EventoPartido registrarEvento(int partidoId, TipoEvento tipo, int minuto, int jugadorId) {
        auth.verificarRol(Rol.ARBITRO);
        Partido partido = buscarPartido(partidoId);
        verificarActaAbierta(partido);

        if (tipo == null) {
            throw new DatosInvalidosException("El tipo de evento es obligatorio");
        }
        if (minuto < 0 || minuto > 130) {
            throw new DatosInvalidosException("El minuto debe estar entre 0 y 130");
        }

        Optional<Jugador> jugador = jugadores.buscarPorId(jugadorId);
        if (jugador.isEmpty()) {
            throw new DatosInvalidosException("No existe un jugador con id " + jugadorId);
        }
        int equipoId = jugador.get().getEquipoId();
        if (equipoId != partido.getLocal().getId() && equipoId != partido.getVisitante().getId()) {
            throw new DatosInvalidosException("El jugador no pertenece a ninguno de los equipos del partido");
        }

        EventoPartido evento = eventos.guardar(new EventoPartido(0, partidoId, tipo, minuto, jugadorId, equipoId));
        partido.setEstado(EstadoPartido.EN_CURSO);
        actualizarMarcador(partido);
        return evento;
    }

    public Partido calcularResultado(int partidoId) {
        auth.verificarRol(Rol.ARBITRO);
        Partido partido = buscarPartido(partidoId);
        actualizarMarcador(partido);
        return partido;
    }

    public Partido cerrarActa(int partidoId) {
        auth.verificarRol(Rol.ARBITRO);
        Partido partido = buscarPartido(partidoId);
        verificarActaAbierta(partido);

        actualizarMarcador(partido);
        verificarQueHayaGanadorSiEsEliminacion(partido);
        partido.setActaCerrada(true);
        partido.setEstado(EstadoPartido.FINALIZADO);
        return partidos.guardar(partido);
    }

    public List<EventoPartido> eventosDelPartido(int partidoId) {
        buscarPartido(partidoId);
        return eventos.buscarPorPartido(partidoId);
    }

    private void actualizarMarcador(Partido partido) {
        int golesLocal = 0;
        int golesVisitante = 0;
        for (EventoPartido e : eventos.buscarPorPartido(partido.getId())) {
            if (e.getTipo() == TipoEvento.GOL) {
                if (e.getEquipoId() == partido.getLocal().getId()) {
                    golesLocal++;
                } else {
                    golesVisitante++;
                }
            }
        }
        partido.setGolesLocal(golesLocal);
        partido.setGolesVisitante(golesVisitante);
        partidos.guardar(partido);
    }

    /*
     * En eliminación directa el que gana pasa a la siguiente ronda (HU-07), entonces
     * no se puede cerrar un acta empatada. El árbitro registra primero el gol que
     * define el partido (por ejemplo, el de la tanda de penales).
     */
    private void verificarQueHayaGanadorSiEsEliminacion(Partido partido) {
        boolean esEliminacion = torneos.buscarPorId(partido.getTorneoId())
                .map(t -> t.getFormato() == FormatoTorneo.ELIMINACION_DIRECTA)
                .orElse(false);
        if (esEliminacion && partido.getGolesLocal() == partido.getGolesVisitante()) {
            throw new DatosInvalidosException("En eliminación directa no puede haber empate. Registre el gol que "
                    + "define el partido antes de cerrar el acta");
        }
    }

    private void verificarActaAbierta(Partido partido) {
        if (partido.isActaCerrada()) {
            throw new ActaCerradaException("El acta del partido " + partido.getId() + " ya está cerrada y no se puede modificar");
        }
    }

    private Partido buscarPartido(int partidoId) {
        Optional<Partido> encontrado = partidos.buscarPorId(partidoId);
        if (encontrado.isEmpty()) {
            throw new DatosInvalidosException("No existe un partido con id " + partidoId);
        }
        return encontrado.get();
    }
}
