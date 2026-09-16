package com.example.rutasvivas.datos

data class Destino(
    var key: String = "",
    var nombre: String = "",
    var pais: String = "",
    var precio: Double = 0.0,
    var descripcion: String = "",
    var imagenPath: String = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "nombre" to nombre,
            "pais" to pais,
            "precio" to precio,
            "descripcion" to descripcion,
            "imagenPath" to imagenPath,
            "key" to key
        )
    }
}