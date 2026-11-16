package torneojav.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.modelo.Partido;

public class PartidoRepositorioMemoria implements PartidoRepositorio {

    private final List<Partido> partidos = new ArrayList<>();
    private int siguienteId = 1;

    @Override
    public Partido guardar(Partido partido) {
        if (partido.getId() == 0) {
            partido.setId(siguienteId);
            siguienteId++;
            partidos.add(partido);
        } else {
            for (int i = 0; i < partidos.size(); i++) {
                if (partidos.get(i).getId() == partido.getId()) {
                    partidos.set(i, partido);
                }
            }
        }
        return partido;
    }

    @Override
    public Optional<Partido> buscarPorId(int id) {
        for (Partido p : partidos) {
            if (p.getId() == id) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Partido> buscarPorTorneo(int torneoId) {
        List<Partido> resultado = new ArrayList<>();
        for (Partido p : partidos) {
            if (p.getTorneoId() == torneoId) {
                resultado.add(p);
            }
        }
        return resultado;
    }
}
