package com.example.puravitslab.models

import android.os.Parcel
import android.os.Parcelable

data class ProductoPedido(
    val nombre: String = "",
    val precio: Double = 0.0,
    val cantidad: Int = 1
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeDouble(precio)
        parcel.writeInt(cantidad)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductoPedido> {
        override fun createFromParcel(parcel: Parcel): ProductoPedido {
            return ProductoPedido(parcel)
        }

        override fun newArray(size: Int): Array<ProductoPedido?> {
            return arrayOfNulls(size)
        }
    }
}