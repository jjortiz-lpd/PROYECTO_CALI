package torneojav.modelo;

import java.time.LocalDate;

/**
 * Representa un torneo del sistema TorneoJAV (HU-02, HU-03).
 */
public class Torneo {

    private int id;
    private String nombre;
    private LocalDate fechaInicio;
    private FormatoTorneo formato; // puede ser null si aún no se ha definido

    public Torneo() {
    }

    public Torneo(int id, String nombre, LocalDate fechaInicio, FormatoTorneo formato) {
        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.formato = formato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public FormatoTorneo getFormato() {
        return formato;
    }

    public void setFormato(FormatoTorneo formato) {
        this.formato = formato;
    }

    /** Sirve para la validación de HU-03 CA-06: sin formato no hay calendario. */
    public boolean tieneFormato() {
        return formato != null;
    }

    @Override
    public String toString() {
        return "Torneo{id=" + id
                + ", nombre='" + nombre + '\''
                + ", fechaInicio=" + fechaInicio
                + ", formato=" + (formato == null ? "sin definir" : formato)
                + '}';
    }
}
