package com.example.puravitslab.controllers

import android.content.Context
import androidx.appcompat.app.AlertDialog

class PersonalizacionController(private val context: Context) {

    fun mostrarDialogoConfirmacion(
        nombreBalsamo: String,
        onConfirm: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle("Confirmación")
            .setMessage("¿Estás seguro de guardar '${nombreBalsamo.ifEmpty { "Mi Bálsamo" }}'?")
            .setPositiveButton("Sí") { _, _ -> onConfirm() }
            .setNegativeButton("No", null)
            .show()
    }

    fun validarNombreBalsamo(nombre: String): Boolean {
        return nombre.trim().isNotEmpty()
    }

    fun obtenerNombreBalsamo(nombre: String): String {
        return nombre.trim().ifEmpty { "Mi Bálsamo" }
    }
}