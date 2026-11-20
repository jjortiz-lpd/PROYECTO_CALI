# Herramientas

## Gestión de proyecto
- GitHub
- GitHub Issues
- GitHub Projects (Tablero Kanban / Sprints)

## Control de versiones
- Git
- GitHub (flujo de trabajo con ramas por funcionalidad y Pull Requests hacia `main`)

## Desarrollo
- Java 17 o superior (el proyecto se compila con `release 17`)
- JavaFX 21 (Interfaz gráfica de usuario)
- Maven 3.9 o superior (gestión de dependencias, compilación y ejecución)
- JDBC / Patrón DAO (Capa de acceso a datos, con una clase repositorio por entidad)
- Driver JDBC de PostgreSQL (se descarga automáticamente con Maven)

## Entorno de desarrollo
- Visual Studio Code con el Extension Pack for Java

## Base de datos
- PostgreSQL
- Neon (PostgreSQL gestionado en la nube, usado como base de datos compartida del equipo)
- pgAdmin (opcional, para quien use una instalación local de PostgreSQL)
- Script `db/schema.sql` (crea las tablas y carga los datos de prueba)

## Pruebas
- Recorrido manual con los datos de prueba (descrito en el `README`); el proyecto todavía no tiene pruebas automáticas

## Documentación
- Markdown
- Mermaid (diagramas dentro de los archivos Markdown)
- README
- Documentación del repositorio (`docs/`)
