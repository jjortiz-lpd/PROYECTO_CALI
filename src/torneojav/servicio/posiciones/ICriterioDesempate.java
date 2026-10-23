package torneojav.servicio.posiciones;

import java.util.List;
import torneojav.modelo.PosicionTabla;

/**
 * Regla para ordenar la tabla de posiciones (HU-09 CA-05).
 * Si un torneo quisiera otro orden (por ejemplo, contar primero el partido entre los
 * dos equipos empatados), se crea otra clase sin tocar el servicio.
 */
public interface ICriterioDesempate {

    /** Devuelve una lista nueva ordenada del primer al último puesto. */
    List<PosicionTabla> ordenarPosiciones(List<PosicionTabla> posiciones);
}
