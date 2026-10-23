package torneojav.servicio.calendario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import torneojav.modelo.Equipo;
import torneojav.modelo.Partido;
import torneojav.modelo.Torneo;

/**
 * Todos contra todos a una vuelta: cada pareja de equipos se enfrenta una sola vez (HU-07 CA-03).
 *
 * Usa el "método del círculo":
 *  - Se ponen los equipos en una lista. Si son impares se agrega un hueco (null) que
 *    significa "descansa esta jornada".
 *  - En cada jornada se emparejan el primero con el último, el segundo con el penúltimo, etc.
 *  - Para la jornada siguiente, el primero se queda quieto y los demás giran una posición.
 *  Con n equipos (n par) salen n-1 jornadas y n*(n-1)/2 partidos, sin repetir ninguna pareja.
 *
 * Ejemplo con 4 equipos A B C D:
 *   Jornada 1: A-D, B-C
 *   Jornada 2: A-C, D-B
 *   Jornada 3: A-B, C-D
 */
public class GeneradorTodosContraTodos implements IGeneradorCalendario {

    @Override
    public List<Partido> generarPartidos(Torneo torneo, List<Equipo> equiposAprobados) {
        List<Equipo> rueda = new ArrayList<>(equiposAprobados);
        if (rueda.size() % 2 != 0) {
            rueda.add(null); // equipo "fantasma": quien le toque, descansa
        }

        int n = rueda.size();
        List<Partido> partidos = new ArrayList<>();

        for (int jornada = 1; jornada <= n - 1; jornada++) {
            LocalDate fecha = torneo.getFechaInicio().plusWeeks(jornada - 1);

            for (int i = 0; i < n / 2; i++) {
                Equipo a = rueda.get(i);
                Equipo b = rueda.get(n - 1 - i);
                if (a == null || b == null) {
                    continue; // uno de los dos es el hueco: no hay partido
                }
                // Se alterna quién es local para que el primer equipo no sea siempre local
                if (i == 0 && jornada % 2 == 0) {
                    partidos.add(new Partido(0, torneo.getId(), b, a, fecha, jornada));
                } else {
                    partidos.add(new Partido(0, torneo.getId(), a, b, fecha, jornada));
                }
            }

            // Rotación: el de la posición 0 queda fijo, el último pasa a la posición 1
            Equipo ultimo = rueda.remove(n - 1);
            rueda.add(1, ultimo);
        }
        return partidos;
    }

    @Override
    public String obtenerNombreFormato() {
        return "Todos contra todos";
    }
}
