package torneojav.repositorio;

import java.util.List;
import java.util.Optional;
import torneojav.modelo.Partido;

public interface PartidoRepositorio {
    Partido guardar(Partido partido);

    Optional<Partido> buscarPorId(int id);

    List<Partido> buscarPorTorneo(int torneoId);
}
