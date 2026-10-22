# Reporte Gerencial — TorneoJAV

## 1. Información General del Proyecto
- **Proyecto:** TorneoJAV
- **Asignatura:** Fundamentos de Ingeniería de Software (`FIS_2630`)
- **Institución:** Pontificia Universidad Javeriana
- **Periodo:** 3
- **Docente:** Kerwin de Jesús Barros Somerson
- **Repositorio:** https://github.com/jjortiz-lpd/PROYECTO_CALI

---

## 2. Objetivo
Centralizar la gestión de torneos deportivos, usuarios y control de acceso por roles, inscripción de equipos, administración de plantillas de jugadores, generación automática de calendarios, registro de resultados y actas arbitrales, actualización de tablas de posiciones y consulta de estadísticas en una sola plataforma.

---

## 3. Equipo de Desarrollo y Organigrama
| Integrante | Rol de Gestión / Ingeniería | Responsabilidad Técnica Principal | GitHub |
|---|---|---|---|
| **Juan José Ortiz** | Configuration Manager | Modelo de datos (E-R), persistencia, capa DAO/JDBC y PostgreSQL. | `@jjortiz-lpd` |
| **Esteban Hernández** | DevOps Engineer | Autenticación por roles, gestión de usuarios, equipos y jugadores. | `@estebanalejandrohh-prog` |
| **Natalia Valencia** | Product Owner + QA Lead | Gestión de torneos, formatos, inscripciones y generación del calendario. | `@Nati393` |
| **Juan Pablo Franco** | Scrum Master + Sprint Planner | Resultados, actas arbitrales, posiciones, estadísticas e interfaz gráfica (JavaFX). | `@jpfrancor` |

---

## 4. Alcance Documental y Arquitectónico Consolidado

### 4.1 Documentación de Ingeniería y Gestión (`docs/`)
1. **Lean Canvas (`canvas.md`):** Definición del problema, usuarios, propuesta de valor y solución.
2. **WBS (`WBS.md`):** Estructura de Desglose del Trabajo en 9 paquetes funcionales y de calidad (`1.1 Usuarios` a `1.9 Calidad y documentación`).
3. **Organigrama (`organigrama.md`):** Roles de gestión e ingeniería y distribución técnica del equipo.
4. **Calendario de Actividades (`calendario.md`):** Cronograma de 12 semanas (`14/09/2026` al `07/12/2026`) alineado con los Milestones de GitHub.
5. **Planeación Ágil (`planeacion-agil.md`):** Marco iterativo, flujo funcional de 9 etapas y criterio de terminado (*Definition of Done*).
6. **Definición de Sprints (`plan-sprints.md`):** Planificación de los 4 Sprints de 2 semanas (14 días por iteración).
7. **Historias de Usuario (`historias-usuario.md`):** Especificación de `HU-01` a `HU-10` (Issues `#9` al `#18`) con criterios de aceptación y reglas de negocio cerradas.
8. **Casos de Uso (`casos-de-uso.md`):** Modelado de los 10 casos de uso (`CU-01` a `CU-10`) y sus dependencias funcionales.
9. **Requisitos Funcionales (`requisitos-funcionales.md`):** 33 requisitos funcionales (`RF-01` a `RF-33`) trazados con las Historias de Usuario.
10. **Requisitos No Funcionales (`requisitos-no-funcionales.md`):** 6 atributos de calidad (`RNF-01` a `RNF-06`: seguridad, integridad, persistencia, usabilidad, mantenibilidad y control de acceso).
11. **Estimación (`estimacion.md`):** Dimensionamiento del esfuerzo en 55 Story Points (SP) sincronizado con GitHub Projects.
12. **Herramientas (`herramientas.md`):** Stack tecnológico definido (Java 17, JavaFX, JDBC/DAO, PostgreSQL 16, Git, GitHub Issues y GitHub Projects).
13. **Reporte Gerencial (`reporte-gerencial.md`):** Estado ejecutivo del proyecto, métricas de avance por Sprint y control de entregables.
14. **Actas de Reunión (`actas.md`):** Consolidado de las 7 actas de seguimiento, acuerdos y decisiones del equipo.

### 4.2 Modelos de Diseño Arquitectónico (`docs/diagramas/`)
Se diseñó e integró la arquitectura del sistema a partir de las historias de usuario críticas (`HU-07`, `HU-08`, `HU-09` y `HU-05`), gestionada mediante 4 sub-issues en el repositorio vinculados a `HU-07 (#15)`:

| Sub-Issue | Artefacto en `docs/diagramas/` | Responsable | Contenido Arquitectónico Modelado |
|---|---|---|---|
| **Issue `#32`** | `diagrama-ecb.md` | Natalia Valencia (`@Nati393`) | **Diagrama de Análisis (ECB):** Separación en estereotipos `«boundary»` (vistas JavaFX), `«control»` (gestores de negocio) y `«entity»` (entidades persistentes). |
| **Issue `#33`** | `diagrama-clases.md` | Juan José Ortiz (`@jjortiz-lpd`) | **Diagrama de Clases y SOLID:** Jerarquía de `Usuario`, entidades nucleares, patrones estrategia (`IGeneradorCalendario`, `ICriterioDesempate`), relaciones UML y justificación de los 5 principios SOLID. |
| **Issue `#34`** | `diagrama-componentes.md` | Juan Pablo Franco (`@jpfrancor`) | **Diagrama de Componentes:** Arquitectura en 4 capas (Presentación JavaFX, 6 módulos de Negocio en Java 17, `CapaPersistenciaDAO` / `GestorConexionJDBC` y motor externo PostgreSQL) con interfaces provistas y requeridas. |
| **Issue `#35`** | `diagrama-despliegue.md` | Esteban Hernández (`@estebanalejandrohh-prog`) | **Diagrama de Despliegue:** Topología cliente-servidor entre `Estación de Trabajo Cliente` (OpenJDK 17 + JavaFX 21, `TorneoJAV-App.jar`, `postgresql-42.7.jar`) y `Servidor de Base de Datos` (`PostgreSQL 16`, `schema.sql`) vía `TCP/IP (JDBC)` puerto `5432`. |

---

## 5. Reglas de Negocio Definidas y Cerradas
Todas las reglas de negocio pendientes en la primera iteración documental quedaron formalizadas en los criterios de aceptación y en el modelo de dominio:
- **Límites de plantilla (`HU-05`):** Mínimo de **5 jugadores** (`minJugadoresEquipo = 5`) y máximo de **15 jugadores** (`maxJugadoresEquipo = 15`) por equipo.
- **Rango de edad (`HU-05`):** Jugadores entre **16 y 45 años cumplidos** (`edadMinima = 16`, `edadMaxima = 45`), verificados mediante su fecha de nacimiento.
- **Criterios de desempate en tabla de posiciones (`HU-09`):** Recálculo automático al cerrarse un acta en `HU-08`, ordenando por: **1.º Mayor puntaje (`PTS`) → 2.º Mayor diferencia de goles (`DG = GF - GC`) → 3.º Mayor cantidad de goles a favor (`GF`)**.
- **Estadísticas disponibles del torneo (`HU-10`):** (1) Resumen de partidos jugados/programados, total de goles y promedio de goles por partido, (2) Equipo más goleador (mayor `GF`), (3) Valla menos vencida (menor `GC`) y (4) Balance disciplinario de tarjetas amarillas y rojas registradas.

---

## 6. Estado de Avance por Sprint y Métricas del Tablero Kanban

### 6.1 Resumen Estadístico de Estimación
- **Total del Backlog Principal:** **10 Historias de Usuario** (`#9` a `#18`) equivalentes a **55.0 Story Points (SP)**.
- **Métricas descriptivas por Historia de Usuario:** Media de **5.50 SP**, mediana de **5.00 SP**, desviación estándar de **1.90 SP**, mínimo de **3.00 SP** (`HU-03`, `HU-06`) y máximo de **8.00 SP** (`HU-05`, `HU-07`, `HU-08`).
- **Distribución actual de elementos en el Tablero Kanban (26 ítems totales):** **11 en `Done`** (6 Historias de Usuario, 3 subtareas de `HU-03` `#27-#29` y 2 Pull Requests `#30-#31`), **2 en `Ready`** (`HU-07` y `HU-08`) y **13 en `Backlog`** (`HU-09`, `HU-10`, 4 sub-issues de diseño `#32-#35` y 7 subtareas técnicas `#20-#26`).

### 6.2 Seguimiento de Ejecución por Sprint
| Sprint / Milestone | Periodo | Historias Incluidas | Esfuerzo (SP) | Historias Completadas | SP Completados | Estado Actual |
|---|---|---|---:|---|---|---|
| **Sprint 1 — Acceso y Configuración** | 28/09/2026 – 12/10/2026 | `HU-01`, `HU-02`, `HU-03` | 13.0 SP | 3 / 3 (100.0%) | 13.0 / 13.0 SP (100.0%) | **Completado (`Done`)** — Incluye subtareas `#27`, `#28`, `#29` y PRs `#30`, `#31` |
| **Sprint 2 — Participantes** | 12/10/2026 – 26/10/2026 | `HU-04`, `HU-05`, `HU-06` | 16.0 SP | 3 / 3 (100.0%) | 16.0 / 16.0 SP (100.0%) | **Completado (`Done`)** |
| **Sprint 3 — Competición** | 26/10/2026 – 09/11/2026 | `HU-07`, `HU-08` | 16.0 SP | 0 / 2 (0.0%) | 0.0 / 16.0 SP (0.0%) | **En inicio (`Ready`)** — Con 4 sub-issues de diseño (`#32` a `#35`) asociados a `HU-07` |
| **Sprint 4 — Información** | 09/11/2026 – 23/11/2026 | `HU-09`, `HU-10` | 10.0 SP | 0 / 2 (0.0%) | 0.0 / 10.0 SP (0.0%) | **Planificado (`Backlog`)** |
| **Total Acumulado** | **14/09/2026 – 07/12/2026** | **10 Historias (`#9-#18`)** | **55.0 SP** | **6 / 10 (60.0%)** | **29.0 / 55.0 SP (52.73%)** | **En ejecución (Sprints 1 y 2 cerrados)** |

---

## 7. Pendientes Actuales
- Cerrar en el tablero Kanban los 4 sub-issues del **Taller de Diseño (`#32`, `#33`, `#34` y `#35`)** una vez verificados los commits de `docs/diagramas/`, completando el progreso `4 / 4 (100%)` dentro de `HU-07 (#15)`.
- Desarrollar e integrar las funcionalidades del **Sprint 3 — Competición** (`HU-07: Generación automática del calendario` y `HU-08: Registro de resultados y eventos del partido`, actualmente en estado `Ready`, junto con las subtareas `#20` a `#22`).
-Completar la asignación de responsables (`Assignees`) en las historias principales del tablero de GitHub Projects conforme a la matriz de responsabilidades de `organigrama.md`.
- Ejecutar posteriormente el **Sprint 4 — Información** (`HU-09` y `HU-10`, junto con las subtareas `#23` a `#26`) y las pruebas integrales de las Semanas 11 y 12.

---

## 8. Próximos Pasos
Cerrar sub-issues de diseño (`#32` a `#35`) → Implementar e integrar `HU-07` y `HU-08` (Sprint 3) → Implementar `HU-09` y `HU-10` (Sprint 4) → Ejecutar pruebas de aceptación sobre PostgreSQL y JavaFX → Consolidar entrega final.
