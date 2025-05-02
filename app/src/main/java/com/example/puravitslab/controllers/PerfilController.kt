package com.example.puravitslab.controllers

import com.example.puravitslab.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PerfilController {
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun getUserProfile(callback: (Usuario?, String?) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid
            database.child("users").child(uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val nombre = snapshot.child("nombre").value as? String ?: "Nombre no disponible"
                    val apellido = snapshot.child("apellido").value as? String ?: "Apellido no disponible"
                    val email = snapshot.child("email").value as? String ?: "Correo no disponible"

                    callback(Usuario(uid, nombre, apellido, email), null)
                } else {
                    callback(null, "Datos del usuario no encontrados")
                }
            }.addOnFailureListener { error ->
                callback(null, "Error al obtener los datos: ${error.message}")
            }
        } else {
            callback(null, "Usuario no autenticado")
        }
    }

    fun logout() {
        auth.signOut()
    }
}