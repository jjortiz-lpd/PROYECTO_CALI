package torneojav.modelo;

public class EventoPartido {
    private int id;
    private int partidoId;
    private TipoEvento tipo;
    private int minuto;
    private int jugadorId;
    private int equipoId;

    public EventoPartido(int id, int partidoId, TipoEvento tipo, int minuto, int jugadorId, int equipoId) {
        this.id = id;
        this.partidoId = partidoId;
        this.tipo = tipo;
        this.minuto = minuto;
        this.jugadorId = jugadorId;
        this.equipoId = equipoId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartidoId() {
        return partidoId;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public int getMinuto() {
        return minuto;
    }

    public int getJugadorId() {
        return jugadorId;
    }

    public int getEquipoId() {
        return equipoId;
    }

    @Override
    public String toString() {
        return "Evento{min " + minuto + ", " + tipo + ", jugadorId=" + jugadorId
                + ", equipoId=" + equipoId + "}";
    }
}
