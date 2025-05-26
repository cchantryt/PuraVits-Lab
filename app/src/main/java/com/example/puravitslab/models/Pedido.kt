package com.example.puravitslab.models

import android.os.Parcel
import android.os.Parcelable

data class Pedido(
    var id: String = "", // Nuevo campo
    val fecha: String = "",
    val productos: List<ProductoPedido> = listOf(),
    val total: Double = 0.0,
    val direccion: String = "",
    val telefono: String = "",
    val metodoPago: String = "",
    val estado: String = "pendiente", // pendiente, enviado, entregado
    var usuarioId: String = "" // Para referencia al usuario
)