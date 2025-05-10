package com.example.puravitslab.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.puravitslab.databinding.ItemProductoBinding
import com.example.puravitslab.models.Producto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.net.URL
import kotlin.concurrent.thread

class ProductoAdapter(
    private val context: Context,
    private val productos: List<Producto>,
    private val onAddToCart: (Producto) -> Unit,
    private val onViewBenefits: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(context), parent, false)
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.bind(producto)
    }

    override fun getItemCount(): Int = productos.size

    inner class ProductoViewHolder(private val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: Producto) {
            binding.productName.text = producto.nombre
            binding.productPrice.text = "Precio: $${producto.precio}"

            // Cargar imagen desde URL sin Glide
            thread {
                try {
                    val url = URL(producto.imagenUrl)
                    val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    binding.productImage.post {
                        binding.productImage.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            binding.addToCartButton.setOnClickListener {
                val currentUser = FirebaseAuth.getInstance().currentUser

                if (currentUser != null) {
                    val userId = currentUser.uid // ID del usuario autenticado
                    val carritosRef = FirebaseDatabase.getInstance().reference.child("Carritos")

                    // Verificar si el usuario ya tiene un carrito
                    carritosRef.orderByChild("userId").equalTo(userId)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    // Si el carrito ya existe, agregar el producto al carrito existente
                                    val carritoId = snapshot.children.first().key
                                    if (carritoId != null) {
                                        val productoData = mapOf(
                                            "productoId" to producto.id
                                        )
                                        carritosRef.child(carritoId).child("productos").push().setValue(productoData)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Producto agregado al carrito existente", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, "Error al agregar al carrito: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                } else {
                                    // Si no existe un carrito, crear uno nuevo
                                    val carritoId = carritosRef.push().key
                                    if (carritoId != null) {
                                        val carritoData = mapOf(
                                            "userId" to userId,
                                            "productos" to mapOf(
                                                carritosRef.push().key to mapOf(
                                                    "productoId" to producto.id
                                                )
                                            )
                                        )
                                        carritosRef.child(carritoId).setValue(carritoData)
                                            .addOnSuccessListener {
                                                Toast.makeText(context, "Producto agregado al nuevo carrito", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener { e ->
                                                Toast.makeText(context, "Error al crear el carrito: ${e.message}", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(context, "Error al verificar el carrito: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                } else {
                    Toast.makeText(context, "Por favor, inicia sesión para agregar productos al carrito", Toast.LENGTH_SHORT).show()
                }
            }
            binding.benefitsButton.setOnClickListener { onViewBenefits(producto) }
        }
    }
}