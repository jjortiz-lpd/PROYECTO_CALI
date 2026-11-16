package torneojav.excepcion;

/**
 * Error al leer o escribir en la base de datos.
 * Envuelve la SQLException para que los servicios no dependan de JDBC.
 * El mensaje incluye lo que respondió PostgreSQL, para saber qué falló.
 */
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String mensaje, Throwable causa) {
        super(causa == null ? mensaje : mensaje + " -> " + causa.getMessage(), causa);
    }
}
