package org.example.Util

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.nio.file.Path

class GestorMecanico

{
    fun añadirCSV(ruta : Path, vehiculo: List<Vehiculo>) {
        val idMecanicoExistente = vehiculo.map { it.id_mecanico }
        val idMecanico : Int
        val nombreMecanico : String
        val especialidadMecanico: String
        val experienciaMecanico: Int
        val tarifaHoraMecanico: Double

        while (true) {
            println("Introduce un ID para el mecánico no existente")
            val bandera = readln().toIntOrNull()
            if (bandera == null) {
                println("Error, ID no valida")
            } else if (bandera in idMecanicoExistente) {
                println("Error, el ID ya existe")
            } else {
                println("ID valido")
                idMecanico = bandera
                break
            }
        }

        println("Dime el nombre del Mecanico")
        nombreMecanico = readln()
        println("Dime la especialidad del Mecanico")
        especialidadMecanico = readln()

        while (true){
            println("Dime la experiencia del Mecanico")
            val banderaExp = readln().toIntOrNull()
            if (banderaExp == null) {
                println("Error, experiencia del Mecanico no valida")
            } else {
                println("Experiencia del Mecanico valida")
                experienciaMecanico = banderaExp
                break
            }
        }

        while (true){
            println("Dime la tarifa del Mecanico")
            val tarifaHoraBandera = readln().toDoubleOrNull()
            if (tarifaHoraBandera == null) {
                println("Error, tarifa del Mecanico no valida")
            } else {
                println("Tarifa del Mecanico valida")
                tarifaHoraMecanico = tarifaHoraBandera
                break
            }
        }

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

    fun modificarCSV(ruta : Path, vehiculo: List<Vehiculo>){

    }
}



