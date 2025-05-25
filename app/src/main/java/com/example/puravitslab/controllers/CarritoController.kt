package com.example.puravitslab.controllers

import android.content.Context
import android.widget.Toast
import com.example.puravitslab.models.CarritoItem
import com.example.puravitslab.models.Producto
import com.example.puravitslab.models.ProductoPersonalizado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CarritoController(private val context: Context) {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    fun agregarProducto(
        producto: Producto,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val userId = auth.currentUser?.uid ?: run {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        val carritoRef = database.child("users").child(userId).child("carrito")
        val itemId = producto.id ?: database.push().key ?: return

        val item = CarritoItem(
            productoId = itemId,
            nombre = producto.nombre,
            precio = producto.precio,
            imagenUrl = producto.imagenUrl ?: ""
        )

        carritoRef.child(itemId).setValue(item)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }


}