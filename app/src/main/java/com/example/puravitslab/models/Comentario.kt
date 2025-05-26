package com.example.puravitslab.models

import android.os.Parcel
import android.os.Parcelable

data class Comentario(
    var usuario: String = "",
    var email: String = "",
    var productoId: String = "",
    var texto: String = "",
    var valoracion: Int = 0,
    var fecha: String = "",
    var colorHex: String = "",  // Guardamos el código HEX original
    var colorNombre: String = "", // Guardamos el nombre legible
    var aroma: String = "",
    var hidratacion: Int = 0,
    var precio: Double = 0.0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(usuario)
        parcel.writeString(email)
        parcel.writeString(productoId)
        parcel.writeString(texto)
        parcel.writeInt(valoracion)
        parcel.writeString(fecha)
        parcel.writeString(colorHex)
        parcel.writeString(colorNombre)
        parcel.writeString(aroma)
        parcel.writeInt(hidratacion)
        parcel.writeDouble(precio)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Comentario> {
        override fun createFromParcel(parcel: Parcel) = Comentario(parcel)
        override fun newArray(size: Int): Array<Comentario?> = arrayOfNulls(size)
    }
}