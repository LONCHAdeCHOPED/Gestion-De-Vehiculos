package org.example.Util
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

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
                1 -> importarFicheroDesdeCSV(Path.of("datos", "mecanicos.csv"))
                2 -> leerMecanicosBIN()
                3 -> añadirMecanicoBIN(leerMecanicosBIN())
                4 -> modificarNombre()
                5 -> eliminarMecanicoBIN()
                0 -> {
                    println("Volviendo al menu principal...")
                }
            }
        } while (numero != 0)

    }
}

data class MecanicoBinario(
    val id_mecanico : Int,
    val nombre_mecanico : String,
    val especialidad_mecanico : String,
    val experiencia_mecanico : Int,
    val tarifa_mecanico : Double
)

// Definicion tamaño del registro binario
const val TAMAÑO_ID = Int.SIZE_BYTES
const val TAMAÑO_NOMBRE = 20
const val TAMAÑO_ESPECIALIDAD = 50
const val TAMAÑO_EXPERIENCIA = Int.SIZE_BYTES
const val TAMAÑO_TARIFA = Double.SIZE_BYTES
const val TAMAÑO_REGISTRO = TAMAÑO_ID + TAMAÑO_NOMBRE + TAMAÑO_ESPECIALIDAD + TAMAÑO_EXPERIENCIA + TAMAÑO_TARIFA

val archivoPath = Path.of("datos","mecanicos.bin")

fun vaciarCrearFichero() {
    try {
        FileChannel.open(
            archivoPath,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        ).close()
        println("--- El fichero '${archivoPath.fileName}' se ha creado y está vacío.")
    } catch (e: Exception) {
        println("Error al vaciar o crear el fichero: ${e.message}")
    }
}

fun importarFicheroDesdeCSV(rutaCSV: Path) {
    val lista = leerDatosCSV(rutaCSV)

    try {
        FileChannel.open(
            archivoPath,
            StandardOpenOption.CREATE,
            StandardOpenOption.WRITE,
            StandardOpenOption.APPEND
        ).use { canal ->
            val buffer = ByteBuffer.allocate(TAMAÑO_REGISTRO)

            for (mecanico in lista) {
                val nuevoMecanico = MecanicoBinario(
                    id_mecanico = mecanico.id_mecanico,
                    nombre_mecanico = mecanico.nombre,
                    especialidad_mecanico = mecanico.especialidad,
                    experiencia_mecanico = mecanico.experiencia,
                    tarifa_mecanico = mecanico.tarifaHora
                )
                buffer.clear()
                buffer.putInt(nuevoMecanico.id_mecanico)

                val nombreBytes = nuevoMecanico.nombre_mecanico
                    .padEnd(TAMAÑO_NOMBRE, ' ')
                    .toByteArray(Charsets.ISO_8859_1)
                buffer.put(nombreBytes, 0, TAMAÑO_NOMBRE)

                val especialidadBytes = nuevoMecanico.especialidad_mecanico
                    .padEnd(TAMAÑO_ESPECIALIDAD, ' ')
                    .toByteArray(Charsets.ISO_8859_1)
                buffer.put(especialidadBytes, 0, TAMAÑO_ESPECIALIDAD)

                buffer.putInt(nuevoMecanico.experiencia_mecanico)
                buffer.putDouble(nuevoMecanico.tarifa_mecanico)

                buffer.flip()
                while (buffer.hasRemaining()) {
                    canal.write(buffer)

                }
                println("- Mecanico '${nuevoMecanico.nombre_mecanico.trim()}' añadido correctamente.")
            }
        }

    } catch (e: Exception) {
        println("Error al importar el fichero: ${e.message}")
    }
}

fun leerMecanicosBIN(): List<MecanicoBinario> {
    val mecanicos = mutableListOf<MecanicoBinario>()

    if (!Files.isReadable(archivoPath)) return emptyList()

    FileChannel.open(archivoPath, StandardOpenOption.READ).use { canal ->
        val buffer = ByteBuffer.allocate(TAMAÑO_REGISTRO)

        while (canal.read(buffer) > 0) {
            buffer.flip()

            val id = buffer.getInt()
            val nombreBytes = ByteArray(TAMAÑO_NOMBRE)
            buffer.get(nombreBytes)
            val nombre = String(nombreBytes, Charsets.ISO_8859_1).trim()

            val especialidadBytes = ByteArray(TAMAÑO_ESPECIALIDAD)
            buffer.get(especialidadBytes)
            val especialidad = String(especialidadBytes, Charsets.ISO_8859_1).trim()

            val experiencia = buffer.getInt()
            val tarifa = buffer.getDouble()

            mecanicos.add(MecanicoBinario(id,nombre, especialidad, experiencia, tarifa))
            buffer.clear()
        }
    }

    for (mecanicos in mecanicos){
        println("ID: ${mecanicos.id_mecanico}, " +
                "Nombre: ${mecanicos.nombre_mecanico}, " +
                "Especialidad: ${mecanicos.especialidad_mecanico}, " +
                "Experiencia: ${mecanicos.experiencia_mecanico}, " +
                "Tarifa: ${mecanicos.tarifa_mecanico}")
    }
    return mecanicos
}

fun añadirMecanicoBIN(mecanico : List<MecanicoBinario>) {
    val idMecanicoExistente = mecanico.map { it.id_mecanico }
    var idValido = false
    var idMecanico = 0
    do {
        println("Introduce un ID para el mecánico no existente")
        val idMecanicoValida = readln().toIntOrNull()
        if (idMecanicoValida == null) {
            println("Error, ID no valida")
        } else if (idMecanicoValida in idMecanicoExistente) {
            println("Error, el ID ya existe")
        } else {
            println("ID valido")
            idMecanico = idMecanicoValida
            idValido = true
        }
    } while (!idValido)

    println("Dime el nombre del Mecanico")
    val nombreMecanico = readln()
    println("Dime la especialidad del Mecanico")
    val especialidad = readln()
    println("Dime la experiencia del Mecanico")
    val experiencia = readln().toInt()
    println("Dime la Tarifa del Mecanico")
    val tarifa = readln().toDouble()

    val nuevoMecanico = MecanicoBinario(idMecanico, nombreMecanico, especialidad, experiencia, tarifa)

    try {
        FileChannel.open(
            archivoPath,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        ).use { canal ->
            val buffer = ByteBuffer.allocate(TAMAÑO_REGISTRO)

            buffer.putInt(nuevoMecanico.id_mecanico)

            val nombreBytes = nuevoMecanico.nombre_mecanico
                .padEnd(TAMAÑO_NOMBRE, ' ')
                .toByteArray(Charsets.ISO_8859_1)
            buffer.put(nombreBytes, 0, TAMAÑO_NOMBRE)

            val especialidadBytes = nuevoMecanico.especialidad_mecanico
                .padEnd(TAMAÑO_ESPECIALIDAD, ' ')
                .toByteArray(Charsets.ISO_8859_1)
            buffer.put(especialidadBytes, 0, TAMAÑO_ESPECIALIDAD)

            buffer.putInt(nuevoMecanico.experiencia_mecanico)
            buffer.putDouble(nuevoMecanico.tarifa_mecanico)

            buffer.flip()
            while (buffer.hasRemaining()) {
                canal.write(buffer)
            }
            println("- Mecanico '${nuevoMecanico.nombre_mecanico.trim()}' añadido correctamente. -")
        }
    } catch (e: Exception) {
        println("Error al añadir al mecanico: ${e.message}")
    }
}

fun modificarNombre(){
    println("--- Modificar Nombre ---")
    var idMecanico: Int
    while (true) {
        print("Dime el ID del mecánico que quieres modificar: ")
        val input = readln().toIntOrNull()

        if (input == null) {
            println("Error: ID no válido.")
        } else {
            idMecanico = input
            break
        }
    }

    println("Dime el nuevo nombre")
    val nombre = readln()
    val nombreBytes = nombre.toByteArray(Charsets.ISO_8859_1)

    if (nombreBytes.size > TAMAÑO_NOMBRE) {
        println("Error: el nombre no puede superar $TAMAÑO_NOMBRE bytes.")
        return
    }

    try {
        FileChannel.open(
            archivoPath,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE
        ).use { canal ->

            val buffer = ByteBuffer.allocate(TAMAÑO_REGISTRO)
            var encontrado = false

            while (canal.read(buffer) > 0 && !encontrado) {
                // Solo procesar registros completos
                if (buffer.position() < TAMAÑO_REGISTRO) {
                    println("Error: registro binario incompleto.")
                    return
                }

                val posicionActual = canal.position()
                buffer.flip()
                val id = buffer.getInt()

                if (id == idMecanico) {
                    encontrado = true
                    val inicioRegistro = posicionActual - TAMAÑO_REGISTRO
                    val posicionNombre = inicioRegistro + TAMAÑO_ID
                    canal.position(posicionNombre)

                    // Preparar el nuevo nombre con espacios
                    val nombreRelleno = nombre.padEnd(
                        TAMAÑO_NOMBRE,
                        ' '
                    ).toByteArray(Charsets.ISO_8859_1)

                    val bufferNombre = ByteBuffer.wrap(
                        nombreRelleno,
                        0,
                        TAMAÑO_NOMBRE
                    )

                    while (bufferNombre.hasRemaining()) {
                        canal.write(bufferNombre)
                    }
                }
                buffer.clear()
            }

            if (encontrado) {
                println("Nombre del mecánico con ID $idMecanico modificado correctamente.")
            } else {
                println("No se encontró ningún mecánico con el ID $idMecanico.")
            }
        }
    }catch (e: Exception) {
        println("Error al modificar el nombre: ${e.message}")
    }
}


fun eliminarMecanicoBIN() {
    var idMecanico: Int
    while (true) {
        print("Dime el ID del mecánico que quieres eliminar: ")
        val input = readln().toIntOrNull()
        if (input == null) {
            println("Error: Debes introducir un ID válido.")
        } else {
            idMecanico = input
            break
        }
    }

    val pathTemporal = Path.of(archivoPath.toString() + ".tmp")
    var mecanicoEncontrado = false

    try {
        FileChannel.open(
            archivoPath, StandardOpenOption.READ).use { canalLectura ->
            FileChannel.open(
                pathTemporal,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            ).use { canalEscritura ->
                val buffer = ByteBuffer.allocate(TAMAÑO_REGISTRO)

                while (canalLectura.read(buffer) > 0) {
                    buffer.flip()
                    val id = buffer.getInt()

                    if (id == idMecanico) {
                        mecanicoEncontrado = true
                    } else {
                        buffer.rewind()
                        while (buffer.hasRemaining()) {
                            canalEscritura.write(buffer)
                        }
                    }
                    buffer.clear()
                }
            }
        }

        if (mecanicoEncontrado) {
            // Sustituir el fichero original por el temporal
            Files.move(
                pathTemporal,
                archivoPath,
                StandardCopyOption.REPLACE_EXISTING)
            println("\n**** Mecánico con ID $idMecanico eliminado con éxito.")
        } else {
            Files.deleteIfExists(pathTemporal)
            println("No se encontró el mecánico con ID: $idMecanico")
        }
    } catch (e: Exception) {
        println("Error durante la eliminación: ${e.message}")
    }
}
