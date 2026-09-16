# Organigrama y roles

## Equipo de desarrollo

```text
                    EQUIPO TORNEOJAV
                           |
        +------------------+------------------+
        |                  |                  |
 Persistencia        Autenticación      Torneos y
   y datos              y equipos         calendario
        |                  |                  |
 Juan José Ortiz    Esteban Hernández   Natalia Murgueitio
        |
        +--------------------------+
                                   |
                            Resultados y GUI
                                   |
                            Juan Pablo Franco
```

## Responsabilidades

| Integrante | Responsabilidad |
|---|---|
| Juan José Ortiz | Modelo entidad-relación, DAO/JDBC, PostgreSQL y scripts |
| Esteban Hernández | Autenticación, usuarios, equipos y jugadores |
| Natalia Murgueitio | Torneos, formatos y generación del fixture |
| Juan Pablo Franco | Resultados, posiciones, estadísticas e interfaz JavaFX |

## Roles dentro del sistema

### Administrador
- Crear y configurar torneos.
- Definir formato.
- Aprobar equipos.

### Capitán
- Inscribir equipo.
- Gestionar jugadores.
- Consultar calendario y posiciones.

### Árbitro / anotador
- Registrar resultados.
- Registrar goles y tarjetas.
- Cerrar actas.
