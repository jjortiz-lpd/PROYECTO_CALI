## Registro de Deuda Técnica — TorneoJAV
Registro de los ítems de deuda técnica identificados en el código y la documentación del proyecto.

Última revisión: noviembre de 2026, después de la interfaz gráfica en JavaFX, la migración a Maven y el paso de la base de datos a Neon.

---

## Deuda pendiente

| # | Ítem | Dónde | Cuadrante | Principal | Interés | Responsable |
|---|---|---|---|---|---|---|
| DT-01 | Contraseñas guardadas en texto plano y comparadas con `equals` | `AutenticacionService`, `db/schema.sql` (`usuario.contrasena`) | Deliberada · prudente | 6 h | Alto, y mayor desde que la base está en Neon y es accesible desde internet: cada usuario nuevo se guarda sin protección y las credenciales de prueba están en el script | Esteban Hernández |
| DT-02 | Sin pruebas automatizadas (no existe carpeta de tests) en cierre de acta y cálculo de posiciones | `ResultadoService`, `TablaPosicionesService` | Inadvertida · imprudente | 20 h | Alto: cada cambio en reglas de resultados se verifica a mano | Natalia Valencia |
| DT-03 | `App.java` sigue concentrando todos los menús y la lectura de consola de la versión de consola, con campos `static` (el armado de servicios ya se movió a `Contexto`) | `src/main/java/App.java` | Deliberada · prudente | 8 h | Medio: archivo largo y difícil de modificar; hay que tocarlo para cualquier cambio de la versión de consola | Juan Pablo Franco |
| DT-04 | Conexión nueva a la BD en cada operación, sin pool; credenciales por defecto `postgres/postgres` | `GestorConexionJDBC` | Inadvertida · prudente | 8 h | Alto desde que la base está en Neon: cada operación abre una conexión nueva por internet y la primera conexión tras un rato sin uso tarda por el arranque en frío | Juan José Ortiz |
| DT-05 | Recalcula toda la tabla en cada cierre de acta y consulta eventos partido por partido (N+1) | `TablaPosicionesService.recalcular`, `ConsultaService` | Deliberada · prudente | 6 h | Bajo hoy, medio al crecer el número de partidos | Juan Pablo Franco |
| DT-06 | Las vistas JavaFX llaman a los servicios (y por tanto a la base) desde el hilo de la interfaz: la ventana se congela mientras dura cada consulta | `torneojav.vista` (todas las vistas) | Deliberada · prudente | 8 h | Medio: con la base remota se notan tirones y la ventana tarda en aparecer al arrancar | Juan Pablo Franco |
| DT-07 | Código repetido entre las vistas (columnas de tabla, alertas, diálogos) y selectores de torneo que se cargan una sola vez por sesión, así que un torneo nuevo no aparece hasta volver a iniciar sesión | `torneojav.vista` | Deliberada · prudente | 4 h | Bajo: cada cambio visual o de comportamiento común hay que repetirlo en varias vistas | Juan Pablo Franco |
| DT-08 | Una sola base compartida para todo el equipo, sin entorno de pruebas separado ni respaldo; ejecutar `schema.sql` la reinicia para todos y las credenciales se reparten a mano | Base en Neon, `db/schema.sql` | Deliberada · prudente | 4 h | Alto: un error al reiniciar o probar borra los datos de todos | Juan José Ortiz |

**Principal total estimado:** 67 h

---

## Deuda pagada

| # | Ítem | Cómo se resolvió | Fecha |
|---|---|---|---|
| DT-03 (parte) | El armado de repositorios y servicios estaba dentro del `main` de `App.java`, y la GUI tendría que repetirlo | Se extrajo a la clase `torneojav.Contexto`, que comparten la consola y la interfaz JavaFX | Noviembre de 2026 |
| DT-06 (parte) | Diagramas desactualizados que no reflejaban Neon y Maven | Se actualizaron correctamente los diagramas. Noviembre de 2026 |

---

## Cuadrantes (Fowler)

| | Prudente | Imprudente |
|---|---|---|
| **Deliberada** | "Entregamos ahora y asumimos las consecuencias" | "No tenemos tiempo para diseñar" |
| **Inadvertida** | "Ahora sabemos cómo debimos hacerlo" | "¿Qué es una capa de servicio?" |

## Definiciones

- **Principal:** esfuerzo estimado, en horas, para eliminar la deuda.
- **Interés:** costo recurrente de no pagarla (retrabajo, errores, riesgo).
- **Responsable:** quien responde por el ítem, según su área en el README.

## Notas de revisión

- Las horas son estimaciones iniciales; ajustarlas con story points o con la experiencia del equipo. En esta revisión se volvió a estimar DT-03 y se propusieron las horas de DT-07, DT-08 y DT-09.
- Si algún ítem fue una decisión consciente, debe pasar a **Deliberada**; si se descubrió después, a **Inadvertida**.
- Con Maven ya instalado en el proyecto, agregar pruebas con JUnit (DT-02) es posible y se ejecutan con `mvn test`.
- Revisar este registro al cierre de cada Sprint, junto con la retrospectiva.
