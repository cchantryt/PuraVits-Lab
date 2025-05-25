package com.example.puravitslab.models

data class ProductoPersonalizado(
    val id: String = "",
    val nombre: String = "Mi Bálsamo",
    val color: String = "#FF0000", // Color en formato HEX
    val usuarioId: String = "", // ID del usuario que lo creó
    val precioBase: Double = 5000.0, // Precio base del bálsamo
    val fechaCreacion: String = "" // Fecha en formato yyyy-MM-dd HH:mm:ss
)