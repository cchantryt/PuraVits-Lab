package com.example.puravitslab.controllers

import com.example.puravitslab.models.Comentario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

class ComunidadController {
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    fun cargarComentarios(callback: (List<Comentario>) -> Unit) {
        database.child("valoraciones_productos").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val comentarios = mutableListOf<Comentario>()
                    snapshot.children.forEach { productoSnapshot ->
                        productoSnapshot.child("comentarios").children.forEach { comentarioSnapshot ->
                            comentarioSnapshot.getValue(Comentario::class.java)?.let {
                                comentarios.add(it)
                            }
                        }
                    }
                    callback(comentarios.sortedByDescending { it.fecha })
                }

                override fun onCancelled(error: DatabaseError) {
                    // Puedes manejar el error aquí si es necesario
                }
            }
        )
    }

    fun enviarValoracion(
        productoId: String,
        comentario: String,
        valoracion: Int,
        colorHex: String,
        aroma: String,
        hidratacion: Int,
        precio: Double,
        callback: (Boolean) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: run {
            callback(false)
            return
        }
        val email = auth.currentUser?.email ?: ""

        val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val nuevoComentario = Comentario(
            usuario = auth.currentUser?.displayName ?: "Usuario",
            email = email,
            productoId = productoId,
            texto = comentario,
            valoracion = valoracion,
            fecha = fecha,
            colorHex = colorHex,
            colorNombre = getNombreColor(colorHex),
            aroma = aroma,
            hidratacion = hidratacion,
            precio = precio
        )

        database.child("valoraciones_productos").child(productoId).child("comentarios")
            .child(userId).setValue(nuevoComentario)
            .addOnSuccessListener {
                actualizarPromedio(productoId)
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    private fun getNombreColor(colorHex: String): String {
        return when(colorHex.uppercase()) {
            "#FF0000" -> "Rojo"
            "#00FF00" -> "Verde"
            "#0000FF" -> "Azul"
            else -> "Rojo"
        }
    }

    private fun actualizarPromedio(productoId: String) {
        database.child("valoraciones_productos").child(productoId)
            .child("comentarios").addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var total = 0.0
                        var count = 0
                        snapshot.children.forEach {
                            it.child("valoracion").getValue(Int::class.java)?.let { valoracion ->
                                total += valoracion
                                count++
                            }
                        }
                        val promedio = if (count > 0) total / count else 0.0
                        database.child("valoraciones_productos").child(productoId)
                            .child("promedio").setValue(promedio)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                }
            )
    }
}