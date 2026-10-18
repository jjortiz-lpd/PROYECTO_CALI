# Diagrama de Análisis (ECB — Entity, Control, Boundary)

## 1. Descripción del Modelo ECB
El patrón **ECB (Entity-Control-Boundary)** estructura el análisis del sistema separando las responsabilidades en tres estereotipos UML:
- **`«boundary»` (Límite):** Interfaces gráficas (vistas y formularios en JavaFX) mediante las cuales los actores (*Administrador*, *Capitán*, *Árbitro*) interactúan con **TorneoJAV**.
- **`«control»` (Control):** Clases gestoras que orquestan la lógica de negocio, validaciones de reglamento y coordinación entre vistas y entidades.
- **`«entity»` (Entidad):** Objetos del dominio persistidos en PostgreSQL que almacenan el estado del torneo.

Se modelan las **Historias de Usuario críticas** del núcleo funcional del sistema:
1. **HU-07:** Generación automática del calendario (8 SP).
2. **HU-08:** Registro de resultados y eventos del partido (8 SP).
3. **HU-09:** Actualización automática de tabla de posiciones (5 SP, encadenada al cierre de acta de HU-08).
4. **HU-05:** Gestión de jugadores de un equipo (8 SP, validación de límites de plantilla y edad).

---

## 2. Representación Gráfica (UML / Mermaid)

```mermaid
flowchart LR
    %% Actores
    Admin(["👤 Administrador"])
    Arbitro(["👤 Árbitro"])
    Capitan(["👤 Capitán"])

    %% Boundaries (Límites)
    subgraph Boundaries ["«boundary» Interfaces de Usuario (JavaFX)"]
        B_Cal["«boundary»\nCalendarioFormView"]
        B_Acta["«boundary»\nActaPartidoFormView"]
        B_Tabla["«boundary»\nTablaPosicionesView"]
        B_Jug["«boundary»\nPlantillaJugadoresFormView"]
    end

    %% Controls (Controladores de Negocio)
    subgraph Controls ["«control» Gestores de Lógica de Negocio"]
        C_Cal["«control»\nGenerarCalendarioCtrl"]
        C_Acta["«control»\nRegistrarResultadoCtrl"]
        C_Tabla["«control»\nActualizarPosicionesCtrl"]
        C_Jug["«control»\nGestionarJugadoresCtrl"]
    end

    %% Entities (Entidades de Dominio)
    subgraph Entities ["«entity» Entidades Persistentes"]
        E_Torneo["«entity»\nTorneo"]
        E_Formato["«entity»\nFormatoTorneo"]
        E_Inscripcion["«entity»\nInscripcion"]
        E_Equipo["«entity»\nEquipo"]
        E_Jugador["«entity»\nJugador"]
        E_Partido["«entity»\nPartido"]
        E_Acta["«entity»\nActaPartido"]
        E_Evento["«entity»\nEventoPartido"]
        E_Posicion["«entity»\nPosicionTabla"]
    end

    %% Flujo HU-07: Generación automática del calendario
    Admin -->|1. solicita generar calendario| B_Cal
    B_Cal -->|2. idTorneo| C_Cal
    C_Cal -->|3. verifica formato| E_Torneo
    C_Cal -->|4. obtiene reglas| E_Formato
    C_Cal -->|5. filtra aprobadas| E_Inscripcion
    C_Cal -->|6. consulta equipos| E_Equipo
    C_Cal -->|7. crea y persiste cruces| E_Partido

    %% Flujo HU-08 y HU-09: Registro de resultados y actualización de posiciones
    Arbitro -->|1. ingresa goles, tarjetas y cierra acta| B_Acta
    B_Acta -->|2. datos del partido y eventos| C_Acta
    C_Acta -->|3. valida estado programado| E_Partido
    C_Acta -->|4. registra eventos| E_Evento
    C_Acta -->|5. calcula marcador y cierra| E_Acta
    C_Acta -->|6. dispara recálculo al cerrar acta| C_Tabla
    C_Tabla -->|7. lee resultado oficial| E_Acta
    C_Tabla -->|8. actualiza PJ, PG, PE, PP, PTS, GF, GC, DG y ordena| E_Posicion
    Admin -->|consulta posiciones| B_Tabla
    Capitan -->|consulta posiciones| B_Tabla
    B_Tabla -->|solicita tabla ordenada| C_Tabla

    %% Flujo HU-05: Gestión de jugadores de un equipo
    Capitan -->|1. agrega/edita/elimina jugador| B_Jug
    B_Jug -->|2. datos personales y dorsal| C_Jug
    C_Jug -->|3. valida cupo 5..15 y propiedad| E_Equipo
    C_Jug -->|4. valida documento único y edad 16..45| E_Jugador
```

---

## 3. Matriz de Elementos ECB por Historia Crítica

| Historia de Usuario | `«boundary»` (Límite) | `«control»` (Control) | `«entity»` (Entidad) | Reglas de Negocio Orquestadas |
|---|---|---|---|---|
| **HU-07: Generación automática del calendario** | `CalendarioFormView` | `GenerarCalendarioCtrl` | `Torneo`, `FormatoTorneo`, `Inscripcion`, `Equipo`, `Partido` | Verifica que el torneo tenga formato definido (*Todos contra todos* o *Eliminación directa*), toma solo equipos con inscripción `APROBADA` y evita partidos duplicados. |
| **HU-08: Registro de resultados y eventos** | `ActaPartidoFormView` | `RegistrarResultadoCtrl` | `Partido`, `ActaPartido`, `EventoPartido` | Registra goles y tarjetas, calcula el marcador final y cambia el estado del acta a `CERRADA`, bloqueando ediciones posteriores. |
| **HU-09: Actualización automática de posiciones** | `TablaPosicionesView` | `ActualizarPosicionesCtrl` | `ActaPartido`, `PosicionTabla`, `Equipo` | Se activa exclusivamente al cerrar un acta en HU-08; actualiza `PJ`, `PG`, `PE`, `PP`, `PTS`, `GF`, `GC`, `DG` y ordena por `PTS → DG → GF`. |
| **HU-05: Gestión de jugadores de un equipo** | `PlantillaJugadoresFormView` | `GestionarJugadoresCtrl` | `Equipo`, `Jugador` | Valida que el Capitán sea dueño del equipo, que la plantilla tenga entre 5 y 15 jugadores, documento no duplicado y edad entre 16 y 45 años. |
