import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import torneojav.excepcion.CredencialesInvalidasException;
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadoInscripcion;
import torneojav.modelo.EventoPartido;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Jugador;
import torneojav.modelo.Partido;
import torneojav.modelo.TipoEvento;
import torneojav.modelo.Torneo;
import torneojav.modelo.Usuario;
import torneojav.repositorio.EquipoRepositorio;
import torneojav.repositorio.EquipoRepositorioMemoria;
import torneojav.repositorio.EventoRepositorioMemoria;
import torneojav.repositorio.JugadorRepositorio;
import torneojav.repositorio.JugadorRepositorioMemoria;
import torneojav.repositorio.PartidoRepositorio;
import torneojav.repositorio.PartidoRepositorioMemoria;
import torneojav.repositorio.UsuarioRepositorioMemoria;
import torneojav.servicio.AutenticacionService;
import torneojav.servicio.EquipoService;
import torneojav.servicio.InscripcionService;
import torneojav.servicio.JugadorService;
import torneojav.servicio.ResultadoService;

public class App {

    private static final Scanner sc = new Scanner(System.in);
    private static final Torneo torneo = new Torneo(1, "Copa Javeriana", LocalDate.of(2026, 10, 15),
            FormatoTorneo.TODOS_CONTRA_TODOS);

    private static AutenticacionService auth;
    private static EquipoService equipoService;
    private static JugadorService jugadorService;
    private static InscripcionService inscripcionService;
    private static ResultadoService resultadoService;

    public static void main(String[] args) {
        EquipoRepositorio equipos = new EquipoRepositorioMemoria();
        JugadorRepositorio jugadores = new JugadorRepositorioMemoria();
        PartidoRepositorio partidos = new PartidoRepositorioMemoria();

        auth = new AutenticacionService(new UsuarioRepositorioMemoria());
        equipoService = new EquipoService(equipos, auth);
        jugadorService = new JugadorService(jugadores, equipos, auth);
        inscripcionService = new InscripcionService(equipos, auth);
        resultadoService = new ResultadoService(partidos, new EventoRepositorioMemoria(), jugadores, auth);

        cargarDatosDePrueba(equipos, jugadores, partidos);

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
            System.out.println("\n--- Menú Administrador ---");
            System.out.println("1. Ver equipos pendientes de aprobación");
            System.out.println("2. Aprobar inscripción");
            System.out.println("3. Rechazar inscripción");
            System.out.println("4. Ver equipos aprobados");
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
                    default:
                        System.out.println("Opción no válida");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
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

    private static void cargarDatosDePrueba(EquipoRepositorio equipos, JugadorRepositorio jugadores,
                                            PartidoRepositorio partidos) {
        Equipo tigres = equipos.guardar(new Equipo(0, "Tigres", 2, torneo.getId()));
        Equipo leones = equipos.guardar(new Equipo(0, "Leones", 4, torneo.getId()));
        equipos.guardar(new Equipo(0, "Pumas", 2, torneo.getId()));
        tigres.setEstado(EstadoInscripcion.APROBADO);
        leones.setEstado(EstadoInscripcion.APROBADO);

        String[] nombresTigres = {"Andrés Gómez", "Juan Pérez", "Carlos Ruiz", "Mateo Díaz",
                "Felipe Torres", "Santiago Rojas", "David López", "Nicolás Vargas"};
        for (int i = 0; i < nombresTigres.length; i++) {
            jugadores.guardar(new Jugador(0, nombresTigres[i], "1000" + i, LocalDate.of(2003, 1 + i, 10),
                    i + 1, tigres.getId()));
        }

        String[] nombresLeones = {"Sebastián Mora", "Daniel Castro", "Julián Herrera", "Tomás Ortiz",
                "Miguel Ramírez", "Esteban Silva", "Pablo Medina"};
        for (int i = 0; i < nombresLeones.length; i++) {
            jugadores.guardar(new Jugador(0, nombresLeones[i], "2000" + i, LocalDate.of(2002, 1 + i, 20),
                    i + 1, leones.getId()));
        }

        partidos.guardar(new Partido(0, torneo.getId(), tigres, leones, torneo.getFechaInicio(), 1));
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
