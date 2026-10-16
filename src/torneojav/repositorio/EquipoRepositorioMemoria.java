package torneojav.repositorio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import torneojav.modelo.Equipo;

public class EquipoRepositorioMemoria implements EquipoRepositorio {

    private final List<Equipo> equipos = new ArrayList<>();
    private int siguienteId = 1;

    @Override
    public Equipo guardar(Equipo equipo) {
        if (equipo.getId() == 0) {
            equipo.setId(siguienteId);
            siguienteId++;
            equipos.add(equipo);
        } else {
            for (int i = 0; i < equipos.size(); i++) {
                if (equipos.get(i).getId() == equipo.getId()) {
                    equipos.set(i, equipo);
                }
            }
        }
        return equipo;
    }

    @Override
    public Optional<Equipo> buscarPorId(int id) {
        for (Equipo e : equipos) {
            if (e.getId() == id) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Equipo> buscarPorTorneo(int torneoId) {
        List<Equipo> resultado = new ArrayList<>();
        for (Equipo e : equipos) {
            if (e.getTorneoId() == torneoId) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    @Override
    public List<Equipo> buscarPorCapitan(int capitanId) {
        List<Equipo> resultado = new ArrayList<>();
        for (Equipo e : equipos) {
            if (e.getCapitanId() == capitanId) {
                resultado.add(e);
            }
        }
        return resultado;
    }
}
