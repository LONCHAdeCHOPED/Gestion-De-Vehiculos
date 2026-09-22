package org.example

data class Vehiculo(
    val id_vehiculo: Int,
    val modelo: String,
    val potencia: Int,
    val consumo : Double
)
fun main() {
    var opcion: Int

    do {
        println("--------------------------------------")
        println("----------- MENÚ PRINCIPAL -----------")
        println("--------------------------------------")
        println("1. Gestion CSV")
        println("0. Salir")

        try {
            val opcion = readLine()!!.toInt()

            when (opcion) {
                1 -> menuCSV()
                0 -> {
                    println("FIN")
                    break }
                else -> println("Error")

            }
        } catch (e: NumberFormatException) {
            println("Error")
        }

    } while (opcion != 0)
}


fun menuCSV() {
    var numero : Int
    do {
        println("--------------------------------")
        println("----------- CRUD CSV -----------")
        println("--------------------------------")
        println("1. Leer datos desde CSV")
        println("2. Añadir un registro nuevo al final del fichero")
        println("3. Modificar un registro existente por ID")
        println("4.Eliminar un registro existente por ID")
        println("0. Volver al menú principal")
        numero = readLine()!!.toInt()
        when (numero) {
            1 -> println("Leyendo")
            0 -> { main()
            break
            }
        }
    } while (numero != 0)

}