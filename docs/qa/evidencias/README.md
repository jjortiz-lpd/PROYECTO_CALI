# Evidencias de QA — TorneoJAV

Esta carpeta reúne todas las evidencias (capturas de pantalla e información del sistema) de las pruebas manuales ejecutadas sobre TorneoJAV entre el 17/11/2026 y el 19/11/2026. Sirven para demostrar que cada resultado reportado realmente ocurrió y para reproducir los defectos encontrados.

## Documentos que usan estas evidencias

| Documento | Qué evidencias referencia |
|---|---|
| [`../informe-de-prueba-de-humo.md`](../informe-de-prueba-de-humo.md) | Todas las capturas de `evidencias_prueba_de_humo/` |
| [`../reporte-de-pruebas.md`](../reporte-de-pruebas.md) | Capturas seleccionadas de las tres carpetas (sección 8) |

## Estructura de la carpeta

```text
docs/qa/evidencias/
├── README.md                          ← este archivo
├── evidencias_prueba_de_humo/         ← prueba de humo (17/11/2026)
├── evidencias_run_1000025/            ← casos de prueba por HU (Azure Test Plans)
└── evidencias_run_1000027/            ← casos adicionales orientados a defectos
```

| Carpeta | Origen | Contenido | Casos cubiertos |
|---|---|---|---|
| `evidencias_prueba_de_humo/` | Capturas manuales | 12 capturas `PH-XX-*.png` | Pasos 0 a 10 de la prueba de humo |
| `evidencias_run_1000025/` | Exportación de Azure Test Plans (run 1000025) | 66 capturas de pantalla por paso | Casos 170 a 213 (HU-01 a HU-10) |
| `evidencias_run_1000027/` | Exportación de Azure Test Plans (run 1000027) | 32 capturas y 15 archivos `SystemInformation` (`.json`) | Casos 223 a 235 (casos adicionales de borde e integridad) |

## Convenciones de nombre

### Prueba de humo

```text
PH-XX-descripcion.png
```

- `PH` = prueba de humo.
- `XX` = número del paso en la tabla de resultados del informe (`PH-00` es el estado inicial).
- Si un paso tiene varias capturas, cada una lleva una descripción distinta (por ejemplo `PH-07-confirmar-cierre-acta.png` y `PH-07-resultados.png`).
- Los pasos 6 y 8 no tienen captura porque no se pueden ejecutar desde la interfaz gráfica (ver el informe).

### Runs de Azure Test Plans

Capturas:

```text
<ID caso>_<nombre del caso>_Paso_Screenshot_<fecha-hora>.png
```

Información del sistema:

```text
<ID caso>_<nombre del caso>_Paso_SystemInformation-<fecha-hora>.json
```

- `<ID caso>` es el ID del caso de prueba en Azure Test Plans (por ejemplo `224`).
- `<nombre del caso>` sigue el formato `CP-HUxx-descripcion`, donde `HUxx` es la historia de usuario que se prueba.
- `<fecha-hora>` está en UTC (la `Z` final), por lo que va 5 horas adelante de la hora de Colombia.

## Cómo encontrar la evidencia de un caso

1. Busca el ID del caso en la tabla de la sección 6 del [reporte de pruebas](../reporte-de-pruebas.md).
2. Según el ID, abre la carpeta que corresponde:
   - Casos **170 a 213**: `evidencias_run_1000025/`.
   - Casos **223 a 235**: `evidencias_run_1000027/`.
3. Busca los archivos que empiezan con ese ID. Por ejemplo, en una terminal:

```bash
ls docs/qa/evidencias/evidencias_run_1000027 | grep "^224_"
```

Un caso puede tener **varias capturas**: una por cada paso que el tester marcó en Azure Test Runner. Ordénalas por fecha-hora para verlas en secuencia.

## Archivos `SystemInformation`

Los archivos `.json` de `evidencias_run_1000027/` los genera automáticamente Azure Test Runner al capturar un paso. Contienen datos del equipo y del navegador usados en la ejecución (sistema operativo, navegador, etc.). Sirven para saber en qué entorno se ejecutó cada caso y no hace falta abrirlos para entender el resultado.

## Casos con evidencia que conviene revisar primero

Estos son los casos que documentan los defectos más graves del reporte:

| Caso | Defecto | Qué muestra la evidencia |
|---|---|---|
| 224 | #250 (Crítica) | Eliminación directa: el ganador de la ronda 1 queda fuera de la ronda 2 |
| 223 | #247 (Alta) | Equipo sin jugadores con estado APROBADO |
| 227 | #251 (Alta) | Acta sin opción para corregir o eliminar un evento |
| 228 | #252 (Media) | Jugador con tarjeta roja que sigue marcando goles |



