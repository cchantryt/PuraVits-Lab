package com.example.puravitslab.models

import android.os.Parcel
import android.os.Parcelable

data class Pedido(
    var id: String = "",
    val fecha: String = "",
    val productos: List<ProductoPedido> = listOf(),
    val total: Double = 0.0,
    val direccion: String = "",
    val telefono: String = "",
    val metodoPago: String = "",
    val estado: String = "pendiente",
    var usuarioId: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createTypedArrayList(ProductoPedido.CREATOR) ?: listOf(),
        parcel.readDouble(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "pendiente",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(fecha)
        parcel.writeTypedList(productos)
        parcel.writeDouble(total)
        parcel.writeString(direccion)
        parcel.writeString(telefono)
        parcel.writeString(metodoPago)
        parcel.writeString(estado)
        parcel.writeString(usuarioId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Pedido> {
        override fun createFromParcel(parcel: Parcel) = Pedido(parcel)
        override fun newArray(size: Int): Array<Pedido?> = arrayOfNulls(size)
    }

    // Función para convertir productos del pedido a CarritoItems
    fun toCarritoItems(): List<CarritoItem> {
        return productos.map { producto ->
            CarritoItem(
                productoId = "", // Nuevo ID generado al añadir al carrito
                nombre = producto.nombre,
                precio = producto.precio,
                cantidad = producto.cantidad,
                esPersonalizado = producto.esPersonalizado,
                colorPersonalizado = producto.colorPersonalizado,
                aroma = producto.aroma,
                hidratacion = producto.hidratacion
            )
        }
    }
}