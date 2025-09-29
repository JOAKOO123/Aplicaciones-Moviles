import kotlinx.coroutines.runBlocking

// ===================== Helpers de UI =====================
private fun leerLinea(prompt: String): String {
    print(prompt)
    System.out.flush()
    return readLine()?.trim().orEmpty()
}

private fun leerEntero(prompt: String): Int? =
    leerLinea(prompt).toIntOrNull()

private fun estadoTexto(e: EstadoPrestamo): String = when (e) {
    is EstadoPrestamo.Pendiente  -> "Pendiente"
    is EstadoPrestamo.EnPrestamo -> "En préstamo"
    is EstadoPrestamo.Devuelto   -> "Devuelto"
    is EstadoPrestamo.Error      -> "Error: ${e.msg}"
}

private fun mostrarCatalogo(catalogo: List<Libro>) {
    println("\nCatálogo:")
    catalogo.forEachIndexed { idx, libro ->
        println(
            "[$idx] ${libro.descripcion()} | " +
                    "costo=${formateaMonto(libro.costoFinal())} | días=${libro.diasPrestamo}"
        )
    }
}

private fun mostrarPrestamos(prestamos: List<Prestamo>) {
    if (prestamos.isEmpty()) {
        println("\nNo hay préstamos aún.")
        return
    }
    println("\nPréstamos:")
    prestamos.forEachIndexed { idx, p ->
        println("[$idx] ${p.libro.titulo} — ${estadoTexto(p.estado)}")
    }
}

// ===================== Menú principal =====================
fun main() = runBlocking {
    val catalogo = inicializarCatalogo()
    val prestamos = mutableListOf<Prestamo>()
    var tipoUsuario = TipoUsuario.ESTUDIANTE
    var diasRetraso = 0

    fun resumen() {
        val subtotal  = calcularSubtotal(prestamos)
        val descuento = aplicarDescuento(subtotal, tipoUsuario)
        val multa     = calcularMulta(diasRetraso)
        val total     = subtotal - descuento + multa

        println("\n=== Resumen ===")
        println("Tipo usuario:\t\t$tipoUsuario")
        println("Días de retraso:\t$diasRetraso")
        println("Subtotal:\t\t${formateaMonto(subtotal)}")
        println("Descuento:\t\t${formateaMonto(descuento)}")
        println("Multa:\t\t\t${formateaMonto(multa)}")
        println("TOTAL:\t\t\t${formateaMonto(total)}")
    }

    loop@ while (true) {
        println(
            """
            
            === BookSmart — Menú ===
            Usuario: $tipoUsuario | Días de retraso: $diasRetraso
            -----------------------------
            1) Ver catálogo
            2) Solicitar préstamo
            3) Ver préstamos
            4) Devolver préstamo
            5) Cambiar tipo de usuario
            6) Cambiar días de retraso
            7) Ver resumen (subtotal/desc/multa/total)
            0) Salir
            """.trimIndent()
        )

        when (leerLinea("Elige una opción: ")) {
            "1" -> {
                mostrarCatalogo(catalogo)
            }
            "2" -> {
                mostrarCatalogo(catalogo)
                val idx = leerEntero("\nIngresa el índice del libro a pedir: ")
                if (idx == null || idx !in catalogo.indices) {
                    println("Índice inválido.")
                } else {
                    val prestamo = Prestamo(catalogo[idx])
                    // Procesa la solicitud (reglas: referencia no se presta)
                    try {
                        val estado = procesarSolicitudPrestamo(prestamo)
                        prestamo.estado = estado
                        prestamos.add(prestamo)
                        println("Resultado: ${estadoTexto(estado)}")
                    } catch (e: IllegalArgumentException) {
                        val err = EstadoPrestamo.Error(e.message ?: "Error de validación")
                        prestamo.estado = err
                        prestamos.add(prestamo)
                        println("Resultado: ${estadoTexto(err)}")
                    } catch (e: Exception) {
                        val err = EstadoPrestamo.Error("Error inesperado: ${e.message}")
                        prestamo.estado = err
                        prestamos.add(prestamo)
                        println("Resultado: ${estadoTexto(err)}")
                    }
                }
            }
            "3" -> {
                mostrarPrestamos(prestamos)
            }
            "4" -> {
                // Solo permitir devolver los que están EnPrestamo
                val indicesVigentes = prestamos.withIndex()
                    .filter { it.value.estado is EstadoPrestamo.EnPrestamo }
                    .map { it.index }

                if (indicesVigentes.isEmpty()) {
                    println("\nNo hay préstamos 'En préstamo' para devolver.")
                } else {
                    println("\nPréstamos 'En préstamo':")
                    indicesVigentes.forEach { i ->
                        val p = prestamos[i]
                        println("[$i] ${p.libro.titulo} — ${estadoTexto(p.estado)}")
                    }
                    val idx = leerEntero("\nIngresa el índice del préstamo a devolver: ")
                    if (idx == null || idx !in indicesVigentes) {
                        println("Índice inválido.")
                    } else {
                        prestamos[idx].estado = EstadoPrestamo.Devuelto
                        println("Devuelto: ${prestamos[idx].libro.titulo}")
                    }
                }
            }
            "5" -> {
                println(
                    """
                    Tipos de usuario:
                    1) ESTUDIANTE (10%)
                    2) DOCENTE    (15%)
                    3) EXTERNO    (0%)
                    """.trimIndent()
                )
                when (leerLinea("Elige tipo: ")) {
                    "1" -> tipoUsuario = TipoUsuario.ESTUDIANTE
                    "2" -> tipoUsuario = TipoUsuario.DOCENTE
                    "3" -> tipoUsuario = TipoUsuario.EXTERNO
                    else -> println("Opción inválida.")
                }
                println("Tipo actualizado a: $tipoUsuario")
            }
            "6" -> {
                val d = leerEntero("Días de retraso (>= 0): ")
                if (d == null || d < 0) {
                    println("Valor inválido.")
                } else {
                    diasRetraso = d
                    println("Días de retraso actualizados a: $diasRetraso")
                }
            }
            "7" -> {
                resumen()
            }
            "0" -> {
                println("¡Hasta luego!")
                break@loop
            }
            else -> println("Opción no válida.")
        }
    }
}
