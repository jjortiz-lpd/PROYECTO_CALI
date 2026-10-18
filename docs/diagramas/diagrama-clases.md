# Diagrama de Clases y Principios SOLID — TorneoJAV

## 1. Descripción del Modelo de Clases
El diagrama de clases modela la estructura estática del dominio de **TorneoJAV**, sus interfaces de servicio/estrategia y las relaciones estructurales entre entidades:
- **Herencia / Realización (`<|--` / `<|..`):** Especialización de roles de usuario (`Administrador`, `Capitan`, `Arbitro` heredan de `Usuario`) e implementación de algoritmos de calendario (`GeneradorTodosContraTodos`, `GeneradorEliminacionDirecta` implementan `IGeneradorCalendario`).
- **Composición (`*--`):** Ciclo de vida fuertemente acoplado (`Partido` contiene `ActaPartido`; `ActaPartido` contiene `EventoPartido`; `Torneo` contiene `PosicionTabla`).
- **Agregación (`o--`):** Agrupación donde la parte puede existir independientemente del contenedor (`Equipo` agrupa objetos `Jugador`; `Torneo` agrupa `Inscripcion`).
- **Asociación (`-->`):** Vínculos estructurales con multiplicidad explícita (`Capitan 1 --> 0..1 Equipo`, `Arbitro 1 --> 0..* Partido`).

---

## 2. Representación Gráfica (UML / Mermaid)

```mermaid
classDiagram
    direction TB

    %% Jerarquía de Usuarios (Herencia)
    class Usuario {
        <<abstract>>
        #idUsuario: Long
        #nombreCompleto: String
        #correo: String
        #passwordHash: String
        #rol: RolUsuario
        +autenticar(correo: String, clave: String) Boolean
        +obtenerRol() RolUsuario
    }

    class Administrador {
        +crearTorneo(torneo: Torneo) Boolean
        +aprobarInscripcion(idInscripcion: Long) void
        +rechazarInscripcion(idInscripcion: Long, motivo: String) void
    }

    class Capitan {
        -telefonoContacto: String
        +registrarEquipo(equipo: Equipo) Equipo
        +agregarJugador(jugador: Jugador) Boolean
        +eliminarJugador(idJugador: Long) Boolean
    }

    class Arbitro {
        -categoriaArbitral: String
        +registrarEvento(idPartido: Long, evento: EventoPartido) void
        +cerrarActa(idPartido: Long) ActaPartido
    }

    Usuario <|-- Administrador
    Usuario <|-- Capitan
    Usuario <|-- Arbitro

    %% Entidades Nucleares del Torneo
    class Torneo {
        -idTorneo: Long
        -nombre: String
        -fechaInicio: LocalDate
        -estado: String
        -minJugadoresEquipo: Integer = 5
        -maxJugadoresEquipo: Integer = 15
        -edadMinima: Integer = 16
        -edadMaxima: Integer = 45
        +configurarFormato(formato: IGeneradorCalendario) void
        +validarDatosObligatorios() Boolean
        +puedeEliminarse() Boolean
    }

    class Equipo {
        -idEquipo: Long
        -nombre: String
        -fechaRegistro: LocalDate
        +validarCupoMaximo(max: Integer) Boolean
        +validarCupoMinimo(min: Integer) Boolean
        +existeDocumentoEnPlantilla(documento: String) Boolean
    }

    class Jugador {
        -idJugador: Long
        -nombreCompleto: String
        -numeroDocumento: String
        -fechaNacimiento: LocalDate
        -numeroCamiseta: Integer
        +calcularEdadActual() Integer
        +cumpleRangoEdad(min: Integer, max: Integer) Boolean
    }

    class Inscripcion {
        -idInscripcion: Long
        -fechaSolicitud: LocalDate
        -estado: EstadoInscripcion
        +aprobar() void
        +rechazar() void
        +estaAprobada() Boolean
    }

    %% Calendario y Estrategia de Generación (OCP / DIP)
    class IGeneradorCalendario {
        <<interface>>
        +generarPartidos(torneo: Torneo, equiposAprobados: List~Equipo~) List~Partido~
        +obtenerNombreFormato() String
    }

    class GeneradorTodosContraTodos {
        +generarPartidos(torneo: Torneo, equiposAprobados: List~Equipo~) List~Partido~
        +obtenerNombreFormato() String
    }

    class GeneradorEliminacionDirecta {
        +generarPartidos(torneo: Torneo, equiposAprobados: List~Equipo~) List~Partido~
        +obtenerNombreFormato() String
    }

    IGeneradorCalendario <|.. GeneradorTodosContraTodos
    IGeneradorCalendario <|.. GeneradorEliminacionDirecta

    %% Partidos, Actas y Eventos (Composición)
    class Partido {
        -idPartido: Long
        -fechaHora: LocalDateTime
        -ronda: Integer
        -estado: EstadoPartido
        +programar(fecha: LocalDateTime) void
        +finalizarPartido() void
    }

    class ActaPartido {
        -idActa: Long
        -golesLocal: Integer
        -golesVisitante: Integer
        -cerrada: Boolean
        -fechaCierre: LocalDateTime
        +calcularResultado() void
        +agregarEvento(evento: EventoPartido) void
        +cerrarActa() void
        +PermiteEdicion() Boolean
    }

    class EventoPartido {
        -idEvento: Long
        -minuto: Integer
        -tipoEvento: TipoEvento
        -descripcion: String
        +esGol() Boolean
        +esTarjeta() Boolean
    }

    %% Tabla de Posiciones y Criterio de Ordenamiento
    class PosicionTabla {
        -idPosicion: Long
        -partidosJugados: Integer
        -partidosGanados: Integer
        -partidosEmpatados: Integer
        -partidosPerdidos: Integer
        -golesFavor: Integer
        -golesContra: Integer
        -puntos: Integer
        +calcularDiferenciaGoles() Integer
        +registrarResultado(golesPro: Integer, golesRival: Integer) void
    }

    class ICriterioDesempate {
        <<interface>>
        +ordenarPosiciones(posiciones: List~PosicionTabla~) List~PosicionTabla~
    }

    class CriterioEstandarTorneo {
        +ordenarPosiciones(posiciones: List~PosicionTabla~) List~PosicionTabla~
    }

    ICriterioDesempate <|.. CriterioEstandarTorneo

    %% Relaciones y Multiplicidades
    Administrador "1" --> "0..*" Torneo : gestiona
    Capitan "1" --> "0..1" Equipo : administra
    Equipo "1" o-- "5..15" Jugador : plantilla
    Torneo "1" o-- "0..*" Inscripcion : recibe
    Equipo "1" --> "0..*" Inscripcion : solicita
    Torneo "1" --> "1" IGeneradorCalendario : usa formato
    Torneo "1" *-- "0..*" Partido : calendario
    Equipo "2" <-- "0..*" Partido : local / visitante
    Arbitro "1" --> "0..*" Partido : dirige
    Partido "1" *-- "0..1" ActaPartido : registra
    ActaPartido "1" *-- "0..*" EventoPartido : contiene
    EventoPartido "0..*" --> "1" Jugador : involucra
    Torneo "1" *-- "0..*" PosicionTabla : tabla posiciones
    PosicionTabla "0..*" --> "1" Equipo : representa
    Torneo "1" --> "1" ICriterioDesempate : aplica
```

---

## 3. Aplicación de los Principios SOLID en el Diseño

1. **SRP — Single Responsibility Principle (Principio de Responsabilidad Única):**
   Cada clase tiene una única razón para cambiar. `Partido` gestiona la programación del encuentro (equipos, fecha, ronda y estado), mientras que `ActaPartido` encapsula el marcador, los eventos y el bloqueo de cierre arbitral, y `PosicionTabla` se encarga exclusivamente del cómputo estadístico (`PJ`, `PG`, `PE`, `PP`, `PTS`, `GF`, `GC`, `DG`).
2. **OCP — Open/Closed Principle (Principio de Abierto/Cerrado):**
   La generación del calendario está abierta a la extensión pero cerrada a la modificación mediante la interfaz `IGeneradorCalendario`. Si en el futuro se agrega un formato *"Fase de grupos + Eliminatoria"*, basta con crear una nueva clase que implemente `IGeneradorCalendario` sin alterar el código de `Torneo` ni de `GenerarCalendarioCtrl`. Lo mismo ocurre con `ICriterioDesempate`.
3. **LSP — Liskov Substitution Principle (Principio de Sustitución de Liskov):**
   Las subclases `Administrador`, `Capitan` y `Arbitro` respetan el contrato de la superclase `Usuario` (`autenticar()` y `obtenerRol()`), y tanto `GeneradorTodosContraTodos` como `GeneradorEliminacionDirecta` pueden sustituirse de forma transparente donde se espera un `IGeneradorCalendario`, retornando siempre una lista válida de objetos `Partido` sin efectos secundarios inesperados.
4. **ISP — Interface Segregation Principle (Principio de Segregación de Interfaces):**
   En lugar de definir una interfaz monolítica `ITorneoManager` que obligue a implementar métodos de calendario, arbitraje y posiciones en una sola clase, los contratos están segregados en interfaces específicas de dominio (`IGeneradorCalendario`, `ICriterioDesempate` y DAOs separados por entidad), de modo que cada servicio depende únicamente de los métodos que utiliza.
5. **DIP — Dependency Inversion Principle (Principio de Inversión de Dependencias):**
   Los módulos de alto nivel (`Torneo`, controladores de negocio) no dependen de implementaciones concretas de bajo nivel (como algoritmos fijos de emparejamiento o consultas SQL directas), sino de abstracciones (`IGeneradorCalendario`, `ICriterioDesempate` e interfaces `IRepositorioDAO<T>`).
