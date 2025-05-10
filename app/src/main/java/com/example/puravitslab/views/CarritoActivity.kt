package com.example.puravitslab.views

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
        binding.checkoutButton.setOnClickListener {
            Toast.makeText(this, "Compra finalizada (lógica pendiente)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarCarrito(userId: String) {
        database.orderByChild("userId").equalTo(userId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartItems.clear()
                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(CarritoItem::class.java)
                        item?.let { cartItems.add(it) }
                    }
                    cartAdapter.notifyDataSetChanged()
                    actualizarTotal()
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
        val total = cartItems.sumOf { it.precio }
        binding.totalPrice.text = "Total: $${"%.2f".format(total)}"
    }
}