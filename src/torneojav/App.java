package torneojav;

import java.time.LocalDate;
import java.util.Scanner;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Torneo;
import torneojav.repositorio.TorneoRepositorioMemoria;
import torneojav.servicio.TorneoService;

public class App {
    public static void main(String[] args) {
        TorneoService servicio = new TorneoService(new TorneoRepositorioMemoria());
        Scanner sc = new Scanner(System.in);

        // 1. Crear un torneo valido
        Torneo copa = servicio.crear("Copa Javeriana", LocalDate.of(2026, 10, 15), FormatoTorneo.TODOS_CONTRA_TODOS);
        System.out.println("Creado: " + copa);

        // 2. Crear uno sin nombre (debe fallar)
        try {
            servicio.crear("", LocalDate.of(2026, 11, 1), FormatoTorneo.ELIMINACION_DIRECTA);
            System.out.println("ERROR: se creo un torneo sin nombre");
        } catch (DatosInvalidosException e) {
            System.out.println("Rechazado como se esperaba: " + e.getMessage());
        }

        // 3. Modificar
        copa.setNombre("Copa Javeriana 2026");
        servicio.actualizar(copa);

        // 4. Listar
        System.out.println("Torneos guardados: " + servicio.listar());

        // 5. Eliminar con confirmacion
        System.out.print("Seguro que quieres eliminar el torneo " + copa.getId() + "? (s/n): ");
        String respuesta = sc.nextLine();
        if (respuesta.equalsIgnoreCase("s")) {
            servicio.eliminar(copa.getId());
            System.out.println("Eliminado. Quedan: " + servicio.listar().size());
        } else {
            System.out.println("Cancelado. Quedan: " + servicio.listar().size());
        }

        sc.close();
    }
}