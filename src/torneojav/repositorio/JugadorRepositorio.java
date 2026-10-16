package torneojav.repositorio;

import java.util.List;
import java.util.Optional;
import torneojav.modelo.Jugador;

public interface JugadorRepositorio {
    Jugador guardar(Jugador jugador);

    Optional<Jugador> buscarPorId(int id);

    List<Jugador> buscarPorEquipo(int equipoId);

    void eliminar(int id);
}
