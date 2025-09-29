Bo@"
# BookSmart (Consola · Kotlin)

Proyecto de ejemplo para practicar la Evaluación Parcial 1 de Desarrollo de Aplicaciones Móviles.

## Características principales
- **Jerarquía de clases:** `Libro` → `LibroFisico` / `LibroDigital`
- **Polimorfismo:** override de `costoFinal()` y `descripcion()`
- **Estados con _sealed class_:** `EstadoPrestamo`
- **Corrutinas:** `suspend fun procesarSolicitudPrestamo()` con `delay(3000)`
- **Colecciones y operaciones funcionales:** `List`, `filter`, `sumOf`
- **Validaciones y errores:** `require`, `try/catch`

## Requisitos
- JDK 17+
- Gradle
- IntelliJ IDEA (recomendado)

## Cómo ejecutar
1. Abrir la carpeta del proyecto en IntelliJ IDEA.
2. Sincronizar Gradle.
3. Ejecutar `Main.kt` o usar:
   ```bash
   ./gradlew run
BookSmart/
├─ build.gradle.kts
├─ settings.gradle.kts
└─ src/
   └─ main/
      └─ kotlin/
         ├─ Libro.kt
         ├─ LibroFisico.kt
         ├─ LibroDigital.kt
         ├─ EstadoPrestamo.kt
         ├─ GestorPrestamos.kt
         └─ Main.kt


