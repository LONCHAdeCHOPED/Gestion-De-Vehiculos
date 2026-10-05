package org.example.Util
import java.nio.file.Path

class GestorBinario {

    fun menuBIN() {
        var numero : Int
//        val ruta = Path.of("datos", "mecanicos.csv")
        do {
            println("----------------------------------------")
            println("----------- CRUD fichero BIN -----------")
            println("----------------------------------------")
            println("1. Importar datos desde fichero de texto plano")
            println("2. Leer información del fichero binario")
            println("3. Añadir un registro nuevo")
            println("4. Modificar un registro existente (por ID)")
            println("5. Eliminar un registro existente (por ID)")
            println("0. Salir")
            numero = readLine()!!.toInt()
            when (numero) {
                1 ->
                2 ->
                3 ->
                4 ->
                0 -> {
                    println("Volviendo al menu principal...")
                }
            }
        } while (numero != 0)

    }




}