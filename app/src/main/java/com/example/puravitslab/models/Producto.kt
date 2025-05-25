package com.example.puravitslab.models

data class Producto(
    var id: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val imagenUrl: String = "",
    val descripcion: String = ""
)