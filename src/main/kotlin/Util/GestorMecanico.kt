package org.example.Util

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.nio.file.Path

class GestorMecanico

{
    fun añadirCSV(ruta: Path, vehiculo: List<Vehiculo>) {
        val idMecanicoExistente = vehiculo.map { it.id_mecanico }
        var idMecanico = 0
        val nombreMecanico: String
        val especialidadMecanico: String
        var experienciaMecanico = 0
        var tarifaHoraMecanico = 0.0

        var idValido = false
        do {
            println("Introduce un ID para el mecánico no existente")
            val bandera = readln().toIntOrNull()
            if (bandera == null) {
                println("Error, ID no valida")
            } else if (bandera in idMecanicoExistente) {
                println("Error, el ID ya existe")
            } else {
                println("ID valido")
                idMecanico = bandera
                idValido = true
            }
        } while (!idValido)

        println("Dime el nombre del Mecanico")
        nombreMecanico = readln()
        println("Dime la especialidad del Mecanico")
        especialidadMecanico = readln()

        var experienciaValida = false
        do {
            println("Dime la experiencia del Mecanico")
            val banderaExp = readln().toIntOrNull()
            if (banderaExp == null) {
                println("Error, experiencia del Mecanico no valida")
            } else {
                println("Experiencia del Mecanico valida")
                experienciaMecanico = banderaExp
                experienciaValida = true
            }
        } while (!experienciaValida)

        var tarifaValida = false
        do {
            println("Dime la tarifa del Mecanico")
            val tarifaHoraBandera = readln().toDoubleOrNull()
            if (tarifaHoraBandera == null) {
                println("Error, tarifa del Mecanico no valida")
            } else {
                println("Tarifa del Mecanico valida")
                tarifaHoraMecanico = tarifaHoraBandera
                tarifaValida = true
            }
        } while (!tarifaValida)

        csvWriter { delimiter = ';' }.open(ruta.toFile(), append = true) {
            writeRow(
                idMecanico,
                nombreMecanico,
                especialidadMecanico,
                experienciaMecanico,
                tarifaHoraMecanico
            )
        }
        println("Se ha añadido al Mecanico correctamente")
    }

    fun modificarCSV(ruta: Path) {
            var idMecanico = 0
            var idValido = false

            do {
                println("Introduce el ID del mecánico a modificar")
                val bandera = readln().toIntOrNull()
                if (bandera == null) {
                    println("Error, ID no valida")
                } else {
                    idMecanico = bandera
                    idValido = true
                }
            } while (!idValido)

            val filas = csvReader { delimiter = ';' }.readAll(ruta.toFile()).toMutableList()
            val indiceFila = filas.indexOfFirst { it[0].toIntOrNull() == idMecanico }
            if (indiceFila == -1) {
                println("Error, no se ha encontrado ningún mecánico con ese ID")
                return
            }

            val filaEncontrada = filas[indiceFila]
            println("Mecánico encontrado: ${filaEncontrada[1]}") // nombre
            println("¿Qué campo quieres modificar?")
            println("1. Nombre")
            println("2. Especialidad")
            println("3. Experiencia")
            println("4. Tarifa por hora")

            var opcion = 0
            var opcionValida = false
            do {
                val bandera = readln().toIntOrNull()
                if (bandera == null || bandera !in 1..4) {
                    println("Error, opción no válida")
                } else {
                    opcion = bandera
                    opcionValida = true
                }
            } while (!opcionValida)

            val filaModificada = filaEncontrada.toMutableList()

            when (opcion) {
                1 -> {
                    println("Introduce el nuevo nombre")
                    filaModificada[1] = readln()
                }
                2 -> {
                    println("Introduce la nueva especialidad")
                    filaModificada[2] = readln()
                }
                3 -> {
                    var experiencia = 0
                    var experienciaValida = false
                    do {
                        println("Introduce la nueva experiencia")
                        val bandera = readln().toIntOrNull()
                        if (bandera == null) {
                            println("Error, experiencia no válida")
                        } else {
                            experiencia = bandera
                            experienciaValida = true
                        }
                    } while (!experienciaValida)
                    filaModificada[3] = experiencia.toString()
                }
                4 -> {
                    var tarifa = 0.0
                    var tarifaValida = false
                    do {
                        println("Introduce la nueva tarifa por hora")
                        val bandera = readln().toDoubleOrNull()
                        if (bandera == null) {
                            println("Error, tarifa no válida")
                        } else {
                            tarifa = bandera
                            tarifaValida = true
                        }
                    } while (!tarifaValida)
                    filaModificada[4] = tarifa.toString()
                }
            }

            filas[indiceFila] = filaModificada

            csvWriter { delimiter = ';' }.writeAll(filas, ruta.toFile())

            println("Se ha modificado al Mecánico correctamente")
        }

    fun eliminarCSV(ruta: Path) {
        var idMecanico = 0
        var idValido = false
        do {
            println("Introduce el ID del mecánico a eliminar")
            val bandera = readln().toIntOrNull()
            if (bandera == null) {
                println("Error, ID no valida")
            } else {
                idMecanico = bandera
                idValido = true
            }
        } while (!idValido)

        val filas = csvReader { delimiter = ';' }.readAll(ruta.toFile()).toMutableList()
        val indiceFila = filas.indexOfFirst { it[0].toIntOrNull() == idMecanico }

        if (indiceFila == -1) {
            println("Error, no se ha encontrado ningún mecánico con ese ID")
            return
        }

        val filaEncontrada = filas[indiceFila]
        println("Mecánico encontrado: ${filaEncontrada[1]}") // nombre
        println("¿Seguro que quieres eliminar a este mecánico? (SI/NO)")

        var confirmar: Boolean? = null
        var banderaValida = false
        do {
            val bandera = readln().trim().uppercase()
            when (bandera) {
                "SI" -> {
                    confirmar = true
                    banderaValida = true
                }
                "NO" -> {
                    confirmar = false
                    banderaValida = true
                }
                else -> println("Error, respuesta no válida. Introduce SI o NO")
            }
        } while (!banderaValida)

        if (confirmar == true) {
            filas.removeAt(indiceFila)
            csvWriter { delimiter = ';' }.writeAll(filas, ruta.toFile())
            println("Se ha eliminado al Mecánico correctamente")
        } else {
            println("No se ha eliminado al Mecánico")
        }
    }
}



