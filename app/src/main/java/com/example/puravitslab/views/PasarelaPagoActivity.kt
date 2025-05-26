package com.example.puravitslab.views

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.puravitslab.MainActivity
import com.example.puravitslab.controllers.PasarelaPagosController
import com.example.puravitslab.databinding.ActivityPasarelaPagoBinding
import com.example.puravitslab.models.CarritoItem

class PasarelaPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasarelaPagoBinding
    private lateinit var controller: PasarelaPagosController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasarelaPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = PasarelaPagosController(this)

        binding.backbutton.setOnClickListener { finish() }

        // Mostrar total
        controller.calcularTotal { total ->
            binding.totalText.text = "$total"
        }

        binding.finishButton.setOnClickListener {
            val direccion = binding.addressInput.text.toString().trim()
            val telefono = binding.phoneInput.text.toString().trim()

            if (direccion.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            controller.procesarPedido(
                direccion = direccion,
                telefono = telefono,
                onSuccess = {
                    Toast.makeText(this, "Pedido realizado con éxito", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                },
                onFailure = { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
