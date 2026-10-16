package torneojav.servicio;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import torneojav.excepcion.AccesoDenegadoException;
import torneojav.excepcion.DatosInvalidosException;
import torneojav.modelo.Equipo;
import torneojav.modelo.Jugador;
import torneojav.modelo.Rol;
import torneojav.repositorio.EquipoRepositorio;
import torneojav.repositorio.JugadorRepositorio;

public class JugadorService {

    public static final int MAX_JUGADORES = 20;
    public static final int MIN_JUGADORES = 7;
    public static final int EDAD_MIN = 16;
    public static final int EDAD_MAX = 45;

    private final JugadorRepositorio jugadores;
    private final EquipoRepositorio equipos;
    private final AutenticacionService auth;

    public JugadorService(JugadorRepositorio jugadores, EquipoRepositorio equipos, AutenticacionService auth) {
        this.jugadores = jugadores;
        this.equipos = equipos;
        this.auth = auth;
    }

    public Jugador agregar(int equipoId, String nombreCompleto, String documento,
                           LocalDate fechaNacimiento, int numeroCamiseta) {
        auth.verificarRol(Rol.CAPITAN);
        verificarEquipoPropio(equipoId);

        if (jugadores.buscarPorEquipo(equipoId).size() >= MAX_JUGADORES) {
            throw new DatosInvalidosException("El equipo ya tiene el máximo de " + MAX_JUGADORES + " jugadores");
        }
        validarDatos(nombreCompleto, documento, fechaNacimiento, numeroCamiseta);
        validarDocumentoNoRepetido(equipoId, documento, 0);

        Jugador nuevo = new Jugador(0, nombreCompleto.trim(), documento.trim(), fechaNacimiento,
                numeroCamiseta, equipoId);
        return jugadores.guardar(nuevo);
    }

    public Jugador editar(int jugadorId, String nombreCompleto, String documento,
                          LocalDate fechaNacimiento, int numeroCamiseta) {
        auth.verificarRol(Rol.CAPITAN);
        Jugador jugador = buscarJugador(jugadorId);
        verificarEquipoPropio(jugador.getEquipoId());

        validarDatos(nombreCompleto, documento, fechaNacimiento, numeroCamiseta);
        validarDocumentoNoRepetido(jugador.getEquipoId(), documento, jugadorId);

        jugador.setNombreCompleto(nombreCompleto.trim());
        jugador.setDocumento(documento.trim());
        jugador.setFechaNacimiento(fechaNacimiento);
        jugador.setNumeroCamiseta(numeroCamiseta);
        return jugadores.guardar(jugador);
    }

    public void eliminar(int jugadorId) {
        auth.verificarRol(Rol.CAPITAN);
        Jugador jugador = buscarJugador(jugadorId);
        verificarEquipoPropio(jugador.getEquipoId());

        if (jugadores.buscarPorEquipo(jugador.getEquipoId()).size() <= MIN_JUGADORES) {
            throw new DatosInvalidosException("El equipo debe mantener al menos " + MIN_JUGADORES + " jugadores");
        }
        jugadores.eliminar(jugadorId);
    }

    public List<Jugador> listarPorEquipo(int equipoId) {
        auth.verificarRol(Rol.CAPITAN);
        verificarEquipoPropio(equipoId);
        return jugadores.buscarPorEquipo(equipoId);
    }

    private Jugador buscarJugador(int jugadorId) {
        Optional<Jugador> encontrado = jugadores.buscarPorId(jugadorId);
        if (encontrado.isEmpty()) {
            throw new DatosInvalidosException("No existe un jugador con id " + jugadorId);
        }
        return encontrado.get();
    }

    private void verificarEquipoPropio(int equipoId) {
        Optional<Equipo> encontrado = equipos.buscarPorId(equipoId);
        if (encontrado.isEmpty()) {
            throw new DatosInvalidosException("No existe un equipo con id " + equipoId);
        }
        if (encontrado.get().getCapitanId() != auth.getUsuarioActual().getId()) {
            throw new AccesoDenegadoException("Solo puede gestionar jugadores de su propio equipo");
        }
    }

    private void validarDatos(String nombreCompleto, String documento, LocalDate fechaNacimiento, int numeroCamiseta) {
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new DatosInvalidosException("El nombre completo es obligatorio");
        }
        if (documento == null || documento.isBlank()) {
            throw new DatosInvalidosException("El número de documento es obligatorio");
        }
        if (fechaNacimiento == null) {
            throw new DatosInvalidosException("La fecha de nacimiento es obligatoria");
        }
        if (numeroCamiseta <= 0) {
            throw new DatosInvalidosException("El número de camiseta debe ser mayor que 0");
        }
        int edad = Period.between(fechaNacimiento, LocalDate.now()).getYears();
        if (edad < EDAD_MIN || edad > EDAD_MAX) {
            throw new DatosInvalidosException("La edad del jugador (" + edad + ") debe estar entre "
                    + EDAD_MIN + " y " + EDAD_MAX + " años");
        }
    }

    private void validarDocumentoNoRepetido(int equipoId, String documento, int idIgnorado) {
        for (Jugador j : jugadores.buscarPorEquipo(equipoId)) {
            if (j.getId() != idIgnorado && j.getDocumento().equals(documento.trim())) {
                throw new DatosInvalidosException("Ya hay un jugador con el documento " + documento.trim() + " en el equipo");
            }
        }
    }
}
