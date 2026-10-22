package torneojav.repositorio;

import java.util.List;
import java.util.Optional;
import torneojav.modelo.Equipo;

public interface EquipoRepositorio {
    Equipo guardar(Equipo equipo);

    Optional<Equipo> buscarPorId(int id);

    List<Equipo> buscarPorTorneo(int torneoId);

    List<Equipo> buscarPorCapitan(int capitanId);
}
