package torneojav;

import torneojav.repositorio.EquipoRepositorio;
import torneojav.repositorio.EquipoRepositorioPostgres;
import torneojav.repositorio.EventoRepositorioPostgres;
import torneojav.repositorio.JugadorRepositorio;
import torneojav.repositorio.JugadorRepositorioPostgres;
import torneojav.repositorio.PartidoRepositorio;
import torneojav.repositorio.PartidoRepositorioPostgres;
import torneojav.repositorio.PosicionRepositorioPostgres;
import torneojav.repositorio.TorneoRepositorio;
import torneojav.repositorio.TorneoRepositorioPostgres;
import torneojav.repositorio.UsuarioRepositorioPostgres;
import torneojav.servicio.AutenticacionService;
import torneojav.servicio.CalendarioService;
import torneojav.servicio.ConsultaService;
import torneojav.servicio.EquipoService;
import torneojav.servicio.FormatoService;
import torneojav.servicio.InscripcionService;
import torneojav.servicio.JugadorService;
import torneojav.servicio.ResultadoService;
import torneojav.servicio.TablaPosicionesService;
import torneojav.servicio.TorneoService;
import torneojav.servicio.posiciones.CriterioEstandarTorneo;

/** Arma los repositorios y servicios una sola vez. Lo comparten la consola y JavaFX. */
public class Contexto {

    public final TorneoRepositorio torneos;
    public final AutenticacionService auth;
    public final EquipoService equipoService;
    public final JugadorService jugadorService;
    public final InscripcionService inscripcionService;
    public final ResultadoService resultadoService;
    public final CalendarioService calendarioService;
    public final TorneoService torneoService;
    public final FormatoService formatoService;
    public final TablaPosicionesService tablaService;
    public final ConsultaService consultaService;

    public Contexto() {
        EquipoRepositorio equipos = new EquipoRepositorioPostgres();
        JugadorRepositorio jugadores = new JugadorRepositorioPostgres();
        PartidoRepositorio partidos = new PartidoRepositorioPostgres();
        torneos = new TorneoRepositorioPostgres();
        EventoRepositorioPostgres eventos = new EventoRepositorioPostgres();

        auth = new AutenticacionService(new UsuarioRepositorioPostgres());
        equipoService = new EquipoService(equipos, auth);
        jugadorService = new JugadorService(jugadores, equipos, auth);
        inscripcionService = new InscripcionService(equipos, auth);
        tablaService = new TablaPosicionesService(new PosicionRepositorioPostgres(), partidos, equipos,
                new CriterioEstandarTorneo(), auth);
        resultadoService = new ResultadoService(partidos, eventos, jugadores, torneos, tablaService, auth);
        consultaService = new ConsultaService(torneos, partidos, eventos, tablaService, auth);
        calendarioService = new CalendarioService(torneos, equipos, partidos, auth);
        torneoService = new TorneoService(torneos, equipos, partidos, auth);
        formatoService = new FormatoService(torneos, partidos, auth);
    }
}