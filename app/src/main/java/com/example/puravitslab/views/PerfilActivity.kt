package com.example.puravitslab.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.puravitslab.MainActivity
import com.example.puravitslab.controllers.PerfilController
import com.example.puravitslab.databinding.ActivityPerfilBinding
import com.google.firebase.auth.FirebaseAuth

class PerfilActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityPerfilBinding
    private val controller = PerfilController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar FirebaseAuth
        auth = FirebaseAuth.getInstance()

        // Verificar si el usuario está autenticado
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Redirigir al LoginActivity si no hay usuario autenticado
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        } else {
            // Cargar datos del usuario
            controller.getUserProfile { user, error ->
                if (user != null) {
                    binding.textViewName.text = user.nombre
                    binding.textViewApellido.text = user.apellido
                    binding.textViewEmail.text = user.email
                } else {
                    Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Botón para cerrar sesión
        binding.buttonLogout.setOnClickListener {
            controller.logout()
            finish()
        }

        binding.backbutton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}