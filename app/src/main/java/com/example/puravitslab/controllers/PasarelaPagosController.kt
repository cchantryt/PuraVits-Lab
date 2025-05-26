package com.example.puravitslab.controllers

import android.content.Context
import com.example.puravitslab.models.CarritoItem
import com.example.puravitslab.models.Pedido
import com.example.puravitslab.models.toProductoPedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class PasarelaPagosController(private val context: Context) {
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    fun procesarPedido(
        direccion: String,
        telefono: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return onFailure("Usuario no autenticado")

        // Obtener productos del carrito
        database.child("users").child(userId).child("carrito").get()
            .addOnSuccessListener { snapshot ->
                val productos = snapshot.children.mapNotNull {
                    it.getValue(CarritoItem::class.java)?.toProductoPedido()
                }

                if (productos.isEmpty()) {
                    onFailure("El carrito está vacío")
                    return@addOnSuccessListener
                }

                val total = productos.sumOf { it.precio * it.cantidad }
                val fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())

                val pedido = Pedido(
                    direccion = direccion,
                    telefono = telefono,
                    metodoPago = "Contra Entrega",
                    estado = "Pendiente",
                    productos = productos,
                    fecha = fecha,
                    total = total,
                    usuarioId = userId
                )

                val pedidoRef = database.child("users").child(userId).child("pedidos").push()
                pedido.id = pedidoRef.key ?: ""

                pedidoRef.setValue(pedido)
                    .addOnSuccessListener {
                        // Limpiar carrito
                        database.child("users").child(userId).child("carrito").removeValue()
                            .addOnSuccessListener { onSuccess() }
                            .addOnFailureListener { onFailure("Error al limpiar carrito") }
                    }
                    .addOnFailureListener { e -> onFailure("Error al guardar pedido: ${e.message}") }
            }
            .addOnFailureListener { onFailure("Error al obtener carrito") }
    }

    fun calcularTotal(callback: (Double) -> Unit) {
        val userId = auth.currentUser?.uid ?: return callback(0.0)

        database.child("users").child(userId).child("carrito").get()
            .addOnSuccessListener { snapshot ->
                val total = snapshot.children.mapNotNull {
                    it.getValue(CarritoItem::class.java)?.toProductoPedido()
                }.sumOf { it.precio * it.cantidad }

                callback(total)
            }
            .addOnFailureListener { callback(0.0) }
    }
}
