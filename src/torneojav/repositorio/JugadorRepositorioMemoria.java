package torneojav.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.modelo.Jugador;

public class JugadorRepositorioMemoria implements JugadorRepositorio {

    private final List<Jugador> jugadores = new ArrayList<>();
    private int siguienteId = 1;

    @Override
    public Jugador guardar(Jugador jugador) {
        if (jugador.getId() == 0) {
            jugador.setId(siguienteId);
            siguienteId++;
            jugadores.add(jugador);
        } else {
            for (int i = 0; i < jugadores.size(); i++) {
                if (jugadores.get(i).getId() == jugador.getId()) {
                    jugadores.set(i, jugador);
                }
            }
        }
        return jugador;
    }

    @Override
    public Optional<Jugador> buscarPorId(int id) {
        for (Jugador j : jugadores) {
            if (j.getId() == id) {
                return Optional.of(j);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Jugador> buscarPorEquipo(int equipoId) {
        List<Jugador> resultado = new ArrayList<>();
        for (Jugador j : jugadores) {
            if (j.getEquipoId() == equipoId) {
                resultado.add(j);
            }
        }
        return resultado;
    }

    @Override
    public void eliminar(int id) {
        jugadores.removeIf(j -> j.getId() == id);
    }
}
