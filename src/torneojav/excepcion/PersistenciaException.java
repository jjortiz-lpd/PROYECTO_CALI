package torneojav.excepcion;

/**
 * Error al leer o escribir en la base de datos.
 * Envuelve la SQLException para que los servicios no dependan de JDBC.
 */
public class PersistenciaException extends RuntimeException {
    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
