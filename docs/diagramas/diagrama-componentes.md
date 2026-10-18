# Diagrama de Componentes — TorneoJAV

## 1. Descripción de la Arquitectura Modular
El diagrama de componentes representa la arquitectura modular de **TorneoJAV** dividida en cuatro capas desacopladas mediante interfaces provistas (contratos que un módulo ofrece) e interfaces requeridas (contratos que un módulo consume):
1. **Capa de Presentación (`«component» UI JavaFX`):** Vistas gráficas segmentadas por rol.
2. **Capa de Lógica de Negocio (6 componentes de dominio):** Módulos independientes para Autenticación, Torneos, Equipos/Jugadores, Calendario, Partidos/Actas y Posiciones/Estadísticas.
3. **Capa de Acceso a Datos (`«component» CapaPersistenciaDAO`):** Implementación del patrón DAO sobre JDBC.
4. **Servicios Externos y Base de Datos:** Motor relacional **PostgreSQL** diferenciado visualmente de los componentes internos de la aplicación.

---

## 2. Representación Gráfica (UML / Mermaid)

```mermaid
flowchart TB
    %% Capa de Presentación
    subgraph Presentacion ["Capa de Presentación (JavaFX)"]
        UI["«component»\nClienteGUI_JavaFX\n(Vistas Admin, Capitán, Árbitro)"]
    end

    %% Interfaces de Servicio (Contratos entre UI y Negocio)
    I_Auth(("○ IAuthService"))
    I_Torneo(("○ ITorneoService"))
    I_Equipo(("○ IEquipoJugadorService"))
    I_Cal(("○ ICalendarioService"))
    I_Acta(("○ IActaPartidoService"))
    I_Pos(("○ IPosicionesConsultaService"))

    %% Capa de Negocio
    subgraph Negocio ["Capa de Lógica de Negocio (Java 17)"]
        C_Auth["«component»\nModuloAutenticacion\n(HU-01)"]
        C_Torneo["«component»\nModuloTorneosFormatos\n(HU-02, HU-03)"]
        C_Equipo["«component»\nModuloEquiposJugadores\n(HU-04, HU-05, HU-06)"]
        C_Cal["«component»\nModuloGeneradorCalendario\n(HU-07)"]
        C_Acta["«component»\nModuloPartidosActas\n(HU-08)"]
        C_Pos["«component»\nModuloPosicionesEstadisticas\n(HU-09, HU-10)"]
    end

    %% Interfaz interna entre Actas y Posiciones
    I_EventoCierre(("○ IActualizadorTabla"))

    %% Interfaz de Persistencia DAO
    I_DAO(("○ IRepositorioDAO (CRUD)"))

    %% Capa de Acceso a Datos
    subgraph Datos ["Capa de Acceso a Datos (DAO / JDBC)"]
        C_DAO["«component»\nCapaPersistenciaDAO\n(UsuarioDAO, TorneoDAO, EquipoDAO,\nPartidoDAO, ActaDAO, PosicionDAO)"]
        C_JDBC["«component»\nGestorConexionJDBC\n(Connection Pool / Driver)"]
    end

    %% Sistema Externo (Base de Datos)
    subgraph Externo ["Sistema Externo de Almacenamiento"]
        DB[("«database»\nPostgreSQL Server\n(Base de Datos TorneoJAV)")]
    end

    %% Conexiones UI -> Interfaces Requeridas -> Componentes Provistos
    UI -->|requiere ◖| I_Auth ---|provee| C_Auth
    UI -->|requiere ◖| I_Torneo ---|provee| C_Torneo
    UI -->|requiere ◖| I_Equipo ---|provee| C_Equipo
    UI -->|requiere ◖| I_Cal ---|provee| C_Cal
    UI -->|requiere ◖| I_Acta ---|provee| C_Acta
    UI -->|requiere ◖| I_Pos ---|provee| C_Pos

    %% Comunicación interna entre componentes de negocio
    C_Cal -.->|requiere formato ◖| I_Torneo
    C_Cal -.->|requiere equipos aprobados ◖| I_Equipo
    C_Acta -->|al cerrar acta invoca ◖| I_EventoCierre ---|provee| C_Pos

    %% Conexiones Negocio -> Capa DAO
    C_Auth -->|requiere ◖| I_DAO
    C_Torneo -->|requiere ◖| I_DAO
    C_Equipo -->|requiere ◖| I_DAO
    C_Cal -->|requiere ◖| I_DAO
    C_Acta -->|requiere ◖| I_DAO
    C_Pos -->|requiere ◖| I_DAO

    I_DAO ---|provee| C_DAO
    C_DAO --> C_JDBC
    C_JDBC ==>|Protocolo JDBC / TCP :5432| DB

    style Externo fill:#f5f5f5,stroke:#333,stroke-dasharray: 5 5
    style DB fill:#dceefb,stroke:#1b6ec2,stroke-width:2px
```

---

## 3. Catálogo de Componentes e Interfaces

| Componente (`«component»`) | Interfaces Provistas (`○`) | Interfaces Requeridas (`◖`) | Responsable en el Equipo |
|---|---|---|---|
| **`ClienteGUI_JavaFX`** | Vistas de interacción con actores | `IAuthService`, `ITorneoService`, `IEquipoJugadorService`, `ICalendarioService`, `IActaPartidoService`, `IPosicionesConsultaService` | Juan Pablo Franco |
| **`ModuloAutenticacion`** | `IAuthService` | `IRepositorioDAO` (`UsuarioDAO`) | Esteban Hernández |
| **`ModuloTorneosFormatos`** | `ITorneoService` | `IRepositorioDAO` (`TorneoDAO`) | Natalia Valencia |
| **`ModuloEquiposJugadores`** | `IEquipoJugadorService` | `IRepositorioDAO` (`EquipoDAO`, `JugadorDAO`, `InscripcionDAO`) | Esteban Hernández |
| **`ModuloGeneradorCalendario`** | `ICalendarioService` | `ITorneoService`, `IEquipoJugadorService`, `IRepositorioDAO` (`PartidoDAO`) | Natalia Valencia |
| **`ModuloPartidosActas`** | `IActaPartidoService` | `IActualizadorTabla`, `IRepositorioDAO` (`PartidoDAO`, `ActaDAO`) | Juan Pablo Franco |
| **`ModuloPosicionesEstadisticas`** | `IPosicionesConsultaService`, `IActualizadorTabla` | `IRepositorioDAO` (`PosicionDAO`, `EstadisticaDAO`) | Juan Pablo Franco |
| **`CapaPersistenciaDAO`** | `IRepositorioDAO` | `GestorConexionJDBC` (Driver PostgreSQL) | Juan José Ortiz |
