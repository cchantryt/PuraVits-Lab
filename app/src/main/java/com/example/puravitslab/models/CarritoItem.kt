package com.example.puravitslab.models

data class CarritoItem(
    val productoId: String = "",
    val configId: String = "",
    val nombre: String = "",
    val precio: Double = 0.0,
    val cantidad: Int = 1,
    val esPersonalizado: Boolean = false,
    val colorPersonalizado: String = "",
    val aroma: String = "", // Valor por defecto
    val hidratacion: Int = 1 // Valor por defecto (1-5), ahora opcional
)
fun CarritoItem.toProductoPedido(): ProductoPedido {
    return ProductoPedido(
        nombre = this.nombre,
        precio = this.precio,
        cantidad = this.cantidad,
        esPersonalizado = this.esPersonalizado,
        colorPersonalizado = this.colorPersonalizado,
        aroma = this.aroma,
        hidratacion = this.hidratacion
    )
}