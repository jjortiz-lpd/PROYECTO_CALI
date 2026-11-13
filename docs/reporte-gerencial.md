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
14. **Actas de Reunión (`actas.md`):** Consolidado de las sesiones de seguimiento, acuerdos y decisiones del equipo.
15. **Registro de Deuda Técnica (`registro-de-deuda.md`):** Bitácora de atajos técnicos y deudas adquiridas durante la fase de codificación.
16. **Retrospectiva (`retrospectiva.md`):** Análisis de lecciones aprendidas y oportunidades de mejora al cierre de las iteraciones.

### 4.2 Modelos de Diseño Arquitectónico Completados (`docs/diagramas/`)
Se diseñó e integró la arquitectura del sistema a partir de las historias de usuario críticas (`HU-07`, `HU-08`, `HU-09` y `HU-05`), cuyos 4 sub-issues (`#32` al `#35`) ya se encuentran implementados y cerrados:

| Artefacto en `docs/diagramas/` | Responsable | Contenido Arquitectónico Modelado |
|---|---|---|
| `diagrama-ecb.md` | Natalia Valencia | **Diagrama de Análisis (ECB):** Separación en estereotipos `«boundary»` (vistas JavaFX), `«control»` (gestores de negocio) y `«entity»` (entidades persistentes). |
| `diagrama-clases.md` | Juan José Ortiz | **Diagrama de Clases y SOLID:** Jerarquía de `Usuario`, entidades nucleares, patrones estrategia (`IGeneradorCalendario`, `ICriterioDesempate`), relaciones UML alineadas al código y justificación de los principios SOLID. |
| `diagrama-componentes.md` | Juan Pablo Franco | **Diagrama de Componentes:** Arquitectura en 4 capas (Presentación JavaFX, 6 módulos de Negocio en Java 17, `CapaPersistenciaDAO` y motor externo PostgreSQL) con interfaces provistas y requeridas. |
| `diagrama-despliegue.md` | Esteban Hernández | **Diagrama de Despliegue:** Topología cliente-servidor (con cajas 3D) entre `Estación de Trabajo Cliente` (OpenJDK 17 + JavaFX 21, `TorneoJAV-App.jar`, `postgresql-42.7.jar`) y `Servidor de Base de Datos` (`PostgreSQL 16`, `schema.sql`) vía `TCP/IP (JDBC)` puerto `5432`. |
| `diagrama-procesos.md` | Equipo de Proyecto | **Diagrama de Procesos (Swimlanes):** Flujo completo de las 10 HU distribuidas en carriles por actor (Administrador, Capitán, Árbitro, Usuario Autorizado), Sistema Java y Base de Datos Separada. |
| `diagrama-secuencia.md` | Equipo de Proyecto | **Diagrama de Secuencia:** Interacciones cronológicas y llamadas a métodos agrupadas en carriles desde la capa de presentación hasta la base de datos externa. |
| `diagrama-patrones.md` | Equipo de Proyecto | **Patrones de Diseño:** Estructura UML de los patrones aplicados (Factory Method, Strategy, Observer, DAO y Singleton). |

---

## 5. Reglas de Negocio Implementadas
Todas las reglas de negocio pendientes quedaron formalizadas e implementadas en la lógica del sistema:
- **Límites de plantilla (`HU-05`):** Mínimo de **5 jugadores** (`minJugadoresEquipo = 5`) y máximo de **15 jugadores** (`maxJugadoresEquipo = 15`) por equipo.
- **Rango de edad (`HU-05`):** Jugadores entre **16 y 45 años cumplidos** (`edadMinima = 16`, `edadMaxima = 45`), verificados mediante su fecha de nacimiento.
- **Criterios de desempate en tabla de posiciones (`HU-09`):** Recálculo automático al cerrarse un acta en `HU-08`, ordenando por: **1.º Mayor puntaje (`PTS`) → 2.º Mayor diferencia de goles (`DG = GF - GC`) → 3.º Mayor cantidad de goles a favor (`GF`)**.
- **Estadísticas disponibles del torneo (`HU-10`):** (1) Resumen de partidos jugados/programados, total de goles y promedio de goles por partido, (2) Equipo más goleador (mayor `GF`), (3) Valla menos vencida (menor `GC`) y (4) Balance disciplinario de tarjetas amarillas y rojas registradas.

---

## 6. Estado de Avance por Sprint y Ejecución
La totalidad de los modelos de diseño (Sub-issues `#32` al `#35`) y la funcionalidad central (backend, servicios, DAOs y base de datos) ya se encuentran desarrolladas. 

### 6.1 Seguimiento de Ejecución por Sprint
| Sprint / Milestone | Periodo | Historias Incluidas | Estado Funcional (Backend/Lógica) | Estado UI (JavaFX) |
|---|---|---|---|---|
| **Sprint 1 — Acceso y Configuración** | 28/09/2026 – 12/10/2026 | `HU-01`, `HU-02`, `HU-03` | 100% Completado | En desarrollo |
| **Sprint 2 — Participantes** | 12/10/2026 – 26/10/2026 | `HU-04`, `HU-05`, `HU-06` | 100% Completado | En desarrollo |
| **Sprint 3 — Competición** | 26/10/2026 – 09/11/2026 | `HU-07`, `HU-08` | 100% Completado | En desarrollo |
| **Sprint 4 — Información** | 09/11/2026 – 23/11/2026 | `HU-09`, `HU-10` | 100% Completado | En desarrollo |

---

## 7. Pendientes Actuales
- **Único pendiente funcional:** Finalizar el desarrollo de las vistas de la interfaz gráfica en **JavaFX**. Dado que toda la lógica de negocio, las validaciones de las Historias de Usuario, la persistencia en base de datos y los diagramas de arquitectura ya están al 100%, solo resta acoplar el front-end a los controladores ya existentes.

---

## 8. Próximos Pasos
Desarrollar vistas JavaFX → Acoplar interfaz con los servicios de backend → Ejecutar Taller de SonarQube para análisis de calidad → Realizar pruebas de aceptación desde la interfaz → Consolidar entrega final.
