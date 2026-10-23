package torneojav.repositorio;

import java.util.List;
import torneojav.modelo.PosicionTabla;

public interface PosicionRepositorio {

    /** Cambia la tabla completa de un torneo por la nueva (todo o nada). */
    void reemplazarTabla(int torneoId, List<PosicionTabla> nuevaTabla);

    List<PosicionTabla> buscarPorTorneo(int torneoId);
}
