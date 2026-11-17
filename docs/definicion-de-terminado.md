# Definición de Terminado (DoD)

Una Historia de Usuario solo pasa a la columna **Closed (Done)** cuando cumple todo lo siguiente:

1. El código está integrado en `main` mediante un Pull Request aprobado.
2. Cada criterio de aceptación está escrito en formato Dado que / Cuando / Entonces.
3. Cada criterio de aceptación está cubierto por al menos un Test Case en Azure Test Plans.
4. Las pruebas unitarias de la historia están escritas y `mvn test` pasa sin errores, tanto en local como en el pipeline.
5. QA ejecutó los Test Cases de la historia y todos quedaron en estado Passed.
6. No hay Bugs de severidad 1 (Critical) o 2 (High) abiertos y vinculados a la historia.
7. La documentación (README o `docs/`) fue actualizada si cambió el comportamiento.

## Responsables
- Desarrollo: cumple los puntos 1, 3 (código de pruebas) y 4.
- QA (Natalia Valencia): verifica los puntos 2, 3, 5 y 6 y es quien mueve la historia a Closed.
