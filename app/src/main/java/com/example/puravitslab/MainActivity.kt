package com.example.puravitslab

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puravitslab.adapters.ProductoAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar el RecyclerView
        binding.recyclerViewProductos.layoutManager = LinearLayoutManager(this)

        val adapter = ProductoAdapter(this, productos, { producto ->
            Toast.makeText(this, "${producto.nombre} agregado al carrito", Toast.LENGTH_SHORT).show()
        }, { producto ->
            Toast.makeText(this, "Beneficios de ${producto.nombre}", Toast.LENGTH_SHORT).show()
        })

        binding.recyclerViewProductos.adapter = adapter

        // Cargar productos desde Firebase
        cargarProductos(adapter)
        //Barra navegacion inferior
        setupBottomNavigation(binding.bottomNavigationView)

        binding.profileButton.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.googleButton.setOnClickListener {
            val url = "https://vt.tiktok.com/ZShcKA2c1/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = android.net.Uri.parse(url)
            startActivity(intent)
        }
    }

    private fun cargarProductos(adapter: ProductoAdapter) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productos.clear()
                for (productoSnapshot in snapshot.children) {
                    val producto = productoSnapshot.getValue(Producto::class.java)
                    if (producto != null) {
                        productos.add(producto)
                        Log.d("MainActivity", "Producto cargado: $producto")
                    } else {
                        Log.e("MainActivity", "Producto nulo encontrado en Firebase")
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Error al cargar productos: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("MainActivity", "Error al cargar productos: ${error.message}")
            }
        })
    }
}
/* VIEWS
* Registro e inicio de sesión - LISTO
* Simulador de color (Filtro IG)
* Personalizacion del balsamo - EN PROCESO
* Pantalla de visualizacion de beneficios - EN PROCESO.
* Sistema de pedidos (pagos contra entrega)
* Seccion feedback y comunidad - EN PROCESO
* Notificaciones push
* Historial de pedidos (desde perfilactivity)
*
* FLOW
* Los productos en mainactivity son mostrados en cartas, tiene nombre, imagen del producto, precio, boton "Agregar al carrito" y boton "Beneficios".
* Personalizar trae los productos del carrito de la base de datos y permite cambiar parametros, luego se guardan en la base de datos.
*
* Carrito: No trae los productos de la base de datos
*
* FUNCIONES
* */