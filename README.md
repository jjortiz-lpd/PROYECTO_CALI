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
| **Esteban Hernández** | Autenticación y equipos: login por roles, gestión de usuarios, equipos y jugadores. | @estebanalejandrohh-prog |
| **Natalia Valencia** | Torneos y calendario: creación de torneos, configuración de formatos y generación automática del calendario. | @Nati393 |
| **Juan Pablo Franco** | Resultados y GUI: registro de resultados, tabla de posiciones, estadísticas e interfaz gráfica. | @jpfrancor |

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

---

## Base de Datos

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

---

## Conexión

El acceso a la base de datos se realizará mediante JDBC, utilizando una capa de acceso a datos basada en DAO.

[PLACEHOLDER — Agregar instrucciones reales de configuración de PostgreSQL.]
---
## Instalación y Ejecución
### Requisitos

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
---
## Configuración de la Base de Datos

[PLACEHOLDER]

1. Instalar PostgreSQL.
2. Crear la base de datos de TorneoJAV.
3. Ejecutar el script de creación del esquema.
4. Configurar las credenciales de conexión.
5. Ejecutar la aplicación.

[PLACEHOLDER — Agregar comandos SQL y configuración real cuando estén disponibles.]

---
## Ejecución de la Aplicación

[PLACEHOLDER]

[COMANDO PARA EJECUTAR TORNEOJAV]

La aplicación deberá iniciar la interfaz gráfica de JavaFX y permitir el acceso de los usuarios según el rol correspondiente.

---
## Ejecución de Pruebas

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

---
La documentación de TorneoJAV se encuentra organizada en diferentes documentos que cubren las distintas etapas de planificación y análisis del proyecto.

* Documento:	Propósito
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
---
## Contexto Académico
Proyecto: TorneoJAV
Asignatura: Fundamentos de Ingeniería de Software
Institución: Pontificia Universidad Javeriana
Periodo: 3
Docente: Kerwin de Jesús Barros Somerson
---
## Repositorio

El código fuente y la gestión del proyecto se encuentran en GitHub:

Repositorio:
https://github.com/jjortiz-lpd/PROYECTO_CALI
---
## Contacto

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
---

## Licencia

Proyecto desarrollado con fines académicos.

