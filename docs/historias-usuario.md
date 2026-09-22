# Historias de usuario oficiales

Estas historias corresponden a los Issues #9 al #18 proporcionados del repositorio.

## HU-01 — Autenticación y control de acceso por roles (#9)

**Como usuario del sistema, quiero iniciar sesión con mis credenciales, para acceder de forma segura a las funcionalidades correspondientes a mi rol.**

### Criterios de aceptación
- CA-01: Dado un usuario registrado, cuando ingrese credenciales válidas, el sistema debe permitir el acceso.
- CA-02: Cuando las credenciales sean incorrectas, el sistema debe rechazar el inicio de sesión y mostrar un mensaje de error.
- CA-03: Después de autenticarse, el sistema debe identificar el rol del usuario.
- CA-04: Un Administrador debe visualizar las funcionalidades administrativas.
- CA-05: Un Capitán debe visualizar las funcionalidades relacionadas con su equipo.
- CA-06: Un Árbitro debe visualizar las funcionalidades relacionadas con partidos y resultados.
- CA-07: Un usuario no debe poder acceder a funcionalidades que no correspondan a su rol.

## HU-02 — Gestión de torneos (#10)

**Como Administrador, quiero gestionar los torneos de la plataforma, para mantener actualizada la información de las competencias que se encuentran en curso.**

### Criterios de aceptación
- CA-01: El Administrador debe poder crear un torneo ingresando como mínimo nombre, fecha y formato.
- CA-02: El sistema debe validar que los campos obligatorios estén diligenciados.
- CA-03: El Administrador debe poder consultar los torneos existentes.
- CA-04: El Administrador debe poder modificar la información de un torneo.
- CA-05: El Administrador debe poder eliminar un torneo que todavía no tenga información que impida su eliminación.
- CA-06: El sistema debe solicitar confirmación antes de eliminar un torneo.
- CA-07: Los cambios realizados deben almacenarse en la base de datos.

## HU-03 — Configuración del formato del torneo (#11)

**Como Administrador, quiero seleccionar el formato de competición del torneo, para que el sistema pueda determinar correctamente la estructura de partidos.**

### Criterios de aceptación
- CA-01: Al crear o configurar un torneo, el sistema debe mostrar las opciones de formato disponibles.
- CA-02: El Administrador debe poder seleccionar "Todos contra todos".
- CA-03: El Administrador debe poder seleccionar "Eliminación directa".
- CA-04: El sistema debe almacenar el formato seleccionado.
- CA-05: El calendario generado posteriormente debe utilizar las reglas correspondientes al formato seleccionado.
- CA-06: El sistema no debe permitir generar un calendario si el torneo no tiene formato definido.

## HU-04 — Registro y administración de equipos (#12)

**Como Capitán, quiero registrar y administrar mi equipo, para participar formalmente en los torneos disponibles.**

### Criterios de aceptación
- CA-01: El Capitán debe poder registrar un equipo proporcionando la información requerida.
- CA-02: El sistema debe validar que el nombre del equipo sea obligatorio.
- CA-03: El sistema debe impedir registros duplicados cuando corresponda.
- CA-04: El Capitán debe poder consultar la información de su equipo.
- CA-05: El Capitán debe poder modificar la información permitida de su equipo.
- CA-06: El Capitán no debe poder modificar equipos pertenecientes a otros capitanes.
- CA-07: El equipo registrado debe quedar asociado al Capitán que lo creó.

## HU-05 — Gestión de jugadores de un equipo (#13)

**Como Capitán, quiero administrar la plantilla de jugadores de mi equipo, para mantener actualizada la información de los participantes del torneo.**

### Criterios de aceptación
- CA-01: El Capitán puede agregar un jugador a su equipo, siempre que el equipo no supere el máximo de [X] jugadores definido por el reglamento del torneo.
- CA-02: El sistema solicita los datos obligatorios del jugador: nombre completo, número de documento, fecha de nacimiento y número de camiseta.
- CA-03: El sistema valida que no se registre dos veces al mismo jugador (mismo número de documento) en la misma plantilla.
- CA-04: El Capitán puede editar la información de un jugador de su equipo.
- CA-05: El Capitán puede eliminar un jugador, siempre que el equipo mantenga el mínimo de [Y] jugadores exigido por las reglas del torneo.
- CA-06: Un Capitán no puede modificar jugadores de otro equipo.
- CA-07: El sistema valida que la edad del jugador esté dentro del rango permitido por el torneo.
- CA-08: Los cambios quedan persistidos de forma permanente en el sistema.

> Pendiente: definir [X], [Y] y las reglas concretas de edad.

## HU-06 — Aprobación de inscripción de equipos (#14)

**Como Administrador, quiero aprobar o rechazar la inscripción de los equipos, para controlar qué equipos pueden participar oficialmente en el torneo.**

### Criterios de aceptación
- CA-01: El sistema debe mostrar al Administrador los equipos pendientes de aprobación.
- CA-02: El Administrador debe poder aprobar una inscripción.
- CA-03: El Administrador debe poder rechazar una inscripción.
- CA-04: El sistema debe registrar el estado de la inscripción.
- CA-05: Un equipo aprobado debe quedar habilitado para participar en la generación del calendario.
- CA-06: Un equipo rechazado no debe incluirse en el calendario oficial.
- CA-07: El Capitán debe poder consultar el estado de inscripción de su equipo.

## HU-07 — Generación automática del calendario (#15)

**Como Administrador, quiero generar automáticamente el calendario del torneo, para evitar errores y reducir el trabajo manual de programación de partidos.**

### Criterios de aceptación
- CA-01: El sistema debe utilizar únicamente equipos aprobados.
- CA-02: El sistema debe verificar que el torneo tenga un formato configurado.
- CA-03: Para "todos contra todos", cada pareja de equipos debe enfrentarse de acuerdo con las reglas establecidas.
- CA-04: Para "eliminación directa", los partidos deben organizarse según las rondas correspondientes.
- CA-05: El sistema no debe generar partidos duplicados.
- CA-06: Cada partido generado debe contener como mínimo los equipos participantes, fecha y estado.
- CA-07: El Administrador debe poder consultar el calendario generado.
- CA-08: Una vez generado, el calendario debe almacenarse en la base de datos.

## HU-08 — Registro de resultados y eventos del partido (#16)

**Como Árbitro, quiero registrar el resultado y los eventos de un partido, para mantener actualizada la información oficial del torneo.**

### Criterios de aceptación
- CA-01: El Árbitro debe poder seleccionar un partido programado.
- CA-02: Debe poder registrar los goles de cada equipo.
- CA-03: Debe poder registrar tarjetas y otros eventos definidos por el sistema.
- CA-04: El sistema debe calcular el resultado final a partir de los goles registrados.
- CA-05: El Árbitro debe poder guardar el acta del partido.
- CA-06: Una vez cerrado el acta, el sistema debe impedir modificaciones no autorizadas.
- CA-07: El resultado debe quedar asociado al partido correspondiente.
- CA-08: Los eventos deben almacenarse junto con la información del partido.

## HU-09 — Actualización automática de tabla de posiciones (#17)

**Como participante del torneo, quiero consultar una tabla de posiciones actualizada automáticamente, para conocer la situación de mi equipo dentro de la competencia.**

### Criterios de aceptación
- CA-01: Cuando un Árbitro cierre un acta, el sistema debe actualizar la tabla de posiciones.
- CA-02: El sistema debe actualizar como mínimo partidos jugados, ganados, empatados, perdidos y puntos.
- CA-03: El sistema debe actualizar los goles a favor y en contra.
- CA-04: El sistema debe calcular la diferencia de goles.
- CA-05: La tabla debe ordenar los equipos de acuerdo con las reglas definidas para el torneo.
- CA-06: Un resultado registrado no debe modificar la tabla antes de que el acta sea validada/cerrada.
- CA-07: Los usuarios autorizados deben poder consultar la tabla actualizada.
- CA-08: La información mostrada debe corresponder a los resultados almacenados.

## HU-10 — Consulta de calendario, resultados y estadísticas (#18)

**Como usuario autorizado, quiero consultar la información actualizada del torneo, para conocer los próximos partidos, resultados, posiciones y estadísticas de los equipos.**

### Criterios de aceptación
- CA-01: El usuario debe poder seleccionar un torneo.
- CA-02: El sistema debe mostrar los partidos programados.
- CA-03: El sistema debe mostrar los partidos finalizados y sus resultados.
- CA-04: El sistema debe mostrar la tabla de posiciones.
- CA-05: El sistema debe mostrar las estadísticas disponibles del torneo.
- CA-06: La información debe corresponder a los datos almacenados en la base de datos.
- CA-07: Los usuarios sin permisos no deben acceder a información restringida.
- CA-08: Las consultas deben realizarse sin modificar la información almacenada.
