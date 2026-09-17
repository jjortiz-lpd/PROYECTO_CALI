# Casos de uso

| ID | Caso de uso | Actor principal | HU |
|---|---|---|---|
| CU-01 | Iniciar sesión | Usuario | HU-01 |
| CU-02 | Gestionar torneo | Administrador | HU-02 |
| CU-03 | Configurar formato | Administrador | HU-03 |
| CU-04 | Gestionar equipo | Capitán | HU-04 |
| CU-05 | Gestionar jugadores | Capitán | HU-05 |
| CU-06 | Aprobar/rechazar inscripción | Administrador | HU-06 |
| CU-07 | Generar calendario | Administrador | HU-07 |
| CU-08 | Registrar resultado y eventos | Árbitro | HU-08 |
| CU-09 | Actualizar tabla de posiciones | Sistema | HU-09 |
| CU-10 | Consultar información del torneo | Usuario autorizado | HU-10 |

## Relaciones
HU-07 depende de que exista un formato configurado y utiliza equipos aprobados.
HU-09 se ejecuta como consecuencia del cierre de un acta de HU-08.
HU-10 consulta la información producida por las funcionalidades anteriores.
