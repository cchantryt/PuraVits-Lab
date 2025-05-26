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
            configId = "", // Vacío para productos no personalizados
            cantidad = 1, // Cantidad inicial
            esPersonalizado = false, // Por defecto no es personalizado
            colorPersonalizado = "", // Vacío para productos estándar
            aroma = "", // Vacío para productos estándar
            hidratacion = 3 // Valor por defecto (1-5)
        )

        carritoRef.child(itemId).setValue(item)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    // En CarritoController.kt
    fun agregarProductoPersonalizado(
        producto: ProductoPersonalizado,
        cantidad: Int = 1,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val userId = auth.currentUser?.uid ?: run {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        val carritoRef = database.child("users").child(userId).child("carrito")
        val itemId = database.push().key ?: return

        val item = CarritoItem(
            productoId = itemId,
            nombre = producto.nombre,
            precio = producto.precioBase,
            cantidad = cantidad,
            esPersonalizado = true,
            colorPersonalizado = producto.color,
            aroma = producto.aroma,
            hidratacion = producto.hidratacion
        )

        carritoRef.child(itemId).setValue(item)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun agregarProducto(
        producto: Producto,
        cantidad: Int = 1,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val userId = auth.currentUser?.uid ?: run {
            onFailure(Exception("Usuario no autenticado"))
            return
        }

        val carritoRef = database.child("users").child(userId).child("carrito")
        val itemId = database.push().key ?: return

        val item = CarritoItem(
            productoId = itemId,
            nombre = producto.nombre,
            precio = producto.precio,
            cantidad = cantidad,
            esPersonalizado = false
        )

        carritoRef.child(itemId).setValue(item)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

}