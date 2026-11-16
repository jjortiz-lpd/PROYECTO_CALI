package torneojav.servicio.calendario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.modelo.Equipo;
import torneojav.modelo.EstadoPartido;
import torneojav.modelo.Partido;
import torneojav.modelo.Torneo;

/**
 * Eliminación directa por rondas (HU-07 CA-04).
 *
 * Solo se puede armar la primera ronda al inicio, porque las siguientes dependen
 * de quién gane. Cuando todos los partidos de una ronda terminan, el administrador
 * genera la ronda siguiente con los ganadores (generarSiguienteRonda).
 *
 * Si el número de equipos no es potencia de 2 (2, 4, 8, 16...) algunos equipos pasan
 * directo a la ronda 2 sin jugar ("bye"). Ejemplo con 5 equipos:
 *   el cuadro se completa a 8, entonces hay 8 - 5 = 3 byes.
 *   Ronda 1: juegan los 2 equipos restantes (1 partido).
 *   Ronda 2: los 3 que descansaron + el ganador = 4 equipos (2 partidos).
 *   Ronda 3: final.
 */
public class GeneradorEliminacionDirecta implements IGeneradorCalendario {

    @Override
    public List<Partido> generarPartidos(Torneo torneo, List<Equipo> equiposAprobados) {
        int n = equiposAprobados.size();
        int byes = potenciaDeDosMayorOIgual(n) - n;

        // Los primeros "byes" equipos descansan; el resto juega la ronda 1
        List<Equipo> juegan = equiposAprobados.subList(byes, n);
        return emparejar(torneo, juegan, 1);
    }

    /**
     * Arma la siguiente ronda con los ganadores de la última ronda jugada.
     *
     * @param partidosExistentes todos los partidos del torneo que ya están en la base
     */
    public List<Partido> generarSiguienteRonda(Torneo torneo, List<Equipo> equiposAprobados,
                                               List<Partido> partidosExistentes) {
        if (partidosExistentes.isEmpty()) {
            throw new DatosInvalidosException("Primero hay que generar el calendario del torneo");
        }

        int ultimaRonda = 0;
        for (Partido p : partidosExistentes) {
            ultimaRonda = Math.max(ultimaRonda, p.getRonda());
        }

        List<Partido> partidosUltimaRonda = new ArrayList<>();
        for (Partido p : partidosExistentes) {
            if (p.getRonda() == ultimaRonda) {
                partidosUltimaRonda.add(p);
            }
        }

        List<Equipo> ganadores = new ArrayList<>();
        for (Partido p : partidosUltimaRonda) {
            if (p.getEstado() != EstadoPartido.FINALIZADO) {
                throw new DatosInvalidosException("La ronda " + ultimaRonda + " no ha terminado: falta cerrar el acta del partido "
                        + p.getId() + " (" + p.getLocal().getNombre() + " vs " + p.getVisitante().getNombre() + ")");
            }
            ganadores.add(ganador(p));
        }

        List<Equipo> siguientes = new ArrayList<>();
        if (ultimaRonda == 1) {
            // A la ronda 2 entran también los equipos que tuvieron bye en la ronda 1
            Set<Integer> jugaronRonda1 = new HashSet<>();
            for (Partido p : partidosUltimaRonda) {
                jugaronRonda1.add(p.getLocal().getId());
                jugaronRonda1.add(p.getVisitante().getId());
            }
            for (Equipo e : equiposAprobados) {
                if (!jugaronRonda1.contains(e.getId())) {
                    siguientes.add(e);
                }
            }
        }
        siguientes.addAll(ganadores);

        if (siguientes.size() == 1) {
            throw new DatosInvalidosException("El torneo ya terminó. Campeón: " + siguientes.get(0).getNombre());
        }
        return emparejar(torneo, siguientes, ultimaRonda + 1);
    }

    /** Empareja de a dos en el orden de la lista: (0 vs 1), (2 vs 3), ... */
    private List<Partido> emparejar(Torneo torneo, List<Equipo> equipos, int ronda) {
        LocalDate fecha = torneo.getFechaInicio().plusWeeks(ronda - 1);
        List<Partido> partidos = new ArrayList<>();
        for (int i = 0; i + 1 < equipos.size(); i += 2) {
            partidos.add(new Partido(0, torneo.getId(), equipos.get(i), equipos.get(i + 1), fecha, ronda));
        }
        return partidos;
    }

    private Equipo ganador(Partido p) {
        if (p.getGolesLocal() == p.getGolesVisitante()) {
            throw new DatosInvalidosException("El partido " + p.getId() + " terminó empatado y en eliminación directa "
                    + "tiene que haber un ganador");
        }
        return p.getGolesLocal() > p.getGolesVisitante() ? p.getLocal() : p.getVisitante();
    }

    /** 3 -> 4, 4 -> 4, 5 -> 8, 9 -> 16 */
    private int potenciaDeDosMayorOIgual(int n) {
        int p = 1;
        while (p < n) {
            p *= 2;
        }
        return p;
    }

    @Override
    public String obtenerNombreFormato() {
        return "Eliminación directa";
    }
}
