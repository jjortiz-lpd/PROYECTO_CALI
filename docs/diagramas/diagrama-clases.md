# Diagrama de Clases — TorneoJAV

## 1. Descripción

Este diagrama muestra las clases tal como están implementadas en `src/torneojav/`. Para que se pueda leer, está dividido en tres partes que corresponden a las capas del sistema:

| Parte | Paquete | Contenido |
|---|---|---|
| 2.1 Dominio | `torneojav.modelo` | Entidades y enumeraciones |
| 2.2 Negocio | `torneojav.servicio` | Servicios por historia de usuario y estrategias de calendario y desempate |
| 2.3 Persistencia | `torneojav.repositorio`, `torneojav.persistencia` | Interfaces de repositorio, implementaciones JDBC y en memoria, conexión |

Notación usada:

| Símbolo | Significado |
|---|---|
| `<\|--` | Herencia |
| `<\|..` | Realización (una clase implementa una interfaz) |
| `o--` | Agregación |
| `-->` | Asociación: la clase tiene un atributo de ese tipo |
| `..>` | Dependencia: la clase usa a la otra o la referencia por su id |

Los getters, setters y `toString()` se omiten.

---

## 2. Representación Gráfica (UML / Mermaid)

### 2.1 Dominio (`torneojav.modelo`)

```mermaid
classDiagram
    direction LR

    class Rol {
        <<enumeration>>
        ADMINISTRADOR
        CAPITAN
        ARBITRO
    }
    class FormatoTorneo {
        <<enumeration>>
        TODOS_CONTRA_TODOS
        ELIMINACION_DIRECTA
        -nombreVisible: String
    }
    class EstadoInscripcion {
        <<enumeration>>
        PENDIENTE
        APROBADO
        RECHAZADO
    }
    class EstadoPartido {
        <<enumeration>>
        PROGRAMADO
        EN_CURSO
        FINALIZADO
    }
    class TipoEvento {
        <<enumeration>>
        GOL
        TARJETA_AMARILLA
        TARJETA_ROJA
        CAMBIO
    }

    class Usuario {
        -id: int
        -nombreUsuario: String
        -contrasena: String
        -rol: Rol
    }
    class Torneo {
        -id: int
        -nombre: String
        -fechaInicio: LocalDate
        -formato: FormatoTorneo
        +tieneFormato() boolean
    }
    class Equipo {
        -id: int
        -nombre: String
        -capitanId: int
        -torneoId: int
        -estado: EstadoInscripcion
    }
    class Jugador {
        -id: int
        -nombreCompleto: String
        -documento: String
        -fechaNacimiento: LocalDate
        -numeroCamiseta: int
        -equipoId: int
    }
    class Partido {
        -id: int
        -torneoId: int
        -local: Equipo
        -visitante: Equipo
        -fecha: LocalDate
        -ronda: int
        -estado: EstadoPartido
        -golesLocal: int
        -golesVisitante: int
        -actaCerrada: boolean
    }
    class EventoPartido {
        -id: int
        -partidoId: int
        -tipo: TipoEvento
        -minuto: int
        -jugadorId: int
        -equipoId: int
    }
    class PosicionTabla {
        +PUNTOS_VICTORIA: int = 3$
        +PUNTOS_EMPATE: int = 1$
        -torneoId: int
        -equipo: Equipo
        -partidosJugados: int
        -partidosGanados: int
        -partidosEmpatados: int
        -partidosPerdidos: int
        -golesFavor: int
        -golesContra: int
        -puntos: int
        +registrarResultado(golesPro: int, golesRival: int) void
        +calcularDiferenciaGoles() int
    }
    class EstadisticasTorneo {
        -partidosJugados: int
        -partidosProgramados: int
        -totalGoles: int
        -promedioGoles: double
        -masGoleadores: List~PosicionTabla~
        -vallasMenosVencidas: List~PosicionTabla~
        -tarjetasAmarillas: int
        -tarjetasRojas: int
        -tarjetasPorEquipo: Map
    }

    Usuario --> Rol
    Torneo --> "0..1" FormatoTorneo
    Equipo --> EstadoInscripcion
    Partido --> EstadoPartido
    EventoPartido --> TipoEvento

    Partido "0..*" --> "1" Equipo : local
    Partido "0..*" --> "1" Equipo : visitante
    PosicionTabla "0..*" --> "1" Equipo : equipo
    EstadisticasTorneo o-- "0..*" PosicionTabla : más goleadores y vallas

    Equipo "0..*" ..> "1" Usuario : capitanId
    Equipo "0..*" ..> "1" Torneo : torneoId
    Jugador "5..15" ..> "1" Equipo : equipoId
    Partido "0..*" ..> "1" Torneo : torneoId
    EventoPartido "0..*" ..> "1" Partido : partidoId
    EventoPartido "0..*" ..> "1" Jugador : jugadorId
    PosicionTabla "0..*" ..> "1" Torneo : torneoId
```

### 2.2 Negocio (`torneojav.servicio`)

```mermaid
classDiagram
    direction LR

    class AutenticacionService {
        -repositorio: UsuarioRepositorio
        -usuarioActual: Usuario
        +iniciarSesion(nombreUsuario: String, contrasena: String) Usuario
        +cerrarSesion() void
        +getUsuarioActual() Usuario
        +verificarRol(permitidos: Rol...) void
    }

    class TorneoService {
        +crear(nombre: String, fechaInicio: LocalDate, formato: FormatoTorneo) Torneo
        +listar() List~Torneo~
        +buscarPorId(id: int) Optional~Torneo~
        +actualizar(id: int, nombre: String, fecha: LocalDate) Torneo
        +eliminar(id: int) void
    }
    class FormatoService {
        +opcionesDisponibles() FormatoTorneo[]
        +asignarFormato(torneoId: int, formato: FormatoTorneo) Torneo
    }
    class EquipoService {
        +registrar(nombre: String, torneoId: int) Equipo
        +consultarMisEquipos() List~Equipo~
        +modificarNombre(equipoId: int, nuevoNombre: String) Equipo
    }
    class JugadorService {
        +MAX_JUGADORES: int = 15$
        +MIN_JUGADORES: int = 5$
        +EDAD_MIN: int = 16$
        +EDAD_MAX: int = 45$
        +agregar(equipoId, nombre, documento, nacimiento, camiseta) Jugador
        +editar(jugadorId, nombre, documento, nacimiento, camiseta) Jugador
        +eliminar(jugadorId: int) void
        +listarPorEquipo(equipoId: int) List~Jugador~
    }
    class InscripcionService {
        +listarPendientes(torneoId: int) List~Equipo~
        +aprobar(equipoId: int) Equipo
        +rechazar(equipoId: int) Equipo
        +consultarEstado(equipoId: int) EstadoInscripcion
        +equiposAprobados(torneoId: int) List~Equipo~
    }
    class CalendarioService {
        -generadores: Map~FormatoTorneo, IGeneradorCalendario~
        +generarCalendario(torneoId: int) List~Partido~
        +generarSiguienteRonda(torneoId: int) List~Partido~
        +consultarCalendario(torneoId: int) List~Partido~
    }
    class ResultadoService {
        +partidosProgramados(torneoId: int) List~Partido~
        +registrarEvento(partidoId: int, tipo: TipoEvento, minuto: int, jugadorId: int) EventoPartido
        +calcularResultado(partidoId: int) Partido
        +cerrarActa(partidoId: int) Partido
        +eventosDelPartido(partidoId: int) List~EventoPartido~
    }
    class TablaPosicionesService {
        -criterio: ICriterioDesempate
        +recalcular(torneoId: int) List~PosicionTabla~
        +consultar(torneoId: int) List~PosicionTabla~
    }
    class ConsultaService {
        +listarTorneos() List~Torneo~
        +seleccionarTorneo(torneoId: int) Torneo
        +partidosProgramados(torneoId: int) List~Partido~
        +partidosFinalizados(torneoId: int) List~Partido~
        +tablaPosiciones(torneoId: int) List~PosicionTabla~
        +estadisticas(torneoId: int) EstadisticasTorneo
    }

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
        +generarSiguienteRonda(torneo: Torneo, aprobados: List~Equipo~, existentes: List~Partido~) List~Partido~
        +obtenerNombreFormato() String
    }
    class ICriterioDesempate {
        <<interface>>
        +ordenarPosiciones(posiciones: List~PosicionTabla~) List~PosicionTabla~
    }
    class CriterioEstandarTorneo {
        +ordenarPosiciones(posiciones: List~PosicionTabla~) List~PosicionTabla~
    }

    IGeneradorCalendario <|.. GeneradorTodosContraTodos
    IGeneradorCalendario <|.. GeneradorEliminacionDirecta
    ICriterioDesempate <|.. CriterioEstandarTorneo

    CalendarioService --> "2" IGeneradorCalendario : escoge según el formato
    TablaPosicionesService --> "1" ICriterioDesempate : ordena con
    ResultadoService --> TablaPosicionesService : recalcula al cerrar acta
    ConsultaService --> TablaPosicionesService : lee la tabla

    TorneoService --> AutenticacionService
    FormatoService --> AutenticacionService
    EquipoService --> AutenticacionService
    JugadorService --> AutenticacionService
    InscripcionService --> AutenticacionService
    CalendarioService --> AutenticacionService
    ResultadoService --> AutenticacionService
    TablaPosicionesService --> AutenticacionService
    ConsultaService --> AutenticacionService
```

Todos los servicios reciben un `AutenticacionService` y llaman a `verificarRol(...)` al comienzo de las operaciones que exigen un rol. Así se cumple HU-01 CA-07 en la capa de negocio y no solo en el menú.

Repositorios que usa cada servicio (todos se reciben por el constructor como interfaz):

| Servicio | HU | Repositorios |
|---|---|---|
| `AutenticacionService` | HU-01 | `UsuarioRepositorio` |
| `TorneoService` | HU-02 | `TorneoRepositorio`, `EquipoRepositorio`, `PartidoRepositorio` |
| `FormatoService` | HU-03 | `TorneoRepositorio`, `PartidoRepositorio` |
| `EquipoService` | HU-04 | `EquipoRepositorio` |
| `JugadorService` | HU-05 | `JugadorRepositorio`, `EquipoRepositorio` |
| `InscripcionService` | HU-06 | `EquipoRepositorio` |
| `CalendarioService` | HU-07 | `TorneoRepositorio`, `EquipoRepositorio`, `PartidoRepositorio` |
| `ResultadoService` | HU-08 | `PartidoRepositorio`, `EventoRepositorio`, `JugadorRepositorio`, `TorneoRepositorio` |
| `TablaPosicionesService` | HU-09 | `PosicionRepositorio`, `PartidoRepositorio`, `EquipoRepositorio` |
| `ConsultaService` | HU-10 | `TorneoRepositorio`, `PartidoRepositorio`, `EventoRepositorio` |

### 2.3 Persistencia (`torneojav.repositorio` y `torneojav.persistencia`)

```mermaid
classDiagram
    direction LR

    class UsuarioRepositorio {
        <<interface>>
        +buscarPorNombre(nombreUsuario: String) Optional~Usuario~
    }
    class TorneoRepositorio {
        <<interface>>
        +guardar(torneo: Torneo) Torneo
        +listarTodos() List~Torneo~
        +buscarPorId(id: int) Optional~Torneo~
        +eliminar(id: int) void
    }
    class EquipoRepositorio {
        <<interface>>
        +guardar(equipo: Equipo) Equipo
        +buscarPorId(id: int) Optional~Equipo~
        +buscarPorTorneo(torneoId: int) List~Equipo~
        +buscarPorCapitan(capitanId: int) List~Equipo~
    }
    class JugadorRepositorio {
        <<interface>>
        +guardar(jugador: Jugador) Jugador
        +buscarPorId(id: int) Optional~Jugador~
        +buscarPorEquipo(equipoId: int) List~Jugador~
        +eliminar(id: int) void
    }
    class PartidoRepositorio {
        <<interface>>
        +guardar(partido: Partido) Partido
        +buscarPorId(id: int) Optional~Partido~
        +buscarPorTorneo(torneoId: int) List~Partido~
    }
    class EventoRepositorio {
        <<interface>>
        +guardar(evento: EventoPartido) EventoPartido
        +buscarPorPartido(partidoId: int) List~EventoPartido~
    }
    class PosicionRepositorio {
        <<interface>>
        +reemplazarTabla(torneoId: int, nuevaTabla: List~PosicionTabla~) void
        +buscarPorTorneo(torneoId: int) List~PosicionTabla~
    }

    class GestorConexionJDBC {
        -url: String$
        -usuario: String$
        -contrasena: String$
        +obtenerConexion()$ Connection
        +probarConexion()$ void
        -leerConfiguracion()$ void
        -buscarArchivo()$ File
    }
    class PersistenciaException {
        <<exception>>
    }

    UsuarioRepositorio <|.. UsuarioRepositorioPostgres
    TorneoRepositorio <|.. TorneoRepositorioPostgres
    EquipoRepositorio <|.. EquipoRepositorioPostgres
    JugadorRepositorio <|.. JugadorRepositorioPostgres
    PartidoRepositorio <|.. PartidoRepositorioPostgres
    EventoRepositorio <|.. EventoRepositorioPostgres
    PosicionRepositorio <|.. PosicionRepositorioPostgres

    UsuarioRepositorio <|.. UsuarioRepositorioMemoria
    TorneoRepositorio <|.. TorneoRepositorioMemoria
    EquipoRepositorio <|.. EquipoRepositorioMemoria
    JugadorRepositorio <|.. JugadorRepositorioMemoria
    PartidoRepositorio <|.. PartidoRepositorioMemoria
    EventoRepositorio <|.. EventoRepositorioMemoria
    PosicionRepositorio <|.. PosicionRepositorioMemoria

    UsuarioRepositorioPostgres ..> GestorConexionJDBC
    TorneoRepositorioPostgres ..> GestorConexionJDBC
    EquipoRepositorioPostgres ..> GestorConexionJDBC
    JugadorRepositorioPostgres ..> GestorConexionJDBC
    PartidoRepositorioPostgres ..> GestorConexionJDBC
    EventoRepositorioPostgres ..> GestorConexionJDBC
    PosicionRepositorioPostgres ..> GestorConexionJDBC
    GestorConexionJDBC ..> PersistenciaException : lanza
```

Cada interfaz tiene dos implementaciones:

| Implementación | Dónde guarda | Para qué |
|---|---|---|
| `...RepositorioPostgres` | PostgreSQL, con JDBC y `PreparedStatement` | La que usa la aplicación |
| `...RepositorioMemoria` | Una `List` en memoria | Probar la lógica de negocio sin base de datos |

`GestorConexionJDBC` lee la URL, el usuario y la contraseña de `db.properties` una sola vez y abre una conexión nueva en cada operación. Cada repositorio la cierra con `try-with-resources`. `PosicionRepositorioPostgres.reemplazarTabla` es la única operación que usa una transacción explícita (`setAutoCommit(false)`, `commit()`, `rollback()`), porque borra y vuelve a escribir varias filas que deben cambiar juntas.

### 2.4 Excepciones (`torneojav.excepcion`)

Todas heredan de `RuntimeException`, así que no obligan a usar `throws` en cada método. La interfaz las atrapa y muestra el mensaje al usuario.

| Excepción | Se lanza cuando |
|---|---|
| `CredencialesInvalidasException` | El usuario o la contraseña no coinciden (HU-01) |
| `AccesoDenegadoException` | El rol no tiene permiso, o un capitán intenta tocar un equipo ajeno |
| `DatosInvalidosException` | Falta un dato obligatorio o se rompe una regla de negocio |
| `EquipoDuplicadoException` | Ya existe un equipo con ese nombre en el torneo (HU-04) |
| `FormatoNoDefinidoException` | Se intenta generar el calendario de un torneo sin formato (HU-03, HU-07) |
| `ActaCerradaException` | Se intenta modificar un partido cuya acta ya se cerró (HU-08) |
| `PersistenciaException` | Falla la conexión o una consulta a PostgreSQL. Envuelve la `SQLException` |

---

## 3. Aplicación de los Principios SOLID

1. **SRP (Responsabilidad Única).** Cada servicio atiende una sola historia de usuario: `CalendarioService` solo arma calendarios y `TablaPosicionesService` solo calcula posiciones. Dentro de la persistencia, cada repositorio maneja una tabla y `GestorConexionJDBC` es el único que sabe cómo conectarse. Si cambia la contraseña de la base, se toca un archivo de configuración y ninguna clase.

2. **OCP (Abierto/Cerrado).** `CalendarioService` guarda los generadores en un `Map<FormatoTorneo, IGeneradorCalendario>`. Para agregar un formato nuevo (por ejemplo, fase de grupos) se crea otra clase que implemente `IGeneradorCalendario`, se agrega el valor al enum y se registra en el mapa. El algoritmo de los formatos que ya existen no se modifica. `ICriterioDesempate` funciona igual para la tabla de posiciones.

3. **LSP (Sustitución de Liskov).** `GeneradorTodosContraTodos` y `GeneradorEliminacionDirecta` se usan a través de la interfaz sin que el servicio sepa cuál es. Lo mismo pasa con los repositorios: `TorneoRepositorioPostgres` y `TorneoRepositorioMemoria` son intercambiables y los servicios se comportan igual con cualquiera de los dos.

4. **ISP (Segregación de Interfaces).** No hay un repositorio genérico con todos los métodos posibles. Cada interfaz tiene solo lo que sus servicios necesitan: `UsuarioRepositorio` tiene un solo método, `EventoRepositorio` no tiene `eliminar` porque los eventos de un acta no se borran y `PosicionRepositorio` reemplaza la tabla completa en lugar de editar filas sueltas.

5. **DIP (Inversión de Dependencias).** Los servicios dependen de interfaces (`EquipoRepositorio`, `ICriterioDesempate`) y las reciben por el constructor. La única clase que crea objetos concretos es `App.java`, en su método `main`. Para cambiar PostgreSQL por memoria basta con cambiar las líneas `new ...RepositorioPostgres()` por `new ...RepositorioMemoria()` en ese método.

---

## 4. Cambios respecto al diseño inicial

El primer diagrama se hizo antes de programar. Durante la implementación se simplificaron algunas partes; esta tabla registra qué cambió y por qué:

| Diseño inicial | Implementación | Motivo |
|---|---|---|
| `Administrador`, `Capitan` y `Arbitro` heredan de `Usuario` | Una sola clase `Usuario` con el enum `Rol` | Los tres roles tienen los mismos datos; lo que cambia son los permisos, y eso se controla con `verificarRol` |
| Clase `Inscripcion` aparte | Atributo `estado` en `Equipo` | Un equipo se inscribe en un solo torneo, así que la inscripción no necesita tabla propia |
| Clase `ActaPartido` aparte | Atributos `golesLocal`, `golesVisitante` y `actaCerrada` en `Partido` | Cada partido tiene una sola acta. El marcador se calcula a partir de `EventoPartido` |
| `IGenericDAO<T, ID>` para todas las entidades | Una interfaz de repositorio por entidad | No todas las entidades necesitan los mismos métodos (ver ISP) |
| Conexión con patrón Singleton | `GestorConexionJDBC` con métodos estáticos que abren una conexión por operación | Una conexión compartida se puede quedar abierta o caerse; abrir y cerrar en cada operación es más simple y seguro para una aplicación de un solo usuario |
| Observador `IObservadorCierreActa` | `ResultadoService` llama directamente a `TablaPosicionesService.recalcular` | Solo hay una acción que depende del cierre del acta. Si se agregan más (por ejemplo, notificar a los capitanes), conviene volver al observador |
