package torneojav.modelo;

import java.time.LocalDate;

public class Jugador {
    private int id;
    private String nombreCompleto;
    private String documento;
    private LocalDate fechaNacimiento;
    private int numeroCamiseta;
    private int equipoId;

    public Jugador(int id, String nombreCompleto, String documento, LocalDate fechaNacimiento,
                   int numeroCamiseta, int equipoId) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.documento = documento;
        this.fechaNacimiento = fechaNacimiento;
        this.numeroCamiseta = numeroCamiseta;
        this.equipoId = equipoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getNumeroCamiseta() {
        return numeroCamiseta;
    }

    public void setNumeroCamiseta(int numeroCamiseta) {
        this.numeroCamiseta = numeroCamiseta;
    }

    public int getEquipoId() {
        return equipoId;
    }

    @Override
    public String toString() {
        return "Jugador{id=" + id + ", nombre='" + nombreCompleto + "', documento=" + documento
                + ", nacimiento=" + fechaNacimiento + ", camiseta=" + numeroCamiseta
                + ", equipoId=" + equipoId + "}";
    }
}
