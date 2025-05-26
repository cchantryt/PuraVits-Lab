package com.example.puravitslab.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puravitslab.adapters.HistorialAdapter
import com.example.puravitslab.controllers.CarritoController
import com.example.puravitslab.controllers.HistorialPedidosController
import com.example.puravitslab.databinding.ActivityHistorialPedidosBinding
import com.example.puravitslab.models.Pedido
import com.example.puravitslab.models.Producto
import com.example.puravitslab.models.ProductoPersonalizado

class HistorialPedidosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistorialPedidosBinding
    private lateinit var historialController: HistorialPedidosController
    private lateinit var carritoController: CarritoController
    private lateinit var adapter: HistorialAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistorialPedidosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historialController = HistorialPedidosController()
        carritoController = CarritoController(this)

        binding.backButton.setOnClickListener { finish() }

        cargarHistorial()
    }

    private fun cargarHistorial() {
        historialController.cargarHistorial(object : HistorialPedidosController.HistorialPedidosCallback {
            override fun onHistorialCargado(pedidos: List<Pedido>) {
                adapter = HistorialAdapter(pedidos) { pedido ->
                    repetirPedido(pedido)
                }
                binding.recyclerHistorial.adapter = adapter
                binding.recyclerHistorial.layoutManager = LinearLayoutManager(this@HistorialPedidosActivity)
            }

            override fun onHistorialError(mensaje: String) {
                Toast.makeText(this@HistorialPedidosActivity, mensaje, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun repetirPedido(pedido: Pedido) {
        var productosAñadidos = 0
        val totalProductos = pedido.productos.size

        pedido.productos.forEach { producto ->
            if (producto.esPersonalizado) {
                val productoPersonalizado = ProductoPersonalizado(
                    nombre = producto.nombre,
                    color = producto.colorPersonalizado,
                    aroma = producto.aroma,
                    hidratacion = producto.hidratacion,
                    precioBase = producto.precio
                )

                carritoController.agregarProductoPersonalizado(
                    productoPersonalizado,
                    producto.cantidad,
                    onSuccess = {
                        productosAñadidos++
                        if (productosAñadidos == totalProductos) {
                            Toast.makeText(
                                this,
                                "Todos los productos añadidos al carrito",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    onFailure = { e ->
                        Toast.makeText(
                            this,
                            "Error al añadir ${producto.nombre}: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            } else {
                carritoController.agregarProducto(
                    Producto(
                        id = "", // Se generará nuevo ID
                        nombre = producto.nombre,
                        precio = producto.precio
                    ),
                    producto.cantidad,
                    onSuccess = {
                        productosAñadidos++
                        if (productosAñadidos == totalProductos) {
                            Toast.makeText(
                                this,
                                "Todos los productos añadidos al carrito",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    onFailure = { e ->
                        Toast.makeText(
                            this,
                            "Error al añadir ${producto.nombre}: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}