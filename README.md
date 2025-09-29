# BookSmart (Consola · Kotlin)

Proyecto de ejemplo para practicar la **Evaluación Parcial 1** de Desarrollo de Aplicaciones Móviles.
Incluye:
- Jerarquía `Libro` → `LibroFisico` / `LibroDigital` (herencia)
- Polimorfismo mediante `override` de `costoFinal()` y `descripcion()`
- `sealed class EstadoPrestamo` y `when` para manejar estados
- Corrutinas: `suspend fun procesarSolicitudPrestamo` + `delay(3000)`
- Colecciones (`List`) y operaciones funcionales (`filter`, `sumOf`)
- Manejo de errores con `require` y `try/catch`
- 
## Requisitos
- JDK 17+
- Gradle
- IntelliJ IDEA

## Ejecutar
1. Abrir la carpeta del proyecto en IntelliJ.
2. Sincronizar Gradle.
3. Ejecutar `Main.kt` o `gradle run`.

### Estructura
```
BookSmart/
├─ build.gradle.kts
├─ settings.gradle.kts
└─ src/main/kotlin
   ├─ Libro.kt
   ├─ EstadoPrestamo.kt
   ├─ GestorPrestamos.kt
   └─ Main.kt
```

### Notas de lógica
- Físico no referencia: +2% (mantención).
- Digital con DRM: +5% (restricción).
- Multa por retraso: $300 por día (ajustable en `GestorPrestamos.kt`).
- Descuento por tipo de usuario: `ESTUDIANTE 10%`, `DOCENTE 15%`, `EXTERNO 0%`.
