package torneojav.servicio;

import torneojav.excepcion.DatosInvalidosException;
import torneojav.excepcion.FormatoNoDefinidoException;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Rol;
import torneojav.modelo.Torneo;
import torneojav.repositorio.PartidoRepositorio;
import torneojav.repositorio.TorneoRepositorio;

/**
 * HU-03: configuración del formato de competencia.
 */
public class FormatoService {

    private final TorneoRepositorio torneoRepositorio;
    private final PartidoRepositorio partidos;
    private final AutenticacionService auth;

    public FormatoService(TorneoRepositorio torneoRepositorio, PartidoRepositorio partidos,
                          AutenticacionService auth) {
        this.torneoRepositorio = torneoRepositorio;
        this.partidos = partidos;
        this.auth = auth;
    }

    // CA-01
    public FormatoTorneo[] opcionesDisponibles() {
        return FormatoTorneo.values();
    }

    // CA-02, CA-03, CA-04
    public Torneo asignarFormato(int torneoId, FormatoTorneo formato) {
        auth.verificarRol(Rol.ADMINISTRADOR);
        if (formato == null) {
            throw new FormatoNoDefinidoException("Tiene que seleccionar un formato");
        }

        Torneo torneo = torneoRepositorio.buscarPorId(torneoId)
                .orElseThrow(() -> new DatosInvalidosException("No existe un torneo con id " + torneoId));

        // CA-05: el calendario usa las reglas del formato. Si ya se generó con un formato,
        // cambiarlo dejaría partidos armados con las reglas equivocadas.
        if (formato != torneo.getFormato() && !partidos.buscarPorTorneo(torneoId).isEmpty()) {
            throw new DatosInvalidosException("No se puede cambiar el formato: el calendario ya fue generado como "
                    + torneo.getFormato());
        }

        torneo.setFormato(formato);
        return torneoRepositorio.guardar(torneo);
    }
}
