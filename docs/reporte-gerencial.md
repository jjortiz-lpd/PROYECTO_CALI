# Reporte Gerencial — TorneoJAV

## 1. Información General del Proyecto
- **Proyecto:** TorneoJAV
- **Asignatura:** Fundamentos de Ingeniería de Software (`FIS_2630`)
- **Institución:** Pontificia Universidad Javeriana
- **Docente:** Kerwin de Jesús Barros Somerson
- **Repositorio:** https://github.com/jjortiz-lpd/PROYECTO_CALI

## 2. Objetivo
Centralizar la gestión de torneos deportivos, usuarios y roles, inscripción de equipos, plantillas de jugadores, programación automática de partidos, registro de resultados y actas arbitrales, tablas de posiciones y consultas de estadísticas en una sola plataforma.

## 3. Equipo de Trabajo y Roles
| Integrante | Rol de Gestión / Ingeniería | Responsabilidad Técnica Principal |
|---|---|---|
| **Juan José Ortiz** | Configuration Manager | Modelo de datos (E-R), persistencia, capa DAO/JDBC y PostgreSQL. |
| **Esteban Hernández** | DevOps Engineer | Autenticación por roles, gestión de usuarios, equipos y jugadores. |
| **Natalia Valencia** | Product Owner + QA Lead | Gestión de torneos, formatos, inscripciones y generación del calendario. |
| **Juan Pablo Franco** | Scrum Master + Sprint Planner | Resultados, actas arbitrales, tabla de posiciones, estadísticas e interfaz gráfica (JavaFX). |

## 4. Alcance Documental y Arquitectónico Consolidado
El repositorio cuenta con la totalidad de los artefactos de planificación, requisitos y diseño arquitectónico estructurados en `docs/` y en la Wiki del proyecto:

### Documentación de Gestión e Ingeniería (`docs/`)
1. **Lean Canvas (`canvas.md`):** Propuesta de valor, problema, segmentos de clientes y solución.
2. **WBS (`WBS.md`):** Estructura de Desglose del Trabajo en 9 paquetes funcionales y de calidad.
3. **Organigrama (`organigrama.md`):** Roles de ingeniería y distribución de responsabilidades.
4. **Calendario de Actividades (`calendario.md`):** Cronograma general de 12 semanas alineado con los Milestones.
5. **Planeación Ágil (`planeacion-agil.md`):** Marco iterativo, flujo funcional de 9 etapas y criterio de terminado (*Definition of Done*).
6. **Definición de Sprints (`plan-sprints.md`):** Distribución de las 10 Historias de Usuario en 4 Sprints de 2 semanas.
7. **Historias de Usuario (`historias-usuario.md`):** Especificación de `HU-01` a `HU-10` (Issues `#9` al `#18`) con criterios de aceptación cerrados.
8. **Casos de Uso (`casos-de-uso.md`):** Modelado de interacciones (`CU-01` a `CU-10`) y dependencias entre actores y sistema.
9. **Requisitos Funcionales (`requisitos-funcionales.md`):** 33 requisitos funcionales (`RF-01` a `RF-33`) trazados con las Historias de Usuario.
10. **Requisitos No Funcionales (`requisitos-no-funcionales.md`):** 6 atributos de calidad (`RNF-01` a `RNF-06`: seguridad, integridad, persistencia, usabilidad, mantenibilidad y control de acceso).
11. **Estimación (`estimacion.md`):** Dimensionamiento del backlog en 55 Story Points (SP).
12. **Herramientas (`herramientas.md`):** Stack tecnológico confirmado (Java 17, JavaFX, JDBC/DAO, PostgreSQL, Git y GitHub Projects).
13. **Reporte Gerencial (`reporte-gerencial.md`):** Seguimiento ejecutivo de alcance, métricas, decisiones y estado de los Sprints.
14. **Actas de Reunión (`actas.md`):** Registro histórico de sesiones, decisiones y control de tareas del equipo.

### Modelos de Diseño Arquitectónico (`docs/diagramas/`)
15. **Diagrama de Análisis ECB (`diagrama-ecb.md`):** Separación de responsabilidades en estereotipos `«boundary»`, `«control»` y `«entity»` para las historias críticas (`HU-07`, `HU-08`, `HU-09` y `HU-05`).
16. **Diagrama de Clases (`diagrama-clases.md`):** Modelo estructural del dominio con relaciones UML, multiplicidades y aplicación explícita de los 5 principios **SOLID**.
17. **Diagrama de Componentes (`diagrama-componentes.md`):** Arquitectura modular por capas con interfaces provistas y requeridas hacia PostgreSQL.
18. **Diagrama de Despliegue (`diagrama-despliegue.md`):** Infraestructura física y lógica cliente-servidor (OpenJDK 17 + JavaFX comunicándose vía TCP/IP JDBC puerto `5432` con PostgreSQL 16).

## 5. Reglas de Negocio Definidas y Cerradas
Se resolvieron la totalidad de las definiciones funcionales que se encontraban abiertas al inicio del proyecto:
- **Límites de plantilla (`HU-05`):** Mínimo de **5 jugadores** (`[Y] = 5`) y máximo de **15 jugadores** (`[X] = 15`) por equipo.
- **Rango de edad (`HU-05`):** Jugadores entre **16 y 45 años cumplidos**, validados mediante su fecha de nacimiento.
- **Criterios de ordenamiento y desempate (`HU-09`):** Actualización automática al cerrar el acta arbitral ordenando por: **1.º Mayor puntaje (`PTS`) → 2.º Mayor diferencia de goles (`DG`) → 3.º Mayor cantidad de goles a favor (`GF`)**.
- **Alcance de estadísticas (`HU-10`):** (1) Resumen de partidos jugados/programados, total de goles y promedio de goles por partido, (2) Equipo más goleador (mayor `GF`), (3) Valla menos vencida (menor `GC`) y (4) Balance disciplinario de tarjetas amarillas y rojas.

## 6. Estado de Avance por Sprint y Métricas del Backlog
El esfuerzo total del proyecto es de **55 Story Points (SP)** distribuidos en **10 Historias de Usuario** (media de **5.50 SP**, mediana de **5.00 SP**, desviación estándar de **1.90 SP**, mínimo de **3 SP** y máximo de **8 SP`):

| Sprint / Milestone | Fecha Límite | Historias | Esfuerzo (SP) | Avance de Issues | SP Completados | Estado Actual |
|---|---|---|---:|---|---|---|
| **Sprint 1 — Acceso y Configuración** | 12/10/2026 | `HU-01`, `HU-02`, `HU-03` | 13 SP | 66.7% (2/3 cerradas) | 10 / 13 SP (76.9%) | En cierre (`HU-01` y `HU-02` en `Done` vía PR `#30` y `#31`; `HU-03` en `Ready`) |
| **Sprint 2 — Participantes** | 26/10/2026 | `HU-04`, `HU-05`, `HU-06` | 16 SP | 0.0% (0/3 cerradas) | 0 / 16 SP (0.0%) | En preparación (`Backlog`) |
| **Sprint 3 — Competición** | 09/11/2026 | `HU-07`, `HU-08` | 16 SP | 0.0% (0/2 cerradas) | 0 / 16 SP (0.0%) | Planificado (`Backlog`) |
| **Sprint 4 — Información** | 23/11/2026 | `HU-09`, `HU-10` | 10 SP | 0.0% (0/2 cerradas) | 0 / 10 SP (0.0%) | Planificado (`Backlog`) |
| **Total Proyecto** | **07/12/2026** | **10 Historias** | **55 SP** | **20.0% (2/10)** | **10 / 55 SP (18.2%)** | **En ejecución** |

## 7. Pendientes Actuales
- Completar e integrar la **HU-03 (Configuración del formato del torneo)** para cerrar al 100% el Milestone del **Sprint 1**.
- Sincronizar en el tablero de GitHub Projects los Story Points (`Estimate`), tallas (`Size`) y asignaciones (`Assignees`) conforme a la línea base de `estimacion.md` y `organigrama.md`.
- Vincular y cerrar los 4 sub-issues del **Taller de Diseño** (`ECB`, `Clases`, `Componentes` y `Despliegue`) mediante sus commits en `docs/diagramas/`.
- Iniciar la implementación e integración de las historias del **Sprint 2 (`HU-04`, `HU-05`, `HU-06`)** con fecha meta del **26/10/2026**.

## 8. Próximos Pasos
Cerrar `HU-03` (Sprint 1) → Subir diagramas de arquitectura a `docs/diagramas/` con sus commits asociados → Ejecutar e integrar el Sprint 2 (`HU-04` a `HU-06`) → Validar criterios de aceptación y pruebas de persistencia en PostgreSQL.
