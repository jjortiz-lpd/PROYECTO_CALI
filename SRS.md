# SRS — Especificación de Requisitos del Sistema

## 1. Información general

**Proyecto:** TorneoJAV  
**Tipo:** Gestor de torneos de fútbol amateur

## 2. Propósito

Desarrollar una solución que permita gestionar de forma organizada la información de torneos de fútbol amateur, incluyendo equipos, jugadores, calendarios, resultados y posiciones.

## 3. Actores

- Administrador de liga
- Capitán de equipo
- Árbitro / anotador

## 4. Requisitos funcionales

| ID | Requisito |
|---|---|
| RF-01 | El sistema debe permitir iniciar sesión mediante usuario y contraseña. |
| RF-02 | El sistema debe identificar el rol del usuario. |
| RF-03 | El administrador debe poder crear torneos. |
| RF-04 | El administrador debe poder seleccionar el formato del torneo. |
| RF-05 | El administrador debe poder aprobar equipos. |
| RF-06 | El capitán debe poder registrar un equipo. |
| RF-07 | El capitán debe poder registrar jugadores. |
| RF-08 | El sistema debe permitir consultar los equipos inscritos. |
| RF-09 | El sistema debe generar el calendario de partidos. |
| RF-10 | El árbitro debe poder registrar resultados. |
| RF-11 | El árbitro debe poder registrar goles y tarjetas. |
| RF-12 | El árbitro debe poder cerrar el acta del partido. |
| RF-13 | El sistema debe actualizar la tabla de posiciones. |
| RF-14 | Los usuarios deben poder consultar el calendario. |
| RF-15 | Los usuarios autorizados deben poder consultar estadísticas. |

## 5. Requisitos no funcionales

- **RNF-01 Seguridad:** las contraseñas deben almacenarse mediante un mecanismo seguro como BCrypt.
- **RNF-02 Rendimiento:** las operaciones normales deben responder de forma adecuada para el tamaño esperado de las ligas.
- **RNF-03 Usabilidad:** la interfaz debe ser sencilla y diferenciada según el rol.
- **RNF-04 Integridad:** no deben registrarse resultados inválidos o partidos inexistentes.
- **RNF-05 Consistencia:** los resultados registrados deben reflejarse en la tabla de posiciones.
- **RNF-06 Mantenibilidad:** separar interfaz, lógica de negocio y persistencia.
- **RNF-07 Compatibilidad:** Java 17, JavaFX y PostgreSQL.

## 6. Tecnología prevista

- Java 17
- JavaFX
- PostgreSQL
- JDBC / DAO
- BCrypt

## 7. Pendientes de validación

- Modelo entidad-relación definitivo.
- Arquitectura definitiva.
- Wireframes definitivos.
- Reglas detalladas para cada formato de torneo.
- Reglas completas de cálculo de posiciones y estadísticas.
