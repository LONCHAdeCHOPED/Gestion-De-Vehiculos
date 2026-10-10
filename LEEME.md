
# Gestor de mecánicos

Este es un programa de consola desarrollado en Kotlin para gestionar un catálogo de mecánicos. Permite añadir, consultar, modificar y eliminar registros, así como importar, exportar y convertir datos entre formatos CSV, XML, JSON y binario.

## 1. Estructura de datos

### **Data Class:**
```kotlin
data class Mecanico(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val experiencia: Int,
    val tarifa: Double
)
```

---

**Estructura del registro binario:**

- **id**: Int - 4 bytes.
- **nombre**: String - 20 bytes.
- **especialidad**: String - 50 bytes.
- **experiencia**: Int - 4 bytes.
- **tarifa**: Double - 8 bytes.
- **Tamaño total del registro**: 4 + 20 + 50 + 4 + 8 = 86 bytes.



## 2. Instrucciones de ejecución

- **Requisitos previos**: Tener instalado un JDK compatible con el proyecto.
- **Ejecución**: Abrir el proyecto en un IDE, como IntelliJ IDEA, y ejecutar la función `main`.
- **Uso**: Seguir las opciones del menú de consola para gestionar mecánicos y realizar conversiones de archivos.
- **Ficheros**: El programa utiliza archivos CSV, XML, JSON y binarios para almacenar, importar y exportar los datos.



## 3. Decisiones de diseño

- **Organización de los datos**: Se utiliza una `data class` para representar cada mecánico de forma sencilla.
- **Formato binario**: Se establece un tamaño fijo de 86 bytes por registro, lo que facilita localizar y modificar registros mediante su posición.
- **Compatibilidad**: Se incluyen formatos CSV, XML y JSON para facilitar el intercambio de información.
- **Gestión mediante menú**: Se utiliza un menú de consola para organizar las operaciones disponibles y facilitar la interacción con el usuario.
