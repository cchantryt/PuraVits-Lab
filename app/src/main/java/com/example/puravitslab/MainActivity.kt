package com.example.puravitslab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puravitslab.adapters.ProductoAdapter
import com.example.puravitslab.controllers.CarritoController
import com.example.puravitslab.databinding.ActivityMainBinding
import com.example.puravitslab.models.Producto
import com.example.puravitslab.utils.Navigation
import com.example.puravitslab.views.PerfilActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : Navigation() {
    private lateinit var binding: ActivityMainBinding
    private val productos = mutableListOf<Producto>()
    private val database = FirebaseDatabase.getInstance().reference.child("Productos")
    private lateinit var carritoController: CarritoController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar controlador del carrito
        carritoController = CarritoController(this)

        setupRecyclerView()
        cargarProductos()
        setupBottomNavigation(binding.bottomNavigationView)
        setupProfileButton()
        binding.googleButton.setOnClickListener {
            val url = "https://vt.tiktok.com/ZShcKA2c1/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(url)
            startActivity(intent)
        }

        binding.googleButton.setOnClickListener {
            val url = "https://vt.tiktok.com/ZShTBUnKn/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewProductos.layoutManager = LinearLayoutManager(this)
        val adapter = ProductoAdapter(
            context = this,
            productos = productos,
            onAddToCart = { producto ->
                carritoController.agregarProducto(producto,
                    onSuccess = {
                        Toast.makeText(this, "${producto.nombre} añadido", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { error ->
                        Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            onViewBenefits = { producto ->
                // Implementar vista de beneficios
                Toast.makeText(this, "Beneficios de ${producto.nombre}", Toast.LENGTH_SHORT).show()
            }
        )
        binding.recyclerViewProductos.adapter = adapter
    }

    private fun agregarAlCarrito(producto: Producto) {
        carritoController.agregarProducto(producto,
            onSuccess = {
                Toast.makeText(this, "${producto.nombre} añadido al carrito", Toast.LENGTH_SHORT).show()
            },
            onFailure = { error ->
                Toast.makeText(this, "Error al añadir: $error", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error al añadir al carrito", error)
            }
        )
    }

    private fun mostrarBeneficios(producto: Producto) {
        Toast.makeText(this, "Mostrando beneficios de ${producto.nombre}", Toast.LENGTH_SHORT).show()
        // Aquí puedes implementar la lógica para mostrar los beneficios
    }

    private fun cargarProductos() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productos.clear()
                for (productoSnapshot in snapshot.children) {
                    productoSnapshot.getValue(Producto::class.java)?.let { producto ->
                        producto.id = productoSnapshot.key ?: "" // Asegurar que el ID esté asignado
                        productos.add(producto)
                        Log.d("MainActivity", "Producto cargado: ${producto.nombre}")
                    }
                }
                (binding.recyclerViewProductos.adapter as? ProductoAdapter)?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error en Firebase: ${error.message}")
            }
        })
    }

    private fun setupProfileButton() {
        binding.profileButton.setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
    }
}