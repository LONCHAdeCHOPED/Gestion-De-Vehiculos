package org.example.Util

import java.nio.file.Path
import java.nio.file.Files
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter


data class Vehiculo(
    val id_mecanico: Int,
    val nombre: String,
    val especialidad: String,
    val experiencia : Int,
    val tarifa_hora: Double
)

fun main() {
    var opcion: Int = -1


    do {
        println("--------------------------------------")
        println("----------- MENÚ PRINCIPAL -----------")
        println("--------------------------------------")
        println("1. Gestion CSV")
        println("0. Salir")

        try {
            opcion = readLine()!!.toInt()

            when (opcion) {
                1 -> menuCSV()
                0 -> {
                    println("FIN")
                     }
                else -> {
                    println("Error")
                }
            }
        } catch (e: NumberFormatException) {
            println("Error")
            opcion = -1
        }

    } while (opcion != 0)
}


fun menuCSV() {
    var numero : Int
    val ruta = Path.of("datos", "mecanicos.csv")
    val gestion = GestorMecanico()
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
            1 -> println(leerDatosCSV(ruta).joinToString(separator = "\n"))
            2 -> println(gestion.añadirCSV(ruta, leerDatosCSV(ruta)))
            0 -> {
                println("Volviendo al menu principal...")
            }
        }
    } while (numero != 0)

}

fun leerDatosCSV(ruta: Path): List<Vehiculo> {
    var vehiculos: List<Vehiculo> = emptyList()

    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer el fichero en la ruta: $ruta")

    } else {
        val reader = csvReader(){
            delimiter = ';'
        }

        val filas: List<List<String>> = reader.readAll(ruta.toFile())

        vehiculos = filas.mapNotNull { columnas ->
            if (columnas.size >= 5){
                try {
                    val idMecanico = columnas[0].toInt()
                    val nombreMecanico = columnas[1]
                    val especialidadMecanico = columnas[2]
                    val experienciaMecanico = columnas[3].toInt()
                    val tarifaHoraMecanico = columnas[4].toDouble()
                    Vehiculo(idMecanico, nombreMecanico, especialidadMecanico, experienciaMecanico, tarifaHoraMecanico)
                } catch (e: Exception){
                    println("Fila inválida ignorada: $columnas -> Error: ${e.message}")
                    null
                }
            } else {
                println("Fila con formato incompleto ignorada: $columnas")
                null
            }

        }
    }
    println("--- Información leida con existo de: $ruta")
    return vehiculos

}



