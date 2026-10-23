import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import torneojav.excepcion.CredencialesInvalidasException;
import torneojav.excepcion.PersistenciaException;
import torneojav.modelo.EstadoInscripcion;
import torneojav.modelo.EventoPartido;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Jugador;
import torneojav.modelo.Partido;
import torneojav.modelo.TipoEvento;
import torneojav.modelo.Torneo;
import torneojav.modelo.Usuario;
import torneojav.persistencia.GestorConexionJDBC;
import torneojav.repositorio.EquipoRepositorio;
import torneojav.repositorio.EquipoRepositorioPostgres;
import torneojav.repositorio.EventoRepositorioPostgres;
import torneojav.repositorio.JugadorRepositorio;
import torneojav.repositorio.JugadorRepositorioPostgres;
import torneojav.repositorio.PartidoRepositorio;
import torneojav.repositorio.PartidoRepositorioPostgres;
import torneojav.repositorio.TorneoRepositorio;
import torneojav.repositorio.TorneoRepositorioPostgres;
import torneojav.repositorio.UsuarioRepositorioPostgres;
import torneojav.servicio.AutenticacionService;
import torneojav.servicio.CalendarioService;
import torneojav.servicio.EquipoService;
import torneojav.servicio.FormatoService;
import torneojav.servicio.InscripcionService;
import torneojav.servicio.JugadorService;
import torneojav.servicio.ResultadoService;
import torneojav.servicio.TorneoService;

public class App {

    private static final Scanner sc = new Scanner(System.in);
    private static Torneo torneo;

    private static AutenticacionService auth;
    private static EquipoService equipoService;
    private static JugadorService jugadorService;
    private static InscripcionService inscripcionService;
    private static ResultadoService resultadoService;
    private static CalendarioService calendarioService;
    private static TorneoService torneoService;
    private static FormatoService formatoService;

    public static void main(String[] args) {
        try {
            GestorConexionJDBC.probarConexion();
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
            System.out.println("Revise que PostgreSQL esté encendido y que db.properties tenga los datos correctos.");
            return;
        }

        EquipoRepositorio equipos = new EquipoRepositorioPostgres();
        JugadorRepositorio jugadores = new JugadorRepositorioPostgres();
        PartidoRepositorio partidos = new PartidoRepositorioPostgres();
        TorneoRepositorio torneos = new TorneoRepositorioPostgres();

        auth = new AutenticacionService(new UsuarioRepositorioPostgres());
        equipoService = new EquipoService(equipos, auth);
        jugadorService = new JugadorService(jugadores, equipos, auth);
        inscripcionService = new InscripcionService(equipos, auth);
        resultadoService = new ResultadoService(partidos, new EventoRepositorioPostgres(), jugadores, torneos, auth);
        calendarioService = new CalendarioService(torneos, equipos, partidos, auth);
        torneoService = new TorneoService(torneos, equipos, partidos, auth);
        formatoService = new FormatoService(torneos, partidos, auth);

        // Se arranca con el primer torneo registrado. El administrador puede cambiarlo
        // con la opción "Seleccionar torneo de trabajo".
        List<Torneo> existentes = torneos.listarTodos();
        if (existentes.isEmpty()) {
            System.out.println("No hay torneos registrados. Ejecute db/schema.sql para cargar los datos de prueba.");
            return;
        }
        torneo = existentes.get(0);

        System.out.println("=== TorneoJAV | " + torneo.getNombre() + " ===");
        while (true) {
            Usuario usuario = login();
            if (usuario == null) {
                break;
            }
            switch (usuario.getRol()) {
                case ADMINISTRADOR:
                    menuAdministrador();
                    break;
                case CAPITAN:
                    menuCapitan();
                    break;
                case ARBITRO:
                    menuArbitro();
                    break;
            }
            auth.cerrarSesion();
            System.out.println("Sesión cerrada.\n");
        }
        System.out.println("Hasta luego.");
    }

    private static Usuario login() {
        while (true) {
            String nombre = leerTexto("Usuario (o 'salir'): ");
            if (nombre.equalsIgnoreCase("salir")) {
                return null;
            }
            String clave = leerTexto("Contraseña: ");
            try {
                Usuario u = auth.iniciarSesion(nombre, clave);
                System.out.println("\nBienvenido " + u.getNombreUsuario() + " (" + u.getRol() + ")");
                return u;
            } catch (CredencialesInvalidasException e) {
                System.out.println("Error: " + e.getMessage() + "\n");
            }
        }
    }

    private static void menuAdministrador() {
        while (true) {
            System.out.println("\n--- Menú Administrador | Torneo de trabajo: " + torneo.getNombre()
                    + " (id " + torneo.getId() + ") ---");
            System.out.println("Inscripciones");
            System.out.println("1. Ver equipos pendientes de aprobación");
            System.out.println("2. Aprobar inscripción");
            System.out.println("3. Rechazar inscripción");
            System.out.println("4. Ver equipos aprobados");
            System.out.println("Calendario");
            System.out.println("5. Generar calendario (" + (torneo.tieneFormato() ? torneo.getFormato() : "sin formato") + ")");
            System.out.println("6. Ver calendario");
            System.out.println("7. Generar siguiente ronda (solo eliminación directa)");
            System.out.println("Torneos");
            System.out.println("8. Crear torneo");
            System.out.println("9. Ver torneos");
            System.out.println("10. Modificar nombre y fecha de un torneo");
            System.out.println("11. Cambiar formato de un torneo");
            System.out.println("12. Eliminar torneo");
            System.out.println("13. Seleccionar torneo de trabajo");
            System.out.println("0. Cerrar sesión");
            int opcion = leerEntero("Opción: ");
            if (opcion == 0) {
                return;
            }
            try {
                switch (opcion) {
                    case 1:
                        imprimir(inscripcionService.listarPendientes(torneo.getId()));
                        break;
                    case 2:
                        System.out.println("Aprobado: " + inscripcionService.aprobar(leerEntero("Id del equipo: ")));
                        break;
                    case 3:
                        System.out.println("Rechazado: " + inscripcionService.rechazar(leerEntero("Id del equipo: ")));
                        break;
                    case 4:
                        imprimir(inscripcionService.equiposAprobados(torneo.getId()));
                        break;
                    case 5:
                        List<Partido> generados = calendarioService.generarCalendario(torneo.getId());
                        System.out.println("Calendario generado con " + generados.size() + " partidos:");
                        imprimirCalendario(generados);
                        break;
                    case 6:
                        imprimirCalendario(calendarioService.consultarCalendario(torneo.getId()));
                        break;
                    case 7:
                        List<Partido> ronda = calendarioService.generarSiguienteRonda(torneo.getId());
                        System.out.println("Ronda " + ronda.get(0).getRonda() + " generada:");
                        imprimirCalendario(ronda);
                        break;
                    case 8:
                        Torneo creado = torneoService.crear(leerTexto("Nombre: "),
                                leerFecha("Fecha de inicio (AAAA-MM-DD): "), elegirFormato());
                        System.out.println("Creado: " + creado);
                        break;
                    case 9:
                        imprimir(torneoService.listar());
                        break;
                    case 10:
                        int idModificar = leerEntero("Id del torneo: ");
                        Torneo modificado = torneoService.actualizar(idModificar, leerTexto("Nuevo nombre: "),
                                leerFecha("Nueva fecha de inicio (AAAA-MM-DD): "));
                        System.out.println("Actualizado: " + modificado);
                        break;
                    case 11:
                        int idFormato = leerEntero("Id del torneo: ");
                        System.out.println("Actualizado: " + formatoService.asignarFormato(idFormato, elegirFormato()));
                        break;
                    case 12:
                        int idEliminar = leerEntero("Id del torneo: ");
                        if (idEliminar == torneo.getId()) {
                            System.out.println("Ese es el torneo de trabajo. Seleccione otro (opción 13) antes de eliminarlo.");
                            break;
                        }
                        // CA-06: confirmación antes de eliminar
                        if (leerTexto("¿Seguro que desea eliminar el torneo " + idEliminar + "? (s/n): ")
                                .equalsIgnoreCase("s")) {
                            torneoService.eliminar(idEliminar);
                            System.out.println("Torneo eliminado");
                        }
                        break;
                    case 13:
                        int idTrabajo = leerEntero("Id del torneo: ");
                        torneo = torneoService.buscarPorId(idTrabajo).orElseThrow(
                                () -> new IllegalArgumentException("No existe un torneo con id " + idTrabajo));
                        System.out.println("Ahora trabaja con: " + torneo);
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
            torneo = torneoService.buscarPorId(torneo.getId()).orElse(torneo);
        }
    }

    private static void menuCapitan() {
        while (true) {
            System.out.println("\n--- Menú Capitán ---");
            System.out.println("1. Registrar equipo");
            System.out.println("2. Ver mis equipos");
            System.out.println("3. Cambiar nombre de un equipo");
            System.out.println("4. Ver estado de inscripción");
            System.out.println("5. Ver jugadores de un equipo");
            System.out.println("6. Agregar jugador");
            System.out.println("7. Editar jugador");
            System.out.println("8. Eliminar jugador");
            System.out.println("0. Cerrar sesión");
            int opcion = leerEntero("Opción: ");
            if (opcion == 0) {
                return;
            }
            try {
                switch (opcion) {
                    case 1:
                        System.out.println("Registrado: " + equipoService.registrar(leerTexto("Nombre del equipo: "), torneo.getId()));
                        break;
                    case 2:
                        imprimir(equipoService.consultarMisEquipos());
                        break;
                    case 3:
                        int id = leerEntero("Id del equipo: ");
                        System.out.println("Actualizado: " + equipoService.modificarNombre(id, leerTexto("Nuevo nombre: ")));
                        break;
                    case 4:
                        EstadoInscripcion estado = inscripcionService.consultarEstado(leerEntero("Id del equipo: "));
                        System.out.println("Estado de la inscripción: " + estado);
                        break;
                    case 5:
                        imprimir(jugadorService.listarPorEquipo(leerEntero("Id del equipo: ")));
                        break;
                    case 6:
                        int equipoId = leerEntero("Id del equipo: ");
                        Jugador nuevo = jugadorService.agregar(equipoId, leerTexto("Nombre completo: "),
                                leerTexto("Documento: "), leerFecha("Fecha de nacimiento (AAAA-MM-DD): "),
                                leerEntero("Número de camiseta: "));
                        System.out.println("Agregado: " + nuevo);
                        break;
                    case 7:
                        int jugadorId = leerEntero("Id del jugador: ");
                        Jugador editado = jugadorService.editar(jugadorId, leerTexto("Nombre completo: "),
                                leerTexto("Documento: "), leerFecha("Fecha de nacimiento (AAAA-MM-DD): "),
                                leerEntero("Número de camiseta: "));
                        System.out.println("Actualizado: " + editado);
                        break;
                    case 8:
                        int borrarId = leerEntero("Id del jugador: ");
                        if (leerTexto("¿Seguro que desea eliminarlo? (s/n): ").equalsIgnoreCase("s")) {
                            jugadorService.eliminar(borrarId);
                            System.out.println("Jugador eliminado");
                        }
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void menuArbitro() {
        while (true) {
            System.out.println("\n--- Menú Árbitro ---");
            System.out.println("1. Ver partidos programados");
            System.out.println("2. Registrar evento (gol, tarjeta, cambio)");
            System.out.println("3. Ver eventos y marcador de un partido");
            System.out.println("4. Cerrar acta");
            System.out.println("0. Cerrar sesión");
            int opcion = leerEntero("Opción: ");
            if (opcion == 0) {
                return;
            }
            try {
                switch (opcion) {
                    case 1:
                        imprimir(resultadoService.partidosProgramados(torneo.getId()));
                        break;
                    case 2:
                        int partidoId = leerEntero("Id del partido: ");
                        TipoEvento tipo = elegirTipoEvento();
                        int minuto = leerEntero("Minuto: ");
                        int jugadorId = leerEntero("Id del jugador: ");
                        EventoPartido evento = resultadoService.registrarEvento(partidoId, tipo, minuto, jugadorId);
                        System.out.println("Registrado: " + evento);
                        break;
                    case 3:
                        int consultaId = leerEntero("Id del partido: ");
                        imprimir(resultadoService.eventosDelPartido(consultaId));
                        System.out.println(resultadoService.calcularResultado(consultaId));
                        break;
                    case 4:
                        int cerrarId = leerEntero("Id del partido: ");
                        if (leerTexto("Después de cerrar no se puede modificar. ¿Continuar? (s/n): ").equalsIgnoreCase("s")) {
                            System.out.println("Acta cerrada: " + resultadoService.cerrarActa(cerrarId));
                        }
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /** HU-03 CA-01: muestra los formatos disponibles para escoger uno. */
    private static FormatoTorneo elegirFormato() {
        FormatoTorneo[] formatos = formatoService.opcionesDisponibles();
        for (int i = 0; i < formatos.length; i++) {
            System.out.println((i + 1) + ". " + formatos[i]);
        }
        int opcion = leerEntero("Formato: ");
        if (opcion < 1 || opcion > formatos.length) {
            return null;
        }
        return formatos[opcion - 1];
    }

    private static TipoEvento elegirTipoEvento() {
        TipoEvento[] tipos = TipoEvento.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ". " + tipos[i]);
        }
        int opcion = leerEntero("Tipo de evento: ");
        if (opcion < 1 || opcion > tipos.length) {
            return null;
        }
        return tipos[opcion - 1];
    }

    /** Muestra los partidos agrupados por ronda (o jornada). */
    private static void imprimirCalendario(List<Partido> partidos) {
        if (partidos.isEmpty()) {
            System.out.println("(el calendario todavía no se ha generado)");
            return;
        }
        int rondaActual = -1;
        for (Partido p : partidos) {
            if (p.getRonda() != rondaActual) {
                rondaActual = p.getRonda();
                System.out.println("-- Ronda " + rondaActual + " (" + p.getFecha() + ")");
            }
            System.out.println("  " + p);
        }
    }

    private static void imprimir(List<?> lista) {
        if (lista.isEmpty()) {
            System.out.println("(sin resultados)");
        }
        for (Object o : lista) {
            System.out.println("  " + o);
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje).trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número.");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje).trim());
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido, use AAAA-MM-DD.");
            }
        }
    }
}
