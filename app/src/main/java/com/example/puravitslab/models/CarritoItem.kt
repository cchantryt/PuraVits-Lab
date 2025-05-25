package com.example.puravitslab.models

data class CarritoItem(
    val productoId: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val imagenUrl: String = "",
    val cantidad: Int = 1,
    val esPersonalizado: Boolean = false,
    val colorPersonalizado: String = "", // Simplificamos la personalización
    val nombrePersonalizado: String = ""
)