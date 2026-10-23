```mermaid
sequenceDiagram
    autonumber

    box rgb(235, 245, 255) Carril 1: Actores del Sistema
        actor Admin as 👤 Administrador
        actor Capitan as 🧢 Capitán
        actor Arbitro as 🟨 Árbitro
        actor Usuario as 👥 Usuario Autorizado
    end

    box rgb(240, 255, 244) Carril 2: Capa de Presentación (Vistas JavaFX)
        participant UI_Login as LoginViewController
        participant UI_Admin as AdminTorneoViewController
        participant UI_Cap as CapitanEquipoViewController
        participant UI_Arb as ArbitroActaViewController
        participant UI_Con as ConsultaGeneralViewController
    end

    box rgb(255, 249, 219) Carril 3: Capa de Negocio (Servicios Java 17)
        participant S_Auth as AutenticacionRolService
        participant S_Torneo as TorneoInscripcionService
        participant S_Equipo as EquipoJugadorService
        participant S_Cal as GeneradorCalendarioService
        participant S_Acta as RegistroActaPartidoService
        participant S_Pos as PosicionesYEstadisticasService
    end

    box rgb(243, 240, 255) Carril 4: Capa de Acceso a Datos (DAOs / JDBC)
        participant DAO as CapaPersistenciaDAO<br/>(Usuario/Torneo/Equipo/Partido/Acta/PosicionDAO)
    end

    box rgb(255, 227, 227) Carril 5: Base de Datos Separada (PostgreSQL :5432)
        participant DB as 🗄️ PostgreSQL Server<br/>(torneojav_db)
    end

    %% =====================================================================
    %% FASE 1: AUTENTICACIÓN Y CONTROL DE ACCESO POR ROLES (HU-01)
    %% =====================================================================
    rect rgb(245, 248, 255)
        Note over Admin, DB: FASE 1 — Autenticación y Control de Acceso por Roles (HU-01 / RF-01 a RF-03)
        Admin->>UI_Login: ingresarCredenciales(correo, password)
        UI_Login->>S_Auth: iniciarSesion(correo, password)
        S_Auth->>DAO: UsuarioDAO.buscarPorCorreo(correo)
        DAO->>DB: SELECT * FROM usuarios WHERE correo = ?
        DB-->>DAO: ResultSet (registro Usuario + RolUsuario)
        DAO-->>S_Auth: Optional<Usuario>
        S_Auth->>S_Auth: validarCredenciales() y verificarAccesoRol()
        S_Auth-->>UI_Login: UsuarioAutenticado (Rol = ADMINISTRADOR)
        UI_Login-->>Admin: Redirigir a AdminTorneoViewController
    end

    %% =====================================================================
    %% FASE 2: GESTIÓN DE TORNEOS Y CONFIGURACIÓN DE FORMATO (HU-02, HU-03)
    %% =====================================================================
    rect rgb(250, 250, 250)
        Note over Admin, DB: FASE 2 — Creación de Torneo y Configuración de Formato (HU-02, HU-03 / RF-04 a RF-10)
        Admin->>UI_Admin: crearTorneo(nombre, fechaInicio, FormatoTorneo.TODOS_CONTRA_TODOS)
        UI_Admin->>S_Torneo: crearTorneo(nuevoTorneo)
        S_Torneo->>S_Torneo: validarCamposObligatorios() y configurarFormato()
        S_Torneo->>DAO: TorneoDAO.guardar(nuevoTorneo)
        DAO->>DB: INSERT INTO torneos (nombre, fecha_inicio, formato, min_jug, max_jug, edad_min, edad_max) VALUES (...)
        DB-->>DAO: id_torneo generado
        DAO-->>S_Torneo: Torneo persistido
        S_Torneo-->>UI_Admin: Confirmación Torneo creado
    end

    %% =====================================================================
    %% FASE 3: REGISTRO DE EQUIPO, JUGADORES E INSCRIPCIÓN (HU-04, HU-05, HU-06)
    %% =====================================================================
    rect rgb(240, 253, 244)
        Note over Capitan, DB: FASE 3 — Registro de Equipo, Plantilla (5..15 jug, 16..45 años) y Aprobación (HU-04, HU-05, HU-06 / RF-11 a RF-19)
        Capitan->>UI_Cap: registrarEquipo(nombreEquipo, siglas)
        UI_Cap->>S_Equipo: registrarEquipo(capitanActual, equipo)
        S_Equipo->>DAO: EquipoDAO.guardar(equipoAsociadoACapitan)
        DAO->>DB: INSERT INTO equipos (nombre, siglas, id_capitan) VALUES (...)
        DB-->>DAO: id_equipo generado
        DAO-->>S_Equipo: Equipo registrado

        Capitan->>UI_Cap: agregarJugador(nombre, documento, fechaNacimiento, dorsal)
        UI_Cap->>S_Equipo: agregarJugadorAPlantilla(capitanActual, idEquipo, jugador)
        S_Equipo->>S_Equipo: validarPropiedadCapitan(), validarCupoMaximo(15), validarDocumentoUnico(), validarRangoEdad(16, 45)
        S_Equipo->>DAO: JugadorDAO.guardar(jugador)
        DAO->>DB: INSERT INTO jugadores (id_equipo, documento, nombre, fecha_nac, dorsal) VALUES (...)
        DB-->>DAO: Confirmación INSERT
        DAO-->>S_Equipo: Jugador persistido
        S_Equipo-->>UI_Cap: Plantilla actualizada (mínimo 5, máximo 15 jugadores)

        Admin->>UI_Admin: aprobarInscripcionEquipo(idInscripcion)
        UI_Admin->>S_Torneo: evaluarInscripcion(idInscripcion, true, "Cumple reglamento")
        S_Torneo->>DAO: InscripcionDAO.actualizarEstado(idInscripcion, EstadoInscripcion.APROBADA)
        DAO->>DB: UPDATE inscripciones SET estado = 'APROBADA' WHERE id_inscripcion = ?
        DB-->>DAO: 1 fila actualizada
        DAO-->>S_Torneo: Estado APROBADA persistido
    end

    %% =====================================================================
    %% FASE 4: GENERACIÓN AUTOMÁTICA DEL CALENDARIO (HU-07)
    %% =====================================================================
    rect rgb(255, 250, 235)
        Note over Admin, DB: FASE 4 — Generación Automática del Calendario según Formato (HU-07 / RF-20 a RF-23)
        Admin->>UI_Admin: solicitarGeneracionCalendario(idTorneo)
        UI_Admin->>S_Cal: generarCalendarioOficial(idTorneo)
        S_Cal->>DAO: TorneoDAO.buscarPorId(idTorneo) + InscripcionDAO.listarEquiposAprobados(idTorneo)
        DAO->>DB: SELECT formato FROM torneos; SELECT e.* FROM equipos e JOIN inscripciones i ON ... WHERE i.estado = 'APROBADA'
        DB-->>DAO: Formato + Lista de Equipos Aprobados
        DAO-->>S_Cal: Datos validados
        S_Cal->>S_Cal: ejecutar Strategy IGeneradorCalendario.generarFixture() sin duplicados
        S_Cal->>DAO: PartidoDAO.guardarLotePartidos(listaPartidos)
        DAO->>DB: INSERT INTO partidos (id_torneo, id_local, id_visitante, fecha_hora, jornada, estado) VALUES (...)
        DB-->>DAO: Lote de partidos almacenado
        DAO-->>S_Cal: List<Partido>
        S_Cal-->>UI_Admin: Calendario oficial generado y publicado
    end

    %% =====================================================================
    %% FASE 5: ARBITRAJE, CIERRE DE ACTA Y TABLA DE POSICIONES (HU-08, HU-09)
    %% =====================================================================
    rect rgb(248, 240, 255)
        Note over Arbitro, DB: FASE 5 — Registro de Resultados, Cierre de Acta y Recálculo de Tabla de Posiciones (HU-08, HU-09 / RF-24 a RF-30)
        Arbitro->>UI_Arb: registrarEventosYCerrarActa(idPartido, listaGolesYTarjetas)
        UI_Arb->>S_Acta: registrarResultadoYEventos(idPartido, listaEventos)
        S_Acta->>S_Acta: recalcularMarcadorDesdeEventos() y cerrarActaOficial() (Inmutable)
        S_Acta->>DAO: ActaEventoDAO.guardarActaConEventosTransaccional(actaCerrada)
        DAO->>DB: BEGIN; INSERT INTO actas_partido (...); INSERT INTO eventos_partido (...); UPDATE partidos SET estado='FINALIZADO_ACTA_CERRADA'; COMMIT;
        DB-->>DAO: Transacción confirmada
        S_Acta->>S_Pos: onActaCerrada(actaCerrada, partido) [Patrón Observer]
        S_Pos->>S_Pos: actualizar PJ, PG, PE, PP, PTS, GF, GC, DG y ordenar con CriterioPtsDgGf (PTS → DG → GF)
        S_Pos->>DAO: PosicionEstadisticaDAO.actualizarPosicionesPartido(idTorneo, posLocal, posVisita)
        DAO->>DB: UPDATE tabla_posiciones SET pj=?, pg=?, pe=?, pp=?, gf=?, gc=?, dg=?, pts=? WHERE id_torneo=? AND id_equipo=?
        DB-->>DAO: Posiciones actualizadas
        S_Pos-->>UI_Arb: Acta cerrada y tabla de posiciones recalculada
    end

    %% =====================================================================
    %% FASE 6: CONSULTA DE CALENDARIO, RESULTADOS Y ESTADÍSTICAS (HU-10)
    %% =====================================================================
    rect rgb(235, 250, 250)
        Note over Usuario, DB: FASE 6 — Consulta Consistente de Solo Lectura de Posiciones, Resultados y Estadísticas (HU-10 / RF-31 a RF-33)
        Usuario->>UI_Con: consultarInformacionTorneo(idTorneo)
        UI_Con->>S_Pos: consultarCalendarioYResultados(idTorneo) + consultarTablaPosiciones(idTorneo) + consultarEstadisticasTorneo(idTorneo)
        S_Pos->>DAO: PartidoDAO.listarPorTorneo(idTorneo) + PosicionEstadisticaDAO.obtenerEstadisticasConsolidadas(idTorneo)
        DAO->>DB: SELECT * FROM partidos; SELECT * FROM tabla_posiciones ORDER BY pts DESC, dg DESC, gf DESC; SELECT agregaciones_estadisticas...
        DB-->>DAO: Datos consistentes (Sin modificar estado en BD)
        DAO-->>S_Pos: Calendario + Tabla Ordenada + EstadisticaTorneo (Goles, Promedio, Más Goleador, Valla Menos Vencida, Tarjetas)
        S_Pos-->>UI_Con: DTOs de visualización
        UI_Con-->>Usuario: Desplegar vistas de Calendario, Resultados, Tabla de Posiciones y Estadísticas
    end
```
