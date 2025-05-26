package com.example.puravitslab.models

import android.os.Parcel
import android.os.Parcelable

data class ProductoPedido(
    val nombre: String = "",
    val precio: Double = 0.0,
    val cantidad: Int = 1,
    val esPersonalizado: Boolean = false,
    val colorPersonalizado: String = "",
    val aroma: String = "",
    val hidratacion: Int = 3
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeDouble(precio)
        parcel.writeInt(cantidad)
        parcel.writeByte(if (esPersonalizado) 1 else 0)
        parcel.writeString(colorPersonalizado)
        parcel.writeString(aroma)
        parcel.writeInt(hidratacion)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ProductoPedido> {
        override fun createFromParcel(parcel: Parcel) = ProductoPedido(parcel)
        override fun newArray(size: Int) = arrayOfNulls<ProductoPedido?>(size)
    }
}