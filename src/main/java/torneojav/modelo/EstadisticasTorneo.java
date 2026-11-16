package torneojav.modelo;

import java.util.List;
import java.util.Map;

/**
 * Las 4 estadísticas generales que pide HU-10 CA-05.
 * Solo cuentan partidos con el acta cerrada, igual que la tabla de posiciones.
 */
public class EstadisticasTorneo {

    // (1) resumen
    private final int partidosJugados;
    private final int partidosProgramados;
    private final int totalGoles;
    private final double promedioGoles;

    // (2) y (3): pueden ser varios si están empatados
    private final List<PosicionTabla> masGoleadores;
    private final List<PosicionTabla> vallasMenosVencidas;

    // (4) balance disciplinario
    private final int tarjetasAmarillas;
    private final int tarjetasRojas;
    /** nombre del equipo -> {amarillas, rojas} */
    private final Map<String, int[]> tarjetasPorEquipo;

    public EstadisticasTorneo(int partidosJugados, int partidosProgramados, int totalGoles,
                              List<PosicionTabla> masGoleadores, List<PosicionTabla> vallasMenosVencidas,
                              int tarjetasAmarillas, int tarjetasRojas, Map<String, int[]> tarjetasPorEquipo) {
        this.partidosJugados = partidosJugados;
        this.partidosProgramados = partidosProgramados;
        this.totalGoles = totalGoles;
        this.promedioGoles = partidosJugados == 0 ? 0 : (double) totalGoles / partidosJugados;
        this.masGoleadores = masGoleadores;
        this.vallasMenosVencidas = vallasMenosVencidas;
        this.tarjetasAmarillas = tarjetasAmarillas;
        this.tarjetasRojas = tarjetasRojas;
        this.tarjetasPorEquipo = tarjetasPorEquipo;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public int getPartidosProgramados() {
        return partidosProgramados;
    }

    public int getTotalGoles() {
        return totalGoles;
    }

    public double getPromedioGoles() {
        return promedioGoles;
    }

    public List<PosicionTabla> getMasGoleadores() {
        return masGoleadores;
    }

    public List<PosicionTabla> getVallasMenosVencidas() {
        return vallasMenosVencidas;
    }

    public int getTarjetasAmarillas() {
        return tarjetasAmarillas;
    }

    public int getTarjetasRojas() {
        return tarjetasRojas;
    }

    public Map<String, int[]> getTarjetasPorEquipo() {
        return tarjetasPorEquipo;
    }
}
