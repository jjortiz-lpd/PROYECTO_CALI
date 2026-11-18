# Reporte Gerencial — TorneoJAV (Versión de Cierre)

## 1. Información General del Proyecto
- **Proyecto:** TorneoJAV
- **Asignatura:** Fundamentos de Ingeniería de Software (`FIS_2630`)
- **Institución:** Pontificia Universidad Javeriana
- **Periodo:** 3
- **Docente:** Kerwin de Jesús Barros Somerson
- **Fecha de corte:** 18/11/2026
- **Estado general:** **Completado (100% funcional, ejecutable con Maven y validado en Azure DevOps)**
- **Repositorio:** https://github.com/jjortiz-lpd/PROYECTO_CALI

---

## 2. Objetivo
Centralizar la gestión de torneos deportivos, usuarios y control de acceso por roles, inscripción de equipos, administración de plantillas de jugadores, generación automática de calendarios, registro de resultados y actas arbitrales, actualización de tablas de posiciones y consulta de estadísticas en una sola plataforma de escritorio portable.

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
11. **Estimación (`estimacion.md`):** Dimensionamiento del esfuerzo en 55.0 Story Points (SP) sincronizado con GitHub Projects.
12. **Herramientas (`herramientas.md`):** Stack tecnológico consolidado (Java 17, JavaFX, **Apache Maven**, JDBC/DAO, PostgreSQL 16, Git, GitHub Projects y **Azure DevOps** para ejecución de pruebas).
13. **Reporte Gerencial (`reporte-gerencial.md`):** Estado ejecutivo de cierre, métricas de cumplimiento por Sprint y evidencia de calidad.
14. **Actas de Reunión (`actas.md`):** Consolidado de las sesiones de seguimiento, acuerdos y decisiones del equipo.
15. **Registro de Deuda Técnica (`registro-de-deuda.md`):** Bitácora de deudas técnicas identificadas y gestionadas durante la codificación.
16. **Retrospectiva (`retrospectiva.md`):** Análisis de lecciones aprendidas y oportunidades de mejora del proceso ágil.

### 4.2 Modelos de Diseño Arquitectónico Completados (`docs/diagramas/`)
La arquitectura del sistema fue modelada, sincronizada con el código final y cerrada en su totalidad (sub-issues `#32` al `#35` y diagramas complementarios):

| Artefacto en `docs/diagramas/` | Responsable | Contenido Arquitectónico Modelado |
|---|---|---|
| `diagrama-ecb.md` | Natalia Valencia | **Diagrama de Análisis (ECB):** Separación en estereotipos `«boundary»` (vistas JavaFX), `«control»` (gestores de negocio) y `«entity»` (entidades persistentes). |
| `diagrama-clases.md` | Juan José Ortiz | **Diagrama de Clases y SOLID:** Jerarquía de `Usuario`, entidades nucleares, patrones estrategia (`IGeneradorCalendario`, `ICriterioDesempate`), relaciones UML alineadas al código y principios SOLID. |
| `diagrama-componentes.md` | Juan Pablo Franco | **Diagrama de Componentes:** Arquitectura en 4 capas (Presentación JavaFX, 6 módulos de Negocio en Java 17, `CapaPersistenciaDAO` y motor externo PostgreSQL) con interfaces provistas y requeridas. |
| `diagrama-despliegue.md` | Esteban Hernández | **Diagrama de Despliegue:** Topología cliente-servidor en cajas 3D entre `Estación de Trabajo Cliente` (OpenJDK 17 + JavaFX + Maven) y `Servidor de Base de Datos` (`PostgreSQL 16`, `schema.sql`) vía `TCP/IP (JDBC)` puerto `5432`. |
| `diagrama-procesos.md` | Equipo de Proyecto | **Diagrama de Procesos (Swimlanes):** Flujo completo de las 10 HU distribuidas en carriles por actor, Sistema Java y Base de Datos separada. |
| `diagrama-secuencia.md` | Equipo de Proyecto | **Diagrama de Secuencia:** Interacciones cronológicas y llamadas a métodos agrupadas en carriles desde la interfaz JavaFX hasta PostgreSQL. |
| `diagrama-patrones.md` | Equipo de Proyecto | **Patrones de Diseño:** Estructura UML de los patrones implementados (Factory Method, Strategy, Observer, DAO y Singleton). |

---

## 5. Reglas de Negocio Implementadas y Verificadas
La totalidad de las reglas de negocio definidas fueron implementadas en backend, integradas en la interfaz JavaFX y validadas en pruebas:
- **Límites de plantilla (`HU-05`):** Mínimo de **5 jugadores** (`minJugadoresEquipo = 5`) y máximo de **15 jugadores** (`maxJugadoresEquipo = 15`) por equipo.
- **Rango de edad (`HU-05`):** Jugadores entre **16 y 45 años cumplidos** (`edadMinima = 16`, `edadMaxima = 45`), validados con su fecha de nacimiento.
- **Criterios de desempate en tabla de posiciones (`HU-09`):** Recálculo automático al cerrar un acta en `HU-08`, ordenando por: **1.º Mayor puntaje (`PTS`) → 2.º Mayor diferencia de goles (`DG = GF - GC`) → 3.º Mayor cantidad de goles a favor (`GF`)**.
- **Estadísticas disponibles del torneo (`HU-10`):** (1) Resumen de partidos jugados/programados, total de goles y promedio de goles por partido, (2) Equipo más goleador (mayor `GF`), (3) Valla menos vencida (menor `GC`) y (4) Balance disciplinario de tarjetas amarillas y rojas registradas.

---

## 6. Estado Final de Ejecución por Sprint
El desarrollo del software culminó al **100%** antes de la fecha límite del Sprint 4 (`23/11/2026`), completando las **10 Historias de Usuario** y los **55.0 Story Points** en todas sus capas (base de datos, lógica de negocio, interfaz gráfica JavaFX y pruebas):

| Sprint / Milestone | Periodo | Historias Incluidas | Esfuerzo (SP) | Backend / Persistencia | Interfaz Gráfica (JavaFX) | Estado Final |
|---|---|---|---:|---|---|---|
| **Sprint 1 — Acceso y Configuración** | 28/09/2026 – 12/10/2026 | `HU-01`, `HU-02`, `HU-03` | 13.0 SP | 100% Completado | 100% Completado | **Terminado (`Done`)** |
| **Sprint 2 — Participantes** | 12/10/2026 – 26/10/2026 | `HU-04`, `HU-05`, `HU-06` | 16.0 SP | 100% Completado | 100% Completado | **Terminado (`Done`)** |
| **Sprint 3 — Competición** | 26/10/2026 – 09/11/2026 | `HU-07`, `HU-08` | 16.0 SP | 100% Completado | 100% Completado | **Terminado (`Done`)** |
| **Sprint 4 — Información** | 09/11/2026 – 23/11/2026 | `HU-09`, `HU-10` | 10.0 SP | 100% Completado | 100% Completado | **Terminado (`Done`)** |
| **Total Acumulado** | **14/09/2026 – 18/11/2026** | **10 / 10 Historias (100%)** | **55.0 / 55.0 SP (100%)** | **100%** | **100%** | **Producto Finalizado** |

---

## 7. Portabilidad, Despliegue y Aseguramiento de Calidad (QA)
- **Construcción y portabilidad con Apache Maven:** El proyecto cuenta con gestión automatizada de dependencias y ciclo de vida en `pom.xml`, garantizando su compilación y ejecución reproducible en cualquier máquina con JDK 17+ y Maven sin configuraciones manuales de librerías externas.
- **Integración completa de la GUI:** Las vistas de **JavaFX** se encuentran completamente desarrolladas y acopladas a los servicios de negocio y control de acceso por roles (`Administrador`, `Capitán`, `Árbitro`).
- **Validación en Azure DevOps:** Se ejecutaron formalmente los planes y casos de prueba del proyecto en **Azure DevOps**, verificando el cumplimiento de los criterios de aceptación (`HU-01` a `HU-10`), validaciones de dominio, integridad de datos y cierre inmutable de actas.

---

## 8. Estado de Pendientes y Cierre Administrativo
- **Pendientes técnicos / funcionales:** **Ninguno.** El 100% del código, la interfaz gráfica, la base de datos, los diagramas de diseño y las pruebas en Azure DevOps están completados.
- **Acciones de cierre formal (Semanas 11 y 12):**
  - Verificar que todos los Issues (`#9` al `#18`) y Milestones en GitHub figuren en estado `Closed` / `Done`.
  - Preparar la demostración en vivo y sustentación final del proyecto.
