package com.example.puravitslab.controllers

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.puravitslab.models.CarritoItem
import com.example.puravitslab.models.ProductoPersonalizado
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class PersonalizacionController(private val context: Context) {
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private var colorSeleccionado: String = "#FF0000"

    fun setColorSeleccionado(colorHex: String) {
        colorSeleccionado = colorHex
    }

    fun mostrarDialogoConfirmacion(
        nombreBalsamo: String,
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(context)
            .setTitle("Confirmar creación")
            .setMessage("¿Crear bálsamo '${nombreBalsamo.ifEmpty { "Mi Bálsamo" }}'?")
            .setPositiveButton("Crear") { _, _ -> onConfirm() }
            .setNegativeButton("Cancelar") { _, _ -> onCancel?.invoke() }
            .setCancelable(false)
            .show()
    }

    fun validarNombreBalsamo(nombre: String): Boolean {
        return nombre.trim().length in 2..30
    }

    fun obtenerNombreBalsamo(nombre: String): String {
        return nombre.trim().ifEmpty { "Mi Bálsamo" }
    }

    fun guardarYAgregarAlCarrito(
        nombre: String,
        aroma: String,
        hidratacion: Int,
        onSuccess: (ProductoPersonalizado) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: run {
            onFailure("Usuario no autenticado")
            return
        }

        // 1. Guardar en productos_personalizados
        val productoId = database.child("productos_personalizados").push().key ?: run {
            onFailure("Error generando ID")
            return
        }

        val producto = ProductoPersonalizado(
            id = productoId,
            nombre = nombre,
            color = colorSeleccionado,
            aroma = aroma,
            hidratacion = hidratacion,
            usuarioId = userId,
            precioBase = calcularPrecio(hidratacion),
            fechaCreacion = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        )

        // 2. Guardar producto personalizado
        database.child("productos_personalizados").child(productoId).setValue(producto)
            .addOnSuccessListener {
                // 3. Añadir al carrito usando el nuevo método
                CarritoController(context).agregarProductoPersonalizado(
                    producto = producto,
                    onSuccess = { onSuccess(producto) },
                    onFailure = { e -> onFailure(e.message ?: "Error al añadir al carrito") }
                )
            }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error al guardar el producto") }
    }

    private fun calcularPrecio(hidratacion: Int): Double {
        return 5000.0 + (hidratacion * 1000.0) // Precio base + ajuste por hidratación
    }
}