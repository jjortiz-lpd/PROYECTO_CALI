```mermaid
flowchart TB
    %% =========================================================
    %% PATRÓN 1: FACTORY METHOD (CREACIÓN POLIMÓRFICA)
    %% =========================================================
    subgraph PatronFactory ["🏭 1. Patrón Factory Method (Creacional)"]
        direction TB
        F_User["«factory»\nUsuarioFactory\n+crearSegunRol(rol: RolUsuario): Usuario"]
        F_Cal["«factory»\nGeneradorCalendarioFactory\n+obtenerEstrategia(formato: FormatoTorneo): IGeneradorCalendario"]
        Sub_Admin["Administrador"]
        Sub_Cap["Capitan"]
        Sub_Arb["Arbitro"]

        F_User -->|instancia| Sub_Admin
        F_User -->|instancia| Sub_Cap
        F_User -->|instancia| Sub_Arb
    end

    %% =========================================================
    %% PATRÓN 2: STRATEGY (ALGORITMOS INTERCAMBIABLES HU-03, 07, 09)
    %% =========================================================
    subgraph PatronStrategy ["♟️ 2. Patrón Strategy (Comportamiento)"]
        direction TB
        Ctx_Cal["«context»\nGeneradorCalendarioService"]
        Int_Cal["«interface»\nIGeneradorCalendario\n+generarFixture(torneo, equipos)"]
        Str_Todos["«strategy»\nGeneradorTodosContraTodos"]
        Str_Elim["«strategy»\nGeneradorEliminacionDirecta"]

        Ctx_Pos["«context»\nPosicionesYEstadisticasService"]
        Int_Des["«interface»\nICriterioDesempate\n+ordenarTabla(posiciones)"]
        Str_Pts["«strategy»\nCriterioPtsDgGf\n(1.º PTS → 2.º DG → 3.º GF)"]

        Ctx_Cal -->|delega en| Int_Cal
        Int_Cal <|.. Str_Todos
        Int_Cal <|.. Str_Elim

        Ctx_Pos -->|delega en| Int_Des
        Int_Des <|.. Str_Pts
    end

    %% =========================================================
    %% PATRÓN 3: OBSERVER (EVENTO DE CIERRE DE ACTA HU-08 -> HU-09)
    %% =========================================================
    subgraph PatronObserver ["🔔 3. Patrón Observer (Comportamiento)"]
        direction TB
        Pub_Acta["«publisher»\nRegistroActaPartidoService\n+cerrarActaYNotificar(idActa)"]
        Int_Obs["«interface»\nIObservadorCierreActa\n+onActaCerrada(acta, partido)"]
        Sub_Pos["«subscriber»\nPosicionesYEstadisticasService\n+onActaCerrada(acta, partido)"]

        Pub_Acta -->|notifica al cerrar acta| Int_Obs
        Int_Obs <|.. Sub_Pos
    end

    %% =========================================================
    %% PATRÓN 4 Y 5: DAO + SINGLETON (PERSISTENCIA HACIA BD SEPARADA)
    %% =========================================================
    subgraph PatronDAOSingleton ["🔌 4 y 5. Patrones DAO + Singleton (Persistencia)"]
        direction TB
        Int_DAO["«interface»\nIGenericDAO<T, ID>\n+guardar() / +actualizar() / +eliminarPorId() / +buscarPorId()"]
        Impl_DAOs["«concrete DAOs»\nUsuarioDAOImpl | TorneoInscripcionDAOImpl\nEquipoJugadorDAOImpl | PartidoCalendarioDAOImpl\nActaEventoPartidoDAOImpl | PosicionEstadisticaDAOImpl"]
        Sing_JDBC["«singleton»\nConexionPostgreSQLSingleton\n-instancia: ConexionPostgreSQLSingleton\n+getInstancia(): ConexionPostgreSQLSingleton\n+obtenerConexion(): Connection"]

        Int_DAO <|.. Impl_DAOs
        Impl_DAOs -->|obtienen Connection de| Sing_JDBC
    end

    %% =========================================================
    %% BASE DE DATOS SEPARADA
    %% =========================================================
    subgraph BaseDatosExterna ["🗄️ Base de Datos Relacional Separada"]
        PG_Ext[("PostgreSQL 16 Server\n(torneojav_db — Puerto 5432)")]
    end

    F_Cal -.->|provee estrategia a| Ctx_Cal
    Ctx_Cal --> Int_DAO
    Sub_Pos --> Int_DAO
    Sing_JDBC ===>|"JDBC / TCP :5432"| PG_Ext

    style PatronFactory fill:#eef6ff,stroke:#1b6ec2,stroke-width:2px
    style PatronStrategy fill:#fff9db,stroke:#e67700,stroke-width:2px
    style PatronObserver fill:#ebfbee,stroke:#2b8a3e,stroke-width:2px
    style PatronDAOSingleton fill:#f3f0ff,stroke:#5f3dc4,stroke-width:2px
    style BaseDatosExterna fill:#ffe3e3,stroke:#c92a2a,stroke-width:3px,stroke-dasharray: 5 5
```
