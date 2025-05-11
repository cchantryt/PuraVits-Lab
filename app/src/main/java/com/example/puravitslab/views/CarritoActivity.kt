package com.example.puravitslab.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puravitslab.adapters.CarritoAdapter
import com.example.puravitslab.databinding.ActivityCarritoBinding
import com.example.puravitslab.models.CarritoItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private lateinit var database: DatabaseReference
    private lateinit var cartAdapter: CarritoAdapter
    private var cartItems: MutableList<CarritoItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar RecyclerView
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(this)
        cartAdapter = CarritoAdapter(cartItems) { item ->
            eliminarProducto(item)
        }
        binding.cartRecyclerView.adapter = cartAdapter

        // Configurar Firebase
        database = FirebaseDatabase.getInstance().reference.child("Carritos")
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null) {
            cargarCarrito(currentUser.uid)
        } else {
            Toast.makeText(this, "Por favor, inicia sesión para ver el carrito", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Configurar botón de retroceso
        binding.backbutton.setOnClickListener {
            finish()
        }

        // Configurar botón de finalizar compra
        binding.finalizarBoton.setOnClickListener {
            val intent = Intent(this, PasarelaPagoActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarCarrito(userId: String) {
        database.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartItems.clear()
                    if (snapshot.exists()) {
                        val carritoSnapshot = snapshot.children.first()
                        val productosSnapshot = carritoSnapshot.child("productos")

                        val productosRef = FirebaseDatabase.getInstance().reference.child("Productos")

                        val totalProductos = productosSnapshot.childrenCount.toInt()
                        var productosCargados = 0

                        for (productoItem in productosSnapshot.children) {
                            val productoId = productoItem.child("productoId").getValue(String::class.java)
                            if (productoId != null) {
                                productosRef.child(productoId)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(productoSnapshot: DataSnapshot) {
                                            val producto = productoSnapshot.getValue(CarritoItem::class.java)
                                            if (producto != null) {
                                                producto.id = productoSnapshot.key.toString()
                                                cartItems.add(producto)
                                            }
                                            productosCargados++
                                            if (productosCargados == totalProductos) {
                                                cartAdapter.notifyDataSetChanged()
                                                actualizarTotal()
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                            }
                        }
                    } else {
                        Toast.makeText(this@CarritoActivity, "No se encontró carrito", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@CarritoActivity, "Error al cargar el carrito: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun eliminarProducto(item: CarritoItem) {
        database.child(item.id).removeValue()
            .addOnSuccessListener {
                cartItems.remove(item)
                cartAdapter.notifyDataSetChanged()
                actualizarTotal()
                Toast.makeText(this, "Producto eliminado del carrito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al eliminar producto: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarTotal() {
        val total = cartItems.sumOf { it.precio.toDouble() }
        binding.totalPrice.text = "Total: $${"%.2f".format(total)}"
    }

}