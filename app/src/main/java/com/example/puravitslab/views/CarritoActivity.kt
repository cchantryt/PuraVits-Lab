package com.example.puravitslab.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puravitslab.adapters.CarritoAdapter
import com.example.puravitslab.controllers.CarritoController
import com.example.puravitslab.databinding.ActivityCarritoBinding
import com.example.puravitslab.models.CarritoItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private lateinit var controller: CarritoController
    private lateinit var adapter: CarritoAdapter
    private var carritoItems: MutableList<CarritoItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = CarritoController(this)
        setupRecyclerView()
        setupButtons()
        cargarCarrito()
        setupAdapter()
        cargarCarrito()
        setupTotal()
    }

    private fun setupRecyclerView() {
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CarritoAdapter(
            items = carritoItems,
            onDeleteClick = { eliminarProducto(it) },
            onPersonalizarClick = { personalizarProducto(it) },
            onBenefitsClick = { verBeneficios(it) },
            onCantidadChanged = { item, nuevaCantidad ->
                // Lógica para actualizar la cantidad
                actualizarCantidadEnFirebase(item, nuevaCantidad)
            }
        )
        binding.cartRecyclerView.adapter = adapter
    }
    private fun actualizarCantidadEnFirebase(item: CarritoItem, nuevaCantidad: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // 1. Actualizar localmente
        val index = carritoItems.indexOfFirst { it.productoId == item.productoId }
        if (index != -1) {
            carritoItems[index] = item.copy(cantidad = nuevaCantidad)
            adapter.notifyItemChanged(index)
            actualizarTotal()
        }

        // 2. Actualizar en Firebase
        FirebaseDatabase.getInstance().reference
            .child("users").child(userId).child("carrito").child(item.productoId)
            .child("cantidad").setValue(nuevaCantidad)
    }

    private fun setupButtons() {
        binding.backbutton.setOnClickListener { finish() }

        binding.finalizarBoton.setOnClickListener {
            if (carritoItems.isNotEmpty()) {
                val intent = Intent(this, PasarelaPagoActivity::class.java).apply {
                    putExtra("total", calcularTotal())
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarCarrito() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            Toast.makeText(this, "Por favor, inicia sesión", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("carrito")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    carritoItems.clear()
                    snapshot.children.forEach { itemSnapshot ->
                        itemSnapshot.getValue(CarritoItem::class.java)?.let {
                            carritoItems.add(it)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    actualizarTotal()

                    if (carritoItems.isEmpty()) {
                        Toast.makeText(this@CarritoActivity, "No hay productos en el carrito", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CarritoActivity, "Error al cargar carrito", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun eliminarProducto(item: CarritoItem) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseDatabase.getInstance().reference
            .child("users")
            .child(userId)
            .child("carrito")
            .child(item.productoId)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
    }

    private fun personalizarProducto(item: CarritoItem) {
        // Implementar lógica de personalización
        Toast.makeText(this, "Personalizar ${item.nombre}", Toast.LENGTH_SHORT).show()
    }

    private fun verBeneficios(item: CarritoItem) {
        // Implementar lógica de beneficios
        Toast.makeText(this, "Beneficios de ${item.nombre}", Toast.LENGTH_SHORT).show()
    }

    private fun actualizarTotal() {
        val total = carritoItems.sumOf { it.precio * it.cantidad }
        binding.totalPrice.text = "Total: $${"%.2f".format(total)}"
    }

    private fun calcularTotal(): Double {
        return carritoItems.sumOf { it.precio }
    }


    private fun setupAdapter() {
        adapter = CarritoAdapter(
            items = carritoItems,
            onDeleteClick = { eliminarProducto(it) },
            onPersonalizarClick = { personalizarProducto(it) },
            onBenefitsClick = { verBeneficios(it) },
            onCantidadChanged = { item, nuevaCantidad ->
                actualizarCantidad(item, nuevaCantidad)
            }
        )
        binding.cartRecyclerView.adapter = adapter
    }

    private fun actualizarCantidad(item: CarritoItem, nuevaCantidad: Int) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Actualizar localmente
        val index = carritoItems.indexOfFirst { it.productoId == item.productoId }
        if (index != -1) {
            carritoItems[index] = item.copy(cantidad = nuevaCantidad)
            adapter.notifyItemChanged(index)
        }

        // Actualizar en Firebase
        FirebaseDatabase.getInstance().reference
            .child("users").child(userId).child("carrito").child(item.productoId)
            .child("cantidad").setValue(nuevaCantidad)

        setupTotal() // Actualizar el total
    }

    private fun setupTotal() {
        val total = carritoItems.sumOf { it.precio * it.cantidad }
        binding.totalPrice.text = "Total: $${"%.2f".format(total)}"

        // Mostrar contador de items en el carrito
        val totalItems = carritoItems.sumOf { it.cantidad }
        supportActionBar?.title = "Carrito ($totalItems items)"
    }
}