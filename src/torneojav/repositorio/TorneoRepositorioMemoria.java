package torneojav.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.modelo.Torneo;

public class TorneoRepositorioMemoria implements TorneoRepositorio {

    private final List<Torneo> torneos = new ArrayList<>();
    private int siguienteId = 1;

    @Override
    public Torneo guardar(Torneo torneo) {
        if (torneo.getId() == 0) {
            torneo.setId(siguienteId);
            siguienteId++;
            torneos.add(torneo);
        } else {
            for (int i = 0; i < torneos.size(); i++) {
                if (torneos.get(i).getId() == torneo.getId()) {
                    torneos.set(i, torneo);
                }
            }
        }
        return torneo;
    }

    @Override
    public List<Torneo> listarTodos() {
        return new ArrayList<>(torneos);
    }

    @Override
    public Optional<Torneo> buscarPorId(int id) {
        for (Torneo t : torneos) {
            if (t.getId() == id) {
                return Optional.of(t);
            }
        }
        return Optional.empty();
    }

    @Override
    public void eliminar(int id) {
        torneos.removeIf(t -> t.getId() == id);
    }
}