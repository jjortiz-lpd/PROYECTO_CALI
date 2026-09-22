# TorneoJAV ⚽

**"Centralizando la gestión de torneos para hacer la competencia más organizada, eficiente y accesible."**

---

## Descripción

**TorneoJAV** es un sistema orientado a la gestión y administración de torneos deportivos. La plataforma busca centralizar en un solo sistema las principales actividades relacionadas con la organización de un torneo, incluyendo el registro y administración de equipos y jugadores, configuración del formato de competencia, generación automática del calendario, registro de resultados y consulta de la tabla de posiciones y estadísticas.

El sistema está diseñado para manejar diferentes tipos de usuarios y permisos según su rol. Los principales roles definidos para el sistema son **Administrador, Capitán de equipo y Árbitro/Anotador**.

TorneoJAV surge como respuesta a problemas relacionados con la gestión manual de torneos mediante herramientas como hojas de cálculo y aplicaciones de mensajería, donde la información de equipos, calendarios, resultados y posiciones puede encontrarse dispersa.

La plataforma busca facilitar la administración del torneo y permitir que la información de la competencia se encuentre organizada, actualizada y disponible para los usuarios autorizados.

---

## Equipo del Proyecto

El equipo de **TorneoJAV** se encuentra organizado mediante responsabilidades relacionadas con las diferentes áreas funcionales y técnicas del sistema.

| **Integrante** | **Responsabilidad principal** | **GitHub** |
|---|---|---|
| **Juan José Ortiz** | Persistencia y datos: modelo entidad-relación, DAO/JDBC, conexión con PostgreSQL y scripts de base de datos. | [@jjortiz-lpd](https://github.com/jjortiz-lpd) |
| **Esteban Hernández** | Autenticación y equipos: login por roles, gestión de usuarios, equipos y jugadores. | [PLACEHOLDER] |
| **Natalia Valencia** | Torneos y calendario: creación de torneos, configuración de formatos y generación automática del calendario. | [PLACEHOLDER] |
| **Juan Pablo Franco** | Resultados y GUI: registro de resultados, tabla de posiciones, estadísticas e interfaz gráfica. | [PLACEHOLDER] |

> **Nota:** Los enlaces de GitHub que todavía no estén confirmados se mantienen como `[PLACEHOLDER]`.

---

## Roles del Sistema

TorneoJAV contempla diferentes tipos de usuarios, cada uno con responsabilidades y permisos específicos dentro de la plataforma.

### Administrador

Es responsable de la administración general del torneo.

Entre sus funciones se encuentran:

- Gestionar torneos.
- Configurar el formato del torneo.
- Aprobar o rechazar la inscripción de equipos.
- Generar el calendario.
- Consultar información del torneo.
- Administrar la información correspondiente al torneo.

### Capitán de equipo

Es responsable de administrar la información de su propio equipo.

Entre sus funciones se encuentran:

- Registrar su equipo.
- Consultar la información del equipo.
- Modificar la información permitida.
- Gestionar los jugadores de su equipo.
- Consultar el estado de inscripción del equipo.

### Árbitro / Anotador

Es responsable de registrar la información correspondiente a los partidos.

Entre sus funciones se encuentran:

- Consultar los partidos programados.
- Registrar goles.
- Registrar tarjetas y otros eventos.
- Registrar el resultado final.
- Cerrar el acta del partido.

### Usuario autorizado

Puede consultar la información disponible del torneo, incluyendo:

- Calendario.
- Resultados.
- Tabla de posiciones.
- Estadísticas disponibles.

---

## Historias de Usuario

El alcance funcional inicial de TorneoJAV está definido mediante **10 historias de usuario oficiales**, correspondientes a los Issues **#9 al #18** del proyecto.

| **Issue** | **Historia de Usuario** |
|---|---|
| #9 | Autenticación y control de acceso por roles |
| #10 | Gestión de torneos |
| #11 | Configuración del formato del torneo |
| #12 | Registro y administración de equipos |
| #13 | Gestión de jugadores de un equipo |
| #14 | Aprobación de inscripción de equipos |
| #15 | Generación automática del calendario |
| #16 | Registro de resultados y eventos del partido |
| #17 | Actualización automática de tabla de posiciones |
| #18 | Consulta de calendario, resultados y estadísticas |

Estas historias de usuario definen las funcionalidades principales que debe proporcionar el sistema y sirven como base para los requisitos funcionales, casos de uso y planificación de los Sprints.

---

## Funcionalidades Principales

Las funcionalidades contempladas para la primera versión de TorneoJAV incluyen:

### Autenticación y control de acceso

El sistema permite autenticar usuarios mediante credenciales y determinar las funcionalidades disponibles según el rol del usuario.

### Gestión de torneos

El Administrador puede:

- Crear torneos.
- Consultar torneos.
- Modificar información.
- Eliminar torneos cuando esté permitido.

### Configuración del formato

El Administrador puede seleccionar el formato de competencia.

Los formatos contemplados inicialmente son:

- **Todos contra todos**
- **Eliminación directa**

El formato seleccionado determina las reglas utilizadas posteriormente para generar el calendario.

### Gestión de equipos

Los Capitanes pueden registrar y administrar sus propios equipos.

El sistema debe controlar que un Capitán no pueda modificar equipos pertenecientes a otros usuarios.

### Gestión de jugadores

Los Capitanes pueden administrar los jugadores asociados a su equipo.

La información contemplada incluye:

- Nombre completo.
- Documento.
- Fecha de nacimiento.
- Número de camiseta.

> **Nota:** El número máximo de jugadores `[X]` y el mínimo requerido `[Y]` permanecen pendientes de definición en los requisitos actuales.

### Aprobación de equipos

El Administrador puede revisar las inscripciones pendientes y:

- Aprobar equipos.
- Rechazar equipos.
- Consultar el estado de inscripción.

Los equipos aprobados pueden participar en la generación del calendario.

### Generación automática del calendario

El sistema genera automáticamente los partidos de acuerdo con:

- Equipos aprobados.
- Formato seleccionado.
- Reglas correspondientes al formato.

Para el formato de todos contra todos se generan las parejas de equipos correspondientes.

Para eliminación directa se generan las rondas correspondientes.

### Registro de resultados

El Árbitro/Anotador puede registrar:

- Goles.
- Tarjetas.
- Otros eventos del partido.
- Resultado final.

Una vez cerrado el reporte del partido, se restringen modificaciones no autorizadas.

### Tabla de posiciones

Una vez validado y cerrado el reporte de un partido, el sistema actualiza la tabla de posiciones.

La información contempla:

- Partidos jugados (PJ).
- Partidos ganados (PG).
- Partidos empatados (PE).
- Partidos perdidos (PP).
- Puntos.
- Goles a favor (GF).
- Goles en contra (GC).
- Diferencia de goles.

### 📈 Consulta de información

Los usuarios autorizados pueden consultar:

- Calendario.
- Partidos finalizados.
- Resultados.
- Tabla de posiciones.
- Estadísticas disponibles.

---

## Tecnologías Utilizadas

De acuerdo con la definición técnica actual del proyecto, TorneoJAV utiliza o contempla las siguientes tecnologías:

- **Lenguaje:** Java 17
- **Interfaz gráfica:** JavaFX
- **Base de datos:** PostgreSQL
- **Acceso a datos:** JDBC / DAO
- **Autenticación:** BCrypt
- **Control de versiones:** Git
- **Repositorio:** GitHub
- **Gestión del proyecto:** GitHub Issues / GitHub Projects

> **Nota:** Si durante el desarrollo se incorporan nuevas tecnologías o herramientas, esta sección deberá actualizarse.

---

## Arquitectura del Proyecto

La arquitectura detallada del sistema se encuentra en proceso de definición.

De acuerdo con la planificación actual, se contempla una separación entre:

- Interfaz de usuario.
- Lógica de negocio.
- Acceso a datos.
- Persistencia.
- Base de datos.

**[PLACEHOLDER — Agregar aquí el diagrama de arquitectura cuando esté definido.]**

---

## Estructura del Proyecto

La estructura definitiva del código todavía se encuentra en proceso de consolidación. Por esta razón, se utiliza el siguiente esquema como placeholder para documentar la estructura cuando el desarrollo esté completo:

```text
TorneoJAV/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── [PLACEHOLDER]
│   │   │
│   │   └── resources/
│   │       └── [PLACEHOLDER]
│   │
│   └── test/
│       └── [PLACEHOLDER]
│
├── database/
│   ├── schema.sql
│   └── [PLACEHOLDER]
│
├── docs/
│   ├── README.md
│   ├── canvas.md
│   ├── WBS.md
│   ├── organigrama.md
│   ├── calendario.md
│   ├── plan-sprints.md
│   ├── historias-usuario.md
│   ├── casos-de-uso.md
│   ├── requisitos-funcionales.md
│   ├── requisitos-no-funcionales.md
│   ├── estimacion.md
│   ├── herramientas.md
│   ├── reporte-gerencial.md
│   └── actas.md
│
├── pom.xml
│   └── [PLACEHOLDER — si se utiliza Maven]
│
├── README.md
│   └── Documento principal del proyecto.
│
└── .gitignore
    └── Archivos y carpetas que Git no debe versionar.
```

Nota: La estructura anterior es provisional. Los elementos marcados como [PLACEHOLDER] deberán reemplazarse cuando la estructura real del código esté consolidada.

Base de Datos

TorneoJAV utiliza PostgreSQL como sistema de gestión de base de datos.

La base de datos deberá almacenar la información relacionada con:

Usuarios.
Roles.
Torneos.
Formatos de torneo.
Equipos.
Jugadores.
Inscripciones.
Partidos.
Resultados.
Eventos de partidos.
Tabla de posiciones.
Estadísticas.

[PLACEHOLDER — Agregar aquí el diagrama entidad-relación cuando esté disponible.]

Conexión

El acceso a la base de datos se realizará mediante JDBC, utilizando una capa de acceso a datos basada en DAO.

[PLACEHOLDER — Agregar instrucciones reales de configuración de PostgreSQL.]

Instalación y Ejecución
Requisitos

Los requisitos previstos para ejecutar TorneoJAV son:

Java 17
PostgreSQL
Git
JavaFX
[PLACEHOLDER — herramienta de construcción: Maven/Gradle]
[PLACEHOLDER — versión exacta de PostgreSQL]
[PLACEHOLDER — otras dependencias necesarias]
Clonar el repositorio
git clone https://github.com/jjortiz-lpd/PROYECTO_CALI.git
cd PROYECTO_CALI

Nota: El nombre de la carpeta local puede cambiar dependiendo de la configuración utilizada al clonar el repositorio.

Configuración de la Base de Datos

[PLACEHOLDER]

1. Instalar PostgreSQL.
2. Crear la base de datos de TorneoJAV.
3. Ejecutar el script de creación del esquema.
4. Configurar las credenciales de conexión.
5. Ejecutar la aplicación.

[PLACEHOLDER — Agregar comandos SQL y configuración real cuando estén disponibles.]

Ejecución de la Aplicación

[PLACEHOLDER]

[COMANDO PARA EJECUTAR TORNEOJAV]

La aplicación deberá iniciar la interfaz gráfica de JavaFX y permitir el acceso de los usuarios según el rol correspondiente.

Ejecución de Pruebas

[PLACEHOLDER]

[COMANDO PARA EJECUTAR LAS PRUEBAS]

Las pruebas deberán cubrir las principales funcionalidades del sistema, incluyendo:

Autenticación.
Control de roles.
Gestión de torneos.
Gestión de equipos.
Gestión de jugadores.
Aprobación de equipos.
Generación del calendario.
Registro de resultados.
Actualización de posiciones.
Consulta de información.
Documentación del Proyecto

La documentación de TorneoJAV se encuentra organizada en diferentes documentos que cubren las distintas etapas de planificación y análisis del proyecto.

Documento	Propósito
Lean Canvas	Define el problema, usuarios, propuesta de valor y solución del proyecto.
WBS	Divide el proyecto en componentes y actividades de trabajo.
Organigrama	Define responsabilidades dentro del equipo.
Calendario	Organiza las actividades a lo largo del proyecto.
Plan de Sprints	Define la distribución inicial de las historias de usuario.
Historias de Usuario	Define las necesidades y funcionalidades desde la perspectiva de los usuarios.
Casos de Uso	Describe las interacciones entre usuarios y sistema.
Requisitos Funcionales	Especifica las funciones que debe realizar el sistema.
Requisitos No Funcionales	Define características de calidad y restricciones del sistema.
Estimación	Presenta una estimación inicial del esfuerzo de desarrollo.
Herramientas	Documenta las herramientas utilizadas durante el proyecto.
Reporte Gerencial	Presenta el estado y avance general del proyecto.
Actas	Registra las reuniones y decisiones relacionadas con el proyecto.

Contexto Académico
Proyecto: TorneoJAV
Asignatura: Fundamentos de Ingeniería de Software
Institución: Pontificia Universidad Javeriana
Periodo: [PLACEHOLDER]
Docente: Kerwin de Jesús Barros Somerson
Repositorio

El código fuente y la gestión del proyecto se encuentran en GitHub:

Repositorio:
https://github.com/jjortiz-lpd/PROYECTO_CALI

Contacto

Equipo de desarrollo de TorneoJAV

Juan José Ortiz

Estudiante de Ingeniería de Sistemas
Pontificia Universidad Javeriana

GitHub: @jjortiz-lpd

Esteban Hernández

Estudiante de Ingeniería de Sistemas
Pontificia Universidad Javeriana

GitHub: @estebanalejandrohh-prog

Natalia Valencia

Estudiante de Ingeniería de Sistemas
Pontificia Universidad Javeriana

GitHub: @Nati393

Juan Pablo Franco

Estudiante de Ingeniería de Sistemas
Pontificia Universidad Javeriana

GitHub: @jpfrancor

Licencia

Proyecto desarrollado con fines académicos.

