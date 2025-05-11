package com.example.puravitslab.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.puravitslab.MainActivity
import com.example.puravitslab.databinding.ActivityPasarelaPagoBinding
import com.example.puravitslab.models.Pedido
import com.example.puravitslab.models.ProductoPedido
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class PasarelaPagoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasarelaPagoBinding
    private val database = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasarelaPagoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.finishButton.setOnClickListener {
            val direccion = binding.addressInput.text.toString()
            val telefono = binding.phoneInput.text.toString()
            val metodoPago = "Contra Entrega" // Solo hay una opción por ahora

            if (direccion.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            guardarPedido(direccion, telefono, metodoPago)
        }
    }

    private fun guardarPedido(direccion: String, telefono: String, metodoPago: String) {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        // Simula los productos del carrito (deberías obtenerlos del carrito real)
        val productos = listOf(
            ProductoPedido("Bálsamo", 5000.0, 2),
            ProductoPedido("Crema", 3000.0, 1)
        )

        val total = productos.sumOf { it.precio * it.cantidad }
        val fecha = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        val pedido = Pedido(
            fecha = fecha,
            productos = productos,
            total = total,
            direccion = direccion,
            telefono = telefono,
            metodoPago = metodoPago
        )

        // Guardar en Firebase
        database.child("Usuarios").child(userId).child("HistorialPedidos").push()
            .setValue(pedido)
            .addOnSuccessListener {
                Toast.makeText(this, "Pedido guardado con éxito", Toast.LENGTH_SHORT).show()
                irAPantallaPrincipal()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar el pedido: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun irAPantallaPrincipal() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}