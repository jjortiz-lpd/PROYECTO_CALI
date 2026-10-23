package torneojav.persistencia;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import torneojav.excepcion.PersistenciaException;

/**
 * Entrega conexiones a PostgreSQL.
 * Lee los datos de conexión del archivo db.properties, que va en la raíz del proyecto
 * (junto a las carpetas src, db y lib).
 *
 * El archivo se busca primero en la carpeta desde donde se ejecuta el programa y,
 * si no está ahí, subiendo desde la carpeta donde quedaron los .class compilados.
 * Así funciona aunque la terminal esté parada en otra carpeta.
 */
public class GestorConexionJDBC {

    private static final String ARCHIVO = "db.properties";

    private static String url = "jdbc:postgresql://localhost:5432/torneojav";
    private static String usuario = "postgres";
    private static String contrasena = "postgres";
    private static boolean configuracionLeida = false;

    private GestorConexionJDBC() {
    }

    /** Abre una conexión nueva. Quien la pide la cierra (try-with-resources). */
    public static Connection obtenerConexion() {
        leerConfiguracion();
        try {
            return DriverManager.getConnection(url, usuario, contrasena);
        } catch (SQLException e) {
            throw new PersistenciaException("No se pudo conectar a la base de datos (" + url + "): "
                    + e.getMessage(), e);
        }
    }

    /** Sirve para verificar al arrancar el programa que la base está disponible. */
    public static void probarConexion() {
        try (Connection c = obtenerConexion()) {
            if (!c.isValid(5)) {
                throw new PersistenciaException("La conexión a la base de datos no respondió", null);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error cerrando la conexión de prueba", e);
        }
    }

    private static void leerConfiguracion() {
        if (configuracionLeida) {
            return;
        }
        configuracionLeida = true;

        File archivo = buscarArchivo();
        if (archivo == null) {
            System.out.println("Aviso: no se encontró " + ARCHIVO + ", se usan los valores por defecto.");
            System.out.println("       Se buscó en: " + new File(ARCHIVO).getAbsolutePath());
            return;
        }

        Properties p = new Properties();
        try (InputStream in = new FileInputStream(archivo)) {
            p.load(in);
            url = p.getProperty("db.url", url).trim();
            usuario = p.getProperty("db.usuario", usuario).trim();
            contrasena = p.getProperty("db.contrasena", contrasena).trim();
        } catch (IOException e) {
            System.out.println("Aviso: no se pudo leer " + archivo.getAbsolutePath() + ": " + e.getMessage());
        }
    }

    /**
     * 1) Busca db.properties en la carpeta de ejecución.
     * 2) Si no está, parte de la carpeta de los .class (por ejemplo PROYECTO_CALI/bin)
     *    y va subiendo carpeta por carpeta hasta encontrarlo.
     */
    private static File buscarArchivo() {
        File enCarpetaActual = new File(ARCHIVO);
        if (enCarpetaActual.isFile()) {
            return enCarpetaActual;
        }
        try {
            File carpeta = new File(GestorConexionJDBC.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
            while (carpeta != null) {
                File candidato = new File(carpeta, ARCHIVO);
                if (candidato.isFile()) {
                    return candidato;
                }
                carpeta = carpeta.getParentFile();
            }
        } catch (URISyntaxException | SecurityException | NullPointerException e) {
            // si no se puede averiguar la carpeta de los .class, se sigue con los valores por defecto
        }
        return null;
    }
}