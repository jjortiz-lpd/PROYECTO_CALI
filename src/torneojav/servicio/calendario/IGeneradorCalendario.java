package torneojav.servicio.calendario;

import java.util.List;
import torneojav.modelo.Equipo;
import torneojav.modelo.Partido;
import torneojav.modelo.Torneo;

/**
 * Estrategia para armar el calendario de un torneo (HU-07).
 * Cada formato de competencia tiene su propia implementación, así que para
 * agregar un formato nuevo basta con crear otra clase (principio abierto/cerrado).
 */
public interface IGeneradorCalendario {

    /**
     * Arma los partidos iniciales del torneo. No los guarda: solo los construye.
     * Los partidos se devuelven con id 0 para que el repositorio les asigne uno.
     */
    List<Partido> generarPartidos(Torneo torneo, List<Equipo> equiposAprobados);

    String obtenerNombreFormato();
}
