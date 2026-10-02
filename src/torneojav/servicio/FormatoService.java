package torneojav.servicio;

import torneojav.excepcion.DatosInvalidosException;
import torneojav.excepcion.FormatoNoDefinidoException;
import torneojav.modelo.FormatoTorneo;
import torneojav.modelo.Torneo;
import torneojav.repositorio.TorneoRepositorio;

public class FormatoService {
    private final TorneoRepositorio torneoRepositorio;

    public FormatoService(TorneoRepositorio torneoRepositorio) {
        this.torneoRepositorio = torneoRepositorio;
    }
        //CA-01
    public FormatoTorneo[] opcionesDisponibles() {
        return FormatoTorneo.values();
    }

    //CA-02 CA-03 CA-04 
    public Torneo asignarFormato(int torneoID, FormatoTorneo formato) {
        
            if (formato == null) {
                throw new FormatoNoDefinidoException ( "Tiene que seleccionar un formato");
            }

            Torneo torneo = torneoRepositorio.buscarPorId(torneoID).orElseThrow(() -> new DatosInvalidosException("No existe un torneo con id " + torneoID));
            torneo.setFormato(formato);
            return torneoRepositorio.guardar(torneo);
            


    }

}
