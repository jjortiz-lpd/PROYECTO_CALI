```mermaid
flowchart LR
    %% =========================================================================
    %% NODO FÍSICO 1: ESTACIÓN DE TRABAJO CLIENTE (CAJA 3D DE ESCRITORIO)
    %% =========================================================================
    subgraph NodoCliente3D ["🧊 «device» Nodo 3D: Estación de Trabajo Cliente (PC Windows 11 / macOS / Linux)"]
        direction TB
        subgraph EntornoJVM3D ["🧊 «executionEnvironment» OpenJDK 17.0.10 LTS + JavaFX 21 SDK"]
            direction TB
            ArtEjecutable[["📦 «artifact»\nTorneoJAV-Client-1.0.jar\n(Vistas FXML, Controladores,\nServicios y Capa DAO)"]]
            ArtDriverJDBC[["📦 «artifact»\npostgresql-42.7.3.jar\n(Driver Oficial JDBC Tipo 4)"]]
            ArtConfiguracion[["📦 «artifact»\napplication.properties\n(db.url, db.user, db.password,\npool.maxConnections=10)"]]
        end
    end

    %% =========================================================================
    %% CANAL DE RED FÍSICA / LÓGICA ENTRE NODOS SEPARADOS
    %% =========================================================================
    CanalRed{{"🌐 Red LAN / Cloud VPN\nProtocolo: TCP/IP (JDBC Wire Protocol v3)\nPuerto: 5432 | Cifrado TLS 1.3"}}

    %% =========================================================================
    %% NODO FÍSICO 2: SERVIDOR DEDICADO DE BASE DE DATOS (SEPARADO)
    %% =========================================================================
    subgraph NodoServidorBD3D ["🧊 «device» Nodo 3D Separado: Servidor de Base de Datos (Ubuntu Server 24.04 LTS / Docker Host)"]
        direction TB
        subgraph EntornoPostgres3D ["🧊 «executionEnvironment» Motor RDBMS PostgreSQL 16.4 (Puerto TCP 5432)"]
            direction TB
            ArtSchemaDDL[["📦 «artifact»\nschema_torneojav.sql\n(DDL: 9 Tablas, PKs, FKs,\nÍndices y Checks de Dominio)"]]
            ArtSeedSQL[["📦 «artifact»\nseed_datos_iniciales.sql\n(DML: Roles y Usuarios Base:\nAdmin, Capitán, Árbitro)"]]
            VolumenDatos[("🗄️ «database»\nCluster: torneojav_prod_db\n(Almacenamiento Transaccional ACID\nen Volumen Persistente Separado)")]
        end
    end

    %% Relaciones internas y de red
    ArtEjecutable -->|lee parámetros| ArtConfiguracion
    ArtEjecutable -->|invoca API| ArtDriverJDBC
    ArtDriverJDBC ===>|"1. Petición SQL / PreparedStatement\n(TCP :5432)"| CanalRed
    CanalRed ===>|"2. Respuesta ResultSet / Commit\n(TCP :5432)"| EntornoPostgres3D
    ArtSchemaDDL -->|estructura tablas en| VolumenDatos
    ArtSeedSQL -->|inicializa catálogos en| VolumenDatos

    %% Estilos de Cajas con Borde Volumétrico 3D
    style NodoCliente3D fill:#dbe4ff,stroke:#1c7ed6,stroke-width:4px
    style EntornoJVM3D fill:#edf2ff,stroke:#364fc7,stroke-width:2px
    style NodoServidorBD3D fill:#ffd8a8,stroke:#d9480f,stroke-width:4px
    style EntornoPostgres3D fill:#fff4e6,stroke:#e8590c,stroke-width:2px
    style VolumenDatos fill:#ffffff,stroke:#c92a2a,stroke-width:3px
    style CanalRed fill:#e6fcf5,stroke:#0ca678,stroke-width:2px
    ```
