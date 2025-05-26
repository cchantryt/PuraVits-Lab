package com.example.puravitslab.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puravitslab.adapters.HistorialAdapter
import com.example.puravitslab.controllers.HistorialPedidosController
import com.example.puravitslab.databinding.ActivityHistorialPedidosBinding
import com.example.puravitslab.models.Pedido

class HistorialPedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialPedidosBinding
    private lateinit var historialController: HistorialPedidosController // Instancia del controlador
    private lateinit var adapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el controlador
        historialController = HistorialPedidosController()

        binding.backButton.setOnClickListener { finish() }

        // Llamar al método del controlador para cargar el historial
        historialController.cargarHistorial(object : HistorialPedidosController.HistorialPedidosCallback {
            override fun onHistorialCargado(pedidos: List<Pedido>) {
                // Cuando el historial se carga exitosamente desde el controlador
                adapter = HistorialAdapter(pedidos)
                binding.recyclerHistorial.adapter = adapter
                binding.recyclerHistorial.layoutManager = LinearLayoutManager(this@HistorialPedidosActivity)
            }

            override fun onHistorialError(mensaje: String) {
                // Cuando ocurre un error al cargar el historial
                Toast.makeText(this@HistorialPedidosActivity, mensaje, Toast.LENGTH_SHORT).show()
            }
        })
    }
}