package com.example.puravitslab.controllers

import android.content.Context
import androidx.appcompat.app.AlertDialog
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

    fun guardarProductoPersonalizado(
        nombre: String,
        onSuccess: (producto: ProductoPersonalizado) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: run {
            onFailure("Debes iniciar sesión primero")
            return
        }

        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val productoId = database.child("users").child(userId).child("productosPersonalizados").push().key
            ?: run {
                onFailure("Error generando ID")
                return
            }

        val producto = ProductoPersonalizado(
            id = productoId,
            nombre = obtenerNombreBalsamo(nombre),
            color = colorSeleccionado,
            usuarioId = userId,
            precioBase = 5000.0,
            fechaCreacion = fecha
        )

        database.child("users").child(userId).child("productosPersonalizados").child(productoId)
            .setValue(producto)
            .addOnSuccessListener { onSuccess(producto) }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Error al guardar. Intenta nuevamente")
            }
    }
}