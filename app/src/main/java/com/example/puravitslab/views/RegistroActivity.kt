package com.example.puravitslab.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.puravitslab.controllers.RegistroController
import com.example.puravitslab.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private val controller = RegistroController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.botonRegistro.setOnClickListener {
            val nombre = binding.nombre.text.toString().trim()
            val apellido = binding.apellido.text.toString().trim()
            val email = binding.correo.text.toString().trim()
            val password = binding.contrasena.text.toString().trim()

            if (nombre.isNotEmpty() && apellido.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                controller.registerUser(nombre, apellido, email, password) { success, error ->
                    if (success) {
                        Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
