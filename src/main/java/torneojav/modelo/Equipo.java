package torneojav.modelo;

public class Equipo {
    private int id;
    private String nombre;
    private int capitanId;
    private int torneoId;
    private EstadoInscripcion estado;

    public Equipo(int id, String nombre, int capitanId, int torneoId) {
        this.id = id;
        this.nombre = nombre;
        this.capitanId = capitanId;
        this.torneoId = torneoId;
        this.estado = EstadoInscripcion.PENDIENTE;
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

    public int getCapitanId() {
        return capitanId;
    }

    public int getTorneoId() {
        return torneoId;
    }

    public EstadoInscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoInscripcion estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Equipo{id=" + id + ", nombre='" + nombre + "', capitanId=" + capitanId
                + ", torneoId=" + torneoId + ", estado=" + estado + "}";
    }
}
