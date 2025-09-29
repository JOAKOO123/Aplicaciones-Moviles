import kotlinx.coroutines.delay

enum class TipoUsuario(val descuento: Double) {
    ESTUDIANTE(0.10),
    DOCENTE(0.15),
    EXTERNO(0.0)
}

data class Prestamo(
    val libro: Libro,
    var estado: EstadoPrestamo = EstadoPrestamo.Pendiente
)

fun inicializarCatalogo(): List<Libro> = listOf(
    LibroFisico("Estructuras de Datos", "Goodrich", 12990.0, diasPrestamo = 7, esReferencia = false),
    LibroFisico("Diccionario Enciclopédico", "Varios", 15990.0, diasPrestamo = 0, esReferencia = true),
    LibroDigital("Programación en Kotlin", "JetBrains", 9990.0, diasPrestamo = 10, drm = true),
    LibroDigital("Algoritmos Básicos", "Cormen", 11990.0, diasPrestamo = 10, drm = false)
)

suspend fun procesarSolicitudPrestamo(prestamo: Prestamo): EstadoPrestamo {
    // Simula operación "lenta" (servicio externo, BD, etc.).
    delay(3000)
    val l = prestamo.libro
    return when (l) {
        is LibroFisico -> {
            if (l.esReferencia) {
                val err = EstadoPrestamo.Error("No se puede prestar un libro de referencia: '${l.titulo}'.")
                prestamo.estado = err
                err
            } else {
                prestamo.estado = EstadoPrestamo.EnPrestamo
                prestamo.estado
            }
        }
        else -> {
            prestamo.estado = EstadoPrestamo.EnPrestamo
            prestamo.estado
        }
    }
}


fun calcularSubtotal(prestamos: List<Prestamo>): Double =
    prestamos.filter { it.estado is EstadoPrestamo.EnPrestamo }
        .sumOf { it.libro.costoFinal() }


fun aplicarDescuento(subtotal: Double, tipo: TipoUsuario): Double =
    subtotal * tipo.descuento

fun calcularMulta(diasRetraso: Int): Double {
    require(diasRetraso >= 0) { "Días de retraso inválidos: $diasRetraso" }
    val tarifa = 300.0 // CLP por día (ajustable)
    return diasRetraso * tarifa
}

fun formateaMonto(monto: Double): String =
    "%,.2f".format(java.util.Locale("es", "CL"), monto)
