# Informe Postmortem — Proyecto TorneoJAV

## 1. Información General
- **Proyecto:** TorneoJAV
- **Asignatura:** Fundamentos de Ingeniería de Software (`FIS_2630`)
- **Institución:** Pontificia Universidad Javeriana
- **Periodo:** 3
- **Docente:** Kerwin de Jesús Barros Somerson
- **Fecha de elaboración:** 18/11/2026
- **Repositorio:** https://github.com/jjortiz-lpd/PROYECTO_CALI
- **Autores (Equipo de Proyecto):**
  - **Juan José Ortiz** — Configuration Manager (`@jjortiz-lpd`)
  - **Esteban Hernández** — DevOps Engineer (`@estebanalejandrohh-prog`)
  - **Natalia Valencia** — Product Owner + QA Lead (`@Nati393`)
  - **Juan Pablo Franco** — Scrum Master + Sprint Planner (`@jpfrancor`)

---

## 2. Resumen Ejecutivo
**TorneoJAV** cumplió el **100% de su alcance funcional y arquitectónico**, completando las **10 Historias de Usuario (`HU-01` a `HU-10`)**, los **33 Requisitos Funcionales (`RF-01` a `RF-33`)**, los **6 Requisitos No Funcionales (`RNF-01` a `RNF-06`)** y los **55.0 Story Points (SP)** planificados, con un producto ejecutable de forma portable mediante **Apache Maven** y validado integralmente en **Azure DevOps**.

Aunque al inicio del semestre el equipo presentó una demora en el arranque de la codificación —llegando a la primera entrega únicamente con artefactos teóricos y sin avance de backend—, una reorganización temprana y una arquitectura en capas bien ejecutada permitieron recuperar el ritmo y finalizar tanto el backend como la interfaz gráfica en **JavaFX** el **18/11/2026**, **5 días antes** del cierre del Sprint 4 (`23/11/2026`) y **19 días antes** de la entrega final de la Semana 12 (`07/12/2026`). Los incidentes técnicos ocurridos (configuración inicial de Maven y un bloqueo administrativo de permisos en Azure DevOps) tuvieron bajo impacto y no comprometieron el cronograma.

---

## 3. Comparativa: Planificado vs. Ejecutado

| Dimensión | Línea Base Planificada | Resultado Real Obtenido | Balance / Desviación |
|---|---|---|---|
| **Alcance funcional** | 10 Historias de Usuario (`#9` a `#18`), 10 Casos de Uso, 33 RF y 6 RNF | 10/10 HU, 33/33 RF y 6/6 RNF implementados e integrados | **100% cumplido** |
| **Esfuerzo (Story Points)** | 55.0 SP totales (promedio de 13.75 SP por Sprint) | 55.0/55.0 SP completados tras corregir el desfase inicial del Kanban (48 SP → 55 SP) | **100% ejecutado** |
| **Cronograma de desarrollo** | 65–70 días de ejecución (14/09/2026 al cierre del Sprint 4 el 23/11/2026) | Producto completo, GUI acoplada y pruebas ejecutadas en **65 días** (`18/11/2026`) | **5 días de anticipación** frente al cierre del Sprint 4 |
| **Arquitectura y construcción** | Java 17, JavaFX, JDBC/DAO y PostgreSQL (gestor de build sin cerrar al inicio) | Empaquetado y ejecución portable con **Apache Maven** (`pom.xml`) en cualquier máquina | **Superado** (portabilidad garantizada) |
| **Aseguramiento de calidad (QA)** | Pruebas de criterios de aceptación y análisis estático | Casos de prueba estructurados ejecutados al 100% en **Azure DevOps** + análisis con **SonarQube** | **Cumplido** (1 día de demora en permisos, sin afectar entregas) |

---

## 4. Línea de Tiempo de Eventos Clave

| Fecha / Etapa | Hito o Evento | Impacto en el Proyecto |
|---|---|---|
| **14/09/2026 – 22/09/2026** (Semanas 1–2) | Cierre de la Primera Entrega exclusivamente con documentación teórica (`docs/`), sin inicio de codificación del backend. | Generó presión inicial sobre el Sprint 1 y postergó la validación temprana del stack técnico. |
| **28/09/2026 – 12/10/2026** (Sprint 1) | Arranque formal del desarrollo y reorganización del equipo; cierre inicial de `HU-01` y `HU-02`, con leve rezago en `HU-03`. | Evidenció la necesidad de sincronizar estimaciones, cerrar reglas de negocio pendientes y estandarizar el tablero Kanban. |
| **12/10/2026 – 09/11/2026** (Sprints 2–3) | Consolidación de reglas de negocio (`HU-05`, `HU-09`, `HU-10`), actualización de diagramas arquitectónicos (`ECB`, `Clases`, `Componentes`, `Despliegue`, `Procesos`, `Secuencia`, `Patrones`) e implementación completa del backend y persistencia en PostgreSQL. | Dejó toda la lógica de negocio y la capa DAO/JDBC estable y lista para consumo. |
| **Noviembre 2026** (Cierre Sprint 4) | Configuración de **Apache Maven**, desarrollo e integración completa de la interfaz gráfica en **JavaFX** y ejecución de pruebas en **Azure DevOps**. | Fricción menor con descarga/compilación de Maven y bloqueo de 1 día por permisos administrativos en la base de datos de Azure DevOps; ambos resueltos sin afectar fechas límite. |
| **18/11/2026** | Cierre técnico del proyecto: aplicación funcional, ejecutable con Maven y validada en pruebas. | Entrega interna anticipada con margen suficiente para consolidar documentación y preparar la sustentación final. |

---

## 5. Qué Salió Bien (Fortalezas y Aciertos)

1. **Calidad y desacoplamiento del Backend:** La implementación de la lógica de negocio, las validaciones de dominio y la capa de persistencia (`DAO / JDBC` con PostgreSQL) respetó estrictamente la separación por capas y los contratos diseñados. Gracias a esto, acoplar la interfaz gráfica en **JavaFX** fue un proceso limpio y rápido: el frontend funcionó directamente como una capa visual ("carcasa") sobre los servicios existentes, **sin necesidad de reescribir el backend**.
2. **Capacidad de reorganización y recuperación del cronograma:** Tras detectar el retraso inicial en el comienzo de la codificación, el equipo ajustó su ritmo de trabajo, dividió responsabilidades con claridad y logró terminar tanto el backend como el frontend días antes de la fecha límite del Sprint 4.
3. **Estandarización de la construcción con Apache Maven:** Migrar y consolidar el proyecto bajo Maven resolvió la gestión de dependencias (JavaFX, driver JDBC de PostgreSQL) y aseguró que **TorneoJAV** pueda compilarse y ejecutarse de forma reproducible en cualquier equipo.
4. **Cierre efectivo de pruebas en Azure DevOps:** Una vez resuelto el acceso administrativo, el equipo ejecutó la totalidad de los casos de prueba planificados sin incidentes funcionales críticos, validando los criterios de aceptación de punta a punta.
5. **Trazabilidad documental y arquitectónica:** La documentación en `docs/`, los 7 diagramas UML/Mermaid y la Wiki del repositorio se mantuvieron actualizados y alineados con la implementación real.

---

## 6. Qué Salió Mal y Análisis de Causa Raíz

### 6.1 Arranque tardío de la codificación (Procrastinación inicial)
- **Descripción:** Durante las primeras semanas el equipo se enfocó únicamente en los documentos teóricos y postergó la creación del código base, llegando a la Primera Entrega sin ningún avance de backend.
- **Causa raíz:** Falta de un hito técnico mínimo (*walking skeleton* o prueba de concepto de conexión Java–PostgreSQL) asociado a la primera fase y ausencia de un entorno de compilación configurado desde la Semana 1.
- **Impacto:** Sobrecarga temporal al iniciar el Sprint 1, mitigada posteriormente gracias a la reorganización del equipo.

### 6.2 Fricción en la configuración inicial de Apache Maven
- **Descripción:** Durante el empaquetado e integración se presentaron inconvenientes al descargar Maven y algunos errores puntuales de compilación local.
- **Causa raíz:** El gestor de dependencias (`pom.xml`) no se definió ni validó al comienzo del proyecto en todos los equipos de desarrollo, dejando abierta esa decisión durante los primeros Sprints (`[PLACEHOLDER]` en el `README.md` inicial).
- **Impacto:** Bajo. Se resolvió internamente sin alterar el calendario de entregas.

### 6.3 Bloqueo temporal de permisos en Azure DevOps
- **Descripción:** La ejecución de pruebas sufrió un retraso aproximado de **1 día** debido a que el equipo aún no contaba con permisos administrativos sobre la base de datos en **Azure DevOps**.
- **Causa raíz:** Validación tardía de credenciales y permisos de infraestructura externa justo al momento de iniciar la fase de pruebas, en lugar de solicitarlos con anticipación durante el Sprint anterior.
- **Impacto:** Mínimo (1 día absorbido dentro del margen de holgura del Sprint 4). Todas las pruebas se ejecutaron exitosamente en cuanto se concedió el acceso.

### 6.4 Desincronización temprana de artefactos de gestión y reglas de negocio
- **Descripción:** Al cierre de la Primera Entrega quedaron parámetros sin definir en `historias-usuario.md` (`[X]`, `[Y]`, rango de edad, criterios de desempate y estadísticas) y diferencias entre los Story Points de `estimacion.md` (55 SP) y el tablero Kanban (48 SP).
- **Causa raíz:** Ausencia de un *Definition of Ready (DoR)* formal antes de mover historias a desarrollo y falta de auditoría periódica entre los archivos Markdown y GitHub Projects.
- **Impacto:** Medio en gestión documental; requirió una jornada dedicada de saneamiento y unificación a mitad del proyecto.

---

## 7. Gestión de Calidad y Deuda Técnica
- **Pruebas funcionales (Azure DevOps):** Se verificaron los flujos críticos de autenticación por rol (`HU-01`), validaciones de campos obligatorios (`HU-02`), reglas de plantilla de 5 a 15 jugadores y edad de 16 a 45 años (`HU-05`), verificación de formato previo al calendario (`HU-07`), inmutabilidad de actas cerradas (`HU-08`) y desempate `PTS → DG → GF` (`HU-09`), logrando resultados exitosos tras habilitar los permisos en el entorno. En total se realizaron 45 pruebas.
- **Análisis estático y deuda técnica:** Los aspectos de mantenibilidad identificados durante el desarrollo quedaron trazados en `docs/registro-de-deuda.md` y evaluados bajo el enfoque de análisis de calidad con **SonarQube** (foco en cierre seguro de recursos JDBC, manejo de excepciones y complejidad en controladores).
- **Integración horizontal vs. vertical:** Si bien el desarrollo en capas horizontales (backend completo primero, interfaz JavaFX al final) funcionó muy bien en este proyecto por la solidez del backend, dejó concentrada toda la integración visual en el tramo final; un fallo en los contratos del backend habría puesto en riesgo la entrega.

---

## 8. Lecciones Aprendidas y Plan de Acción para Futuros Proyectos

| Área | Lección Aprendida | Acción Concreta a Aplicar |
|---|---|---|
| **Inicio del proyecto** | Retrasar el primer commit de código comprime innecesariamente los primeros Sprints. | Construir un *walking skeleton* (repositorio con Maven configurado, clase principal y conexión de prueba a BD) desde la **Semana 1**, en paralelo a la documentación inicial. |
| **Arquitectura** | Un backend bien modularizado con interfaces claras y patrón DAO reduce drásticamente el esfuerzo de integrar la interfaz gráfica. | Mantener la separación estricta en capas (`Presentación → Servicios → DAO → BD`) y acordar los contratos de métodos antes de codificar. |
| **Infraestructura y QA** | Los bloqueos por permisos en plataformas externas (como Azure DevOps) son evitables si se verifican antes de la ventana de pruebas. | Incluir una tarea obligatoria de **"Verificación de accesos, permisos y entornos"** al menos una semana antes de iniciar cada ciclo de pruebas. |
| **Gestión Ágil** | Las historias con variables indefinidas (`[X]`, `[Y]`) y los tableros desincronizados generan retrabajo documental. | Aplicar un *Definition of Ready* estricto (ninguna HU entra a Sprint con placeholders) y automatizar el cierre de Issues mediante palabras clave (`Closes #ID`) en cada Pull Request. |
| **Estrategia de entrega** | Entregar toda la UI al final concentra el riesgo de integración aunque el backend esté terminado. | Priorizar **incrementos verticales** (persistencia + lógica + vista JavaFX + prueba por cada historia) al cierre de cada Sprint. |

---

## 9. Conclusión
El balance final de **TorneoJAV** es altamente positivo. A pesar de un inicio lento en la codificación y de contratiempos menores con Maven y los permisos en Azure DevOps, la solidez técnica del backend, la buena coordinación del equipo y la adopción de prácticas formales de arquitectura y pruebas permitieron entregar un sistema completo, portable y funcional antes de la fecha límite establecida.
