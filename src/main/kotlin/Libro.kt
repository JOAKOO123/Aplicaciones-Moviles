open class Libro(
    val titulo: String,
    val autor: String,
    val precioBase: Double,
    val diasPrestamo: Int
) {
    init {
        require(precioBase >= 0) { "Precio base negativo: $precioBase" }
        require(diasPrestamo >= 0) { "Días de préstamo inválidos: $diasPrestamo" }
    }

    open fun costoFinal(): Double = precioBase

    open fun descripcion(): String = "$titulo — $autor"
}

class LibroFisico(
    titulo: String,
    autor: String,
    precioBase: Double,
    diasPrestamo: Int,
    val esReferencia: Boolean
) : Libro(titulo, autor, precioBase, diasPrestamo) {

    init {
        if (esReferencia) {
            require(diasPrestamo == 0) { "Un libro de referencia debe tener 0 días de préstamo." }
        } else {
            require(diasPrestamo > 0) { "Un libro físico no referencia debe tener días > 0." }
        }
    }

    override fun costoFinal(): Double =
        if (esReferencia) precioBase else precioBase * 1.02 // 2% mantención para préstamo físico

    override fun descripcion(): String =
        buildString {
            append("$titulo — $autor")
            if (esReferencia) append(" [Referencia]")
        }
}

class LibroDigital(
    titulo: String,
    autor: String,
    precioBase: Double,
    diasPrestamo: Int,
    val drm: Boolean
) : Libro(titulo, autor, precioBase, diasPrestamo) {

    init {
        require(diasPrestamo > 0) { "Un libro digital debe tener días > 0." }
    }

    override fun costoFinal(): Double =
        if (drm) precioBase * 1.05 else precioBase // 5% sobrecosto por DRM

    override fun descripcion(): String =
        buildString {
            append("$titulo — $autor")
            if (drm) append(" [DRM]")
        }
}
