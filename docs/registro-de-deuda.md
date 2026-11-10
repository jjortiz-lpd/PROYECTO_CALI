## Registro de Deuda Técnica — TorneoJAV
Registro de los ítems de deuda técnica identificados en el código y la documentación del proyecto. 

---

| # | Ítem | Dónde | Cuadrante | Principal | Interés | Responsable |
|---|---|---|---|---|---|---|
| DT-01 | Contraseñas guardadas en texto plano y comparadas con `equals` | `AutenticacionService`, `db/schema.sql` (`usuario.contrasena`) | Deliberada · prudente | 6 h | Alto: cada usuario nuevo se guarda sin protección y las credenciales de prueba están en el script | Esteban Hernández |
| DT-02 | Sin pruebas automatizadas (no existe carpeta de tests) en cierre de acta y cálculo de posiciones | `ResultadoService`, `TablaPosicionesService` | Inadvertida · imprudente | 20 h | Alto: cada cambio en reglas de resultados se verifica a mano | Natalia Valencia |
| DT-03 | `App.java` de 535 líneas mezcla menús, lectura de consola y armado de dependencias con campos `static` | `src/App.java` | Deliberada · prudente | 16 h | Alto: la GUI en JavaFX tendrá que repetir el cableado de servicios | Juan Pablo Franco |
| DT-04 | Conexión nueva a la BD en cada operación, sin pool; credenciales por defecto `postgres/postgres` | `GestorConexionJDBC` | Inadvertida · prudente | 8 h | Medio: la latencia crece con el uso | Juan José Ortiz |
| DT-05 | Recalcula toda la tabla en cada cierre de acta y consulta eventos partido por partido (N+1) | `TablaPosicionesService.recalcular`, `ConsultaService` | Deliberada · prudente | 6 h | Bajo hoy, medio al crecer el número de partidos | Juan Pablo Franco |
| DT-06 | Diagramas duplicados y desactualizados (`docs/diagrama-*.md` vs `docs/diagramas/`) | `docs/` | Inadvertida · prudente | 3 h | Medio: no hay una versión vigente clara | Juan Pablo Franco |

**Principal total estimado:** 59 h

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

- Las horas son estimaciones iniciales; ajustarlas con story points o con la experiencia del equipo.
- Si algún ítem fue una decisión consciente, debe pasar a **Deliberada**; si se descubrió después, a **Inadvertida**.
- Revisar este registro al cierre de cada Sprint, junto con la retrospectiva.
