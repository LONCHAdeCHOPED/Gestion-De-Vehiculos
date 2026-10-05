package org.example.Util

import java.nio.file.Path
import java.nio.file.Files
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.io.path.Path
import kotlin.math.exp

data class Mecanico(
    val id_mecanico: Int,
    val nombre: String,
    val especialidad: String,
    val experiencia : Int,
    val tarifaHora: Double
)

fun main() {
    var opcion: Int = -1


    do {
        println("--------------------------------------")
        println("----------- MENÚ PRINCIPAL -----------")
        println("--------------------------------------")
        println("1. Gestion CSV")
        println("2. Leer datos desde XML")
        println("3. Leer datos desde JSON")
        println("4. Convertir JSON a CSV")
        println("5. Convertir JSON a XML")
        println("6. Convertir XML a JSON")
        println("7. Convertir XML a CSV")
        println("8. Convertir CSV a JSON")
        println("9. Convertir CSV a XML")
        println("10. Gestión de fichero BIN")
        println("0. Salir")

        try {
            opcion = readLine()!!.toInt()


            when (opcion) {
                1 -> menuCSV()
                2 -> leerDatosXML(Path.of("datos", "mecanico.xml"))
                3 -> leerJSON(Path.of("datos", "mecanicos.json"))
                4 -> convertirJSONaCSV(Path.of("datos")) // Archivos en el metodo
                5 -> convertirJSONaXML(Path.of("datos", "mecanicos.json"), Path.of("datos", "mecanicosJSON-XML.xml"))
                6 -> convertirXMLaJSON(Path.of("datos", "mecanico.xml"), Path.of("datos", "mecanicosXML-JSON.json"))
                7 -> convertirXMLaCSV(Path.of("datos", "mecanico.xml"), Path.of("datos", "mecanicosXML-CSV.csv"))
                8 -> convertirCSVaXML(Path.of("datos", "mecanicos.csv"), Path("datos", "mecanicosCSV-XML.xml"))
                9 -> convertirCSVaJSON(Path.of("datos", "mecanicos.csv"), Path.of("datos", "mecanicosCSV-JSON.json"))
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
        println("4. Eliminar un registro existente por ID")
        println("0. Volver al menú principal")
        numero = readLine()!!.toInt()
        when (numero) {
            1 -> println(leerDatosCSV(ruta).joinToString(separator = "\n"))
            2 -> println(gestion.añadirCSV(ruta, leerDatosCSV(ruta)))
            3 -> println(gestion.modificarCSV(ruta))
            4 -> println(gestion.eliminarCSV(ruta))
            0 -> {
                println("Volviendo al menu principal...")
            }
        }
    } while (numero != 0)

}

fun leerDatosCSV(ruta: Path): List<Mecanico> {
    var mecanicos: List<Mecanico> = emptyList()

    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer el fichero en la ruta: $ruta")

    } else {
        val reader = csvReader(){
            delimiter = ';'
        }

        val filas: List<List<String>> = reader.readAll(ruta.toFile())

        mecanicos = filas.mapNotNull { columnas ->
            if (columnas.size >= 5){
                try {
                    val idMecanico = columnas[0].toInt()
                    val nombreMecanico = columnas[1]
                    val especialidadMecanico = columnas[2]
                    val experienciaMecanico = columnas[3].toInt()
                    val tarifaHoraMecanico = columnas[4].toDouble()
                    Mecanico(idMecanico, nombreMecanico, especialidadMecanico, experienciaMecanico, tarifaHoraMecanico)
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
    return mecanicos

}

// Clase que modela los nodos individuales <mecanico>
data class MecanicoXML(
    @JacksonXmlProperty(localName = "id_mecanico")
    val idMecanico: Int,
    @JacksonXmlProperty(localName = "nombre")
    val nombreMecanico: String,
    @JacksonXmlProperty(localName = "especialidad")
    val especialidad: String,
    @JacksonXmlProperty(localName = "experiencia")
    val experiencia: Int,
    @JacksonXmlProperty(localName = "tarifa_hora")
    val tarifaHora: Double
)

@JacksonXmlRootElement(localName = "mecanicos")
data class MecanicoWrapper(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "mecanico")
    val listaMecanico: List<MecanicoXML> = emptyList()
)

fun leerDatosXML(ruta: Path): List<MecanicoXML> {
    var contenedor = MecanicoWrapper(emptyList())

    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer datos desde XML")
    } else {
        val fichero = ruta.toFile()
        val xmlMapper = XmlMapper().registerKotlinModule()

        contenedor = xmlMapper.readValue(fichero)
        println("--- Información leída con éxito de: $ruta")
    }
    for (mecanicos in contenedor.listaMecanico){
        println("ID: ${mecanicos.idMecanico}, " +
                "Nombre: ${mecanicos.nombreMecanico}, " +
                "Especialidad: ${mecanicos.especialidad}, " +
                "Experiencia: ${mecanicos.experiencia}, " +
                "Tarifa ${mecanicos.tarifaHora}")
    }
    return contenedor.listaMecanico
}

fun escribirDatosXML(ruta: Path, mecanicos: List<MecanicoXML>) {
    try {
        val fichero = ruta.toFile()
        val contenedor = MecanicoWrapper(mecanicos)
        val xmlMapper = XmlMapper().registerKotlinModule()

        val xmlString = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedor)
        fichero.writeText(xmlString)

        println("--- Informacion guardada en XML: $fichero")
    } catch (e: Exception){
        println("Error al guardar XML: ${e.message}")
    }
}

// Anotamos la data class indicando que es serializable para el compilador de Kotlin
@Serializable
data class MecanicoJSON(
    @SerialName("id_mecanico") val idMecanico: Int,
    @SerialName("nombre") val nombreMecanico: String,
    @SerialName("especialidad") val especialidad: String,
    @SerialName("experiencia") val experiencia: Int,
    @SerialName("tarifa_hora") val tarifaHora: Double
)

fun leerJSON(ruta: Path) : List<MecanicoJSON> {
    var mecanicos: List<MecanicoJSON> = emptyList()

    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer JSON")
    } else {
        val jsonString = Files.readString(ruta)

        mecanicos = Json.decodeFromString<List<MecanicoJSON>>(jsonString)
        println("--- Información leída con éxito de: $ruta")
    }
    for (mecanicos in mecanicos){
        println("ID: ${mecanicos.idMecanico}, " +
                "Nombre: ${mecanicos.nombreMecanico}, " +
                "Especialidad: ${mecanicos.especialidad}, " +
                "Experiencia: ${mecanicos.experiencia}, " +
                "Tarifa ${mecanicos.tarifaHora}")
    }
    return mecanicos
}

fun escribirJSON(ruta: Path, mecanicos: List<MecanicoJSON>) {
    try {
        val jsonConfigurador = Json{prettyPrint = true}
        val jsonString = jsonConfigurador.encodeToString(mecanicos)

        Files.writeString(ruta, jsonString)
        println("--- Información guardada en: $ruta")
    } catch (e: Exception){
        println("Error al guardar JSON: ${e.message}")
    }
}

fun convertirJSONaCSV(ruta: Path){
    val gestor = GestorMecanico()

    val rutaJSON = ruta.resolve("mecanicos.json")
    val rutaCSV = ruta.resolve("mecanicos.csv")

    val mecanicosJSON : List<MecanicoJSON> = leerJSON(rutaJSON)

    val mecanicos: List<Mecanico> = mecanicosJSON.map {
        Mecanico(
            id_mecanico = it.idMecanico,
            nombre = it.nombreMecanico,
            especialidad = it.especialidad,
            experiencia = it.experiencia,
            tarifaHora = it.tarifaHora
        )
    }

        csvWriter { delimiter = ';' }.open(rutaCSV.toFile()) {
            mecanicos.forEach {mecanico ->
                writeRow(
                    mecanico.id_mecanico,
                    mecanico.nombre,
                    mecanico.especialidad,
                    mecanico.experiencia,
                    mecanico.tarifaHora
                )
            }
        }
        println("--- JSON convertido a CSV")
    }

fun convertirCSVaJSON(rutaCsv: Path, rutaJson: Path) {
    try {
        val listaMecanicos = leerDatosCSV(rutaCsv)

        if (listaMecanicos.isEmpty()) {
            println("El archivo CSV está vacio")
            return
        }

        val mecanicoJSON = listaMecanicos.map {
            MecanicoJSON(
                idMecanico = it.id_mecanico,
                nombreMecanico = it.nombre,
                especialidad = it.especialidad,
                experiencia = it.experiencia,
                tarifaHora = it.tarifaHora
            )
        }

        val jsonConfigurador = Json { prettyPrint = true }
        val jsonString = jsonConfigurador.encodeToString(mecanicoJSON)
        Files.writeString(rutaJson, jsonString)
        println("Conversión completada")

    } catch (e: Exception) {
        println("Error")
    }
}

fun convertirCSVaXML(rutaCsv: Path, rutaXml: Path) {
    try {
        val listaMecanicos = leerDatosCSV(rutaCsv)

        if (listaMecanicos.isEmpty()) {
            println("El archivo CSV esta vacio")
            return
        }

        val mecanicosXML = listaMecanicos.map {
            MecanicoXML(
                idMecanico = it.id_mecanico,
                nombreMecanico = it.nombre,
                especialidad = it.especialidad,
                experiencia = it.experiencia,
                tarifaHora = it.tarifaHora
            )
        }

        val contenedor = MecanicoWrapper(mecanicosXML)
        val xmlMapper = XmlMapper().registerKotlinModule()
        val xmlString = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedor)
        rutaXml.toFile().writeText(xmlString)
        println("Conversión completada")

    } catch (e: Exception) {
        println("Error")
    }
}

fun convertirJSONaXML(rutaJson: Path, rutaXml: Path) {
    if (!Files.isReadable(rutaJson)) {
        println("Error: No se puede leer el fichero JSON")
        return
    }

    try {
        val jsonString = Files.readString(rutaJson)
        val mecanicoJSON = Json.decodeFromString<List<MecanicoJSON>>(jsonString)

        if (mecanicoJSON.isEmpty()) {
            println("El archivo JSON esta vacio")
            return
        }

        val circuitosXml = mecanicoJSON.map {
            MecanicoXML(
                idMecanico = it.idMecanico,
                nombreMecanico = it.nombreMecanico,
                especialidad = it.especialidad,
                experiencia = it.experiencia,
                tarifaHora = it.tarifaHora
            )
        }

        val fichero = rutaXml.toFile()
        val contenedor = MecanicoWrapper(circuitosXml)
        val xmlMapper = XmlMapper().registerKotlinModule()
        val xmlString = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedor)
        fichero.writeText(xmlString)
        println("Conversion completada")

    } catch (e: Exception) {
        println("Error")
    }
}

fun convertirXMLaCSV(rutaXml: Path, rutaCsv: Path) {
    if (!Files.isReadable(rutaXml)) {
        println("Error: No se puede leer el fichero XML")
        return
    }

    try {
        val xmlMapper = XmlMapper().registerKotlinModule()
        val contenedor = xmlMapper.readValue<MecanicoWrapper>(rutaXml.toFile())
        val mecanicoXML = contenedor.listaMecanico

        if (mecanicoXML.isEmpty()) {
            println("El archivo XML esta vacio")
            return
        }

        csvWriter { delimiter = ';' }.open(rutaCsv.toFile(), append = false) {
            mecanicoXML.forEach { mecanico ->
                writeRow(
                    mecanico.idMecanico,
                    mecanico.nombreMecanico,
                    mecanico.especialidad,
                    mecanico.experiencia,
                    mecanico.tarifaHora
                )
            }
        }
        println("Conversion completada")

    } catch (e: Exception) {
        println("Error")
    }

}


fun convertirXMLaJSON(rutaXml: Path, rutaJson: Path) {
    if (!Files.isReadable(rutaXml)) {
        println("Error: No se puede leer el fichero XML")
        return
    }

    try {
        val xmlMapper = XmlMapper().registerKotlinModule()
        val contenedor = xmlMapper.readValue(rutaXml.toFile(), MecanicoWrapper::class.java)
        val mecanicoXML = contenedor.listaMecanico

        if (mecanicoXML.isEmpty()) {
            println("El archivo XML está vacio")
            return
        }

        val mecanicoJSON = mecanicoXML.map {
            MecanicoJSON(
                idMecanico = it.idMecanico,
                nombreMecanico = it.nombreMecanico,
                especialidad = it.especialidad,
                experiencia = it.experiencia,
                tarifaHora = it.tarifaHora
            )
        }

        val jsonConfigurador = Json { prettyPrint = true }
        val jsonString = jsonConfigurador.encodeToString(mecanicoJSON)
        Files.writeString(rutaJson, jsonString)
        println("Conversion completada")

    } catch (e: Exception) {
        println("Error")
    }
}