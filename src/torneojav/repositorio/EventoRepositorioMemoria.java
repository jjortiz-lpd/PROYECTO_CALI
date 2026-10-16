package torneojav.repositorio;

import java.util.ArrayList;
import java.util.List;
import torneojav.modelo.EventoPartido;

public class EventoRepositorioMemoria implements EventoRepositorio {

    private final List<EventoPartido> eventos = new ArrayList<>();
    private int siguienteId = 1;

    @Override
    public EventoPartido guardar(EventoPartido evento) {
        evento.setId(siguienteId);
        siguienteId++;
        eventos.add(evento);
        return evento;
    }

    @Override
    public List<EventoPartido> buscarPorPartido(int partidoId) {
        List<EventoPartido> resultado = new ArrayList<>();
        for (EventoPartido e : eventos) {
            if (e.getPartidoId() == partidoId) {
                resultado.add(e);
            }
        }
        return resultado;
    }
}
