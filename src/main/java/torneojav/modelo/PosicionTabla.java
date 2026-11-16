package torneojav.modelo;

/**
 * Una fila de la tabla de posiciones (HU-09): cómo va un equipo en un torneo.
 */
public class PosicionTabla {

    public static final int PUNTOS_VICTORIA = 3;
    public static final int PUNTOS_EMPATE = 1;

    private final int torneoId;
    private final Equipo equipo;
    private int partidosJugados;
    private int partidosGanados;
    private int partidosEmpatados;
    private int partidosPerdidos;
    private int golesFavor;
    private int golesContra;
    private int puntos;

    /** Fila nueva, todo en cero. */
    public PosicionTabla(int torneoId, Equipo equipo) {
        this.torneoId = torneoId;
        this.equipo = equipo;
    }

    /** Fila leída de la base de datos. */
    public PosicionTabla(int torneoId, Equipo equipo, int pj, int pg, int pe, int pp,
                         int gf, int gc, int pts) {
        this(torneoId, equipo);
        this.partidosJugados = pj;
        this.partidosGanados = pg;
        this.partidosEmpatados = pe;
        this.partidosPerdidos = pp;
        this.golesFavor = gf;
        this.golesContra = gc;
        this.puntos = pts;
    }

    /** Suma un partido terminado desde el punto de vista de este equipo (CA-02, CA-03). */
    public void registrarResultado(int golesPro, int golesRival) {
        partidosJugados++;
        golesFavor += golesPro;
        golesContra += golesRival;
        if (golesPro > golesRival) {
            partidosGanados++;
            puntos += PUNTOS_VICTORIA;
        } else if (golesPro == golesRival) {
            partidosEmpatados++;
            puntos += PUNTOS_EMPATE;
        } else {
            partidosPerdidos++;
        }
    }

    /** CA-04: DG = GF - GC */
    public int calcularDiferenciaGoles() {
        return golesFavor - golesContra;
    }

    public int getTorneoId() {
        return torneoId;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public int getPartidosGanados() {
        return partidosGanados;
    }

    public int getPartidosEmpatados() {
        return partidosEmpatados;
    }

    public int getPartidosPerdidos() {
        return partidosPerdidos;
    }

    public int getGolesFavor() {
        return golesFavor;
    }

    public int getGolesContra() {
        return golesContra;
    }

    public int getPuntos() {
        return puntos;
    }

    @Override
    public String toString() {
        return equipo.getNombre() + " PJ=" + partidosJugados + " PG=" + partidosGanados
                + " PE=" + partidosEmpatados + " PP=" + partidosPerdidos + " GF=" + golesFavor
                + " GC=" + golesContra + " DG=" + calcularDiferenciaGoles() + " PTS=" + puntos;
    }
}
