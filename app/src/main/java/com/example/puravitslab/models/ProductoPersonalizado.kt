package com.example.puravitslab.models

// models/ProductoPersonalizado.kt
data class ProductoPersonalizado(
    val id: String = "",
    val nombre: String = "Mi Bálsamo",
    val color: String = "#FF0000",         // HEX
    val aroma: String = "",         // Nuevo
    val hidratacion: Int = 1,             // 1-5 (Nuevo)
    val ingredientes: List<String> = listOf("Cereza","Vainilla","Mango"), // Nuevo
    val usuarioId: String = "",
    val precioBase: Double = 5000.0,
    val fechaCreacion: String = ""
)