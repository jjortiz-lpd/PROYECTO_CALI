package torneojav.modelo;

/**
 * Formatos de competencia que puede tener un torneo (HU-03).
 */
public enum FormatoTorneo {
    TODOS_CONTRA_TODOS("Todos contra todos"),
    ELIMINACION_DIRECTA("Eliminación directa");

    private final String nombreVisible;

    FormatoTorneo(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }

    @Override
    public String toString() {
        return nombreVisible;
    }
}
