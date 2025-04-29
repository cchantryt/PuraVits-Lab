package com.example.puravitslab.controllers

import com.example.puravitslab.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistroController{

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    fun registerUser(
        nombre: String,
        apellido: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener

                    val user = Usuario(
                        uid = uid,
                        nombre = nombre,
                        apellido = apellido,
                        email = email
                    )

                    // Aquí se guarda en la base de datos
                    database.child(uid).setValue(user)
                        .addOnSuccessListener {
                            onResult(true, null)
                        }
                        .addOnFailureListener { e ->
                            onResult(false, e.message)
                        }
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
}
