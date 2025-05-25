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
    private var colorSeleccionado: String = "#FF0000" // Rojo por defecto

    fun setColorSeleccionado(colorHex: String) {
        colorSeleccionado = colorHex
    }

    fun mostrarDialogoConfirmacion(
        nombreBalsamo: String,
        onConfirm: () -> Unit,
        onCancel: (() -> Unit)? = null
    ) {
        AlertDialog.Builder(context)
            .setTitle("Confirmación")
            .setMessage("¿Estás seguro de guardar '${nombreBalsamo.ifEmpty { "Mi Bálsamo" }}'?")
            .setPositiveButton("Sí") { _, _ -> onConfirm() }
            .setNegativeButton("No") { _, _ -> onCancel?.invoke() }
            .setCancelable(false)
            .show()
    }

    fun validarNombreBalsamo(nombre: String): Boolean {
        return nombre.trim().length in 2..30 // Entre 2 y 30 caracteres
    }

    fun obtenerNombreBalsamo(nombre: String): String {
        return nombre.trim().ifEmpty { "Mi Bálsamo" }
    }

    fun guardarProductoPersonalizado(
        nombre: String,
        onSuccess: (productoId: String) -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: run {
            onFailure("Usuario no autenticado")
            return
        }

        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val producto = ProductoPersonalizado(
            nombre = obtenerNombreBalsamo(nombre),
            color = colorSeleccionado,
            usuarioId = userId,
            fechaCreacion = fecha
        )

        val newRef = database.child("ProductosPersonalizados").child(userId).push()
        newRef.setValue(producto.toMap())
            .addOnSuccessListener { onSuccess(newRef.key ?: "") }
            .addOnFailureListener { e -> onFailure(e.message ?: "Error al guardar el producto") }
    }
}