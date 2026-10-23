package torneojav.repositorio;

import java.util.ArrayList;
import java.util.List;
import torneojav.modelo.PosicionTabla;

public class PosicionRepositorioMemoria implements PosicionRepositorio {

    private final List<PosicionTabla> posiciones = new ArrayList<>();

    @Override
    public void reemplazarTabla(int torneoId, List<PosicionTabla> nuevaTabla) {
        posiciones.removeIf(p -> p.getTorneoId() == torneoId);
        posiciones.addAll(nuevaTabla);
    }

    @Override
    public List<PosicionTabla> buscarPorTorneo(int torneoId) {
        List<PosicionTabla> resultado = new ArrayList<>();
        for (PosicionTabla p : posiciones) {
            if (p.getTorneoId() == torneoId) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
