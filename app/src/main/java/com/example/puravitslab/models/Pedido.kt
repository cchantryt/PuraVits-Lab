package com.example.puravitslab.models

data class Pedido(
    val fecha: String = "",
    val productos: List<ProductoPedido> = listOf(),
    val total: Double = 0.0,
    val direccion: String = "",
    val telefono: String = "",
    val metodoPago: String = ""
)