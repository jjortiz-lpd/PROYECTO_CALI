package torneojav.repositorio;

import java.util.List;
import torneojav.modelo.EventoPartido;

public interface EventoRepositorio {
    EventoPartido guardar(EventoPartido evento);

    List<EventoPartido> buscarPorPartido(int partidoId);
}
