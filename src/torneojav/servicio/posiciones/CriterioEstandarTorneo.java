package torneojav.servicio.posiciones;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import torneojav.modelo.PosicionTabla;

/**
 * HU-09 CA-05, en este orden:
 *   1.º mayor puntaje (PTS)
 *   2.º mayor diferencia de goles (DG)
 *   3.º mayor cantidad de goles a favor (GF)
 * Si después de eso siguen empatados se ordenan por nombre, solo para que la tabla
 * salga siempre igual y no cambie de orden entre una consulta y otra.
 */
public class CriterioEstandarTorneo implements ICriterioDesempate {

    private static final Comparator<PosicionTabla> ORDEN =
            Comparator.comparingInt(PosicionTabla::getPuntos).reversed()
                    .thenComparing(Comparator.comparingInt(PosicionTabla::calcularDiferenciaGoles).reversed())
                    .thenComparing(Comparator.comparingInt(PosicionTabla::getGolesFavor).reversed())
                    .thenComparing(p -> p.getEquipo().getNombre(), String.CASE_INSENSITIVE_ORDER);

    @Override
    public List<PosicionTabla> ordenarPosiciones(List<PosicionTabla> posiciones) {
        List<PosicionTabla> ordenada = new ArrayList<>(posiciones);
        ordenada.sort(ORDEN);
        return ordenada;
    }
}
