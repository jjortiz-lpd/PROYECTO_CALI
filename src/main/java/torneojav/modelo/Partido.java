package torneojav.modelo;

import java.time.LocalDate;

public class Partido {
    private int id;
    private int torneoId;
    private Equipo local;
    private Equipo visitante;
    private LocalDate fecha;
    private int ronda;
    private EstadoPartido estado;
    private int golesLocal;
    private int golesVisitante;
    private boolean actaCerrada;

    public Partido(int id, int torneoId, Equipo local, Equipo visitante, LocalDate fecha, int ronda) {
        this.id = id;
        this.torneoId = torneoId;
        this.local = local;
        this.visitante = visitante;
        this.fecha = fecha;
        this.ronda = ronda;
        this.estado = EstadoPartido.PROGRAMADO;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTorneoId() {
        return torneoId;
    }

    public Equipo getLocal() {
        return local;
    }

    public Equipo getVisitante() {
        return visitante;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getRonda() {
        return ronda;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartido estado) {
        this.estado = estado;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public boolean isActaCerrada() {
        return actaCerrada;
    }

    public void setActaCerrada(boolean actaCerrada) {
        this.actaCerrada = actaCerrada;
    }

    @Override
    public String toString() {
        return "Partido{id=" + id + ", " + local.getNombre() + " " + golesLocal + " - "
                + golesVisitante + " " + visitante.getNombre() + ", fecha=" + fecha
                + ", ronda=" + ronda + ", estado=" + estado + "}";
    }
}
