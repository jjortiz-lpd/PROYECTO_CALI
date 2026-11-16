package torneojav.repositorio;


import java.util.List;
import java.util.Optional;
import torneojav.modelo.Torneo;

public interface TorneoRepositorio {

    Torneo guardar(Torneo torneo);

    List<Torneo> listarTodos();

    Optional<Torneo> buscarPorId(int id);

    void eliminar (int id);
    
}
