package com.example.puravitslab.controllers

import com.example.puravitslab.models.Pedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistorialPedidosController {

    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    // Interfaz de callback para comunicar el resultado a la vista
    interface HistorialPedidosCallback {
        fun onHistorialCargado(pedidos: List<Pedido>)
        fun onHistorialError(mensaje: String)
    }

    fun cargarHistorial(callback: HistorialPedidosCallback) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            callback.onHistorialError("Usuario no autenticado.")
            return
        }

        database.child("users").child(userId).child("pedidos")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val pedidos = snapshot.children.mapNotNull {
                        it.getValue(Pedido::class.java)
                    }.sortedByDescending { it.fecha } // Ordenar por fecha descendente

                    callback.onHistorialCargado(pedidos)
                }

                override fun onCancelled(error: DatabaseError) {
                    callback.onHistorialError("Error al cargar historial: ${error.message}")
                }
            })
    }
}