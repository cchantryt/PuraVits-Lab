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
import kotlin.concurrent.thread

class ProductoAdapter(
    private val context: Context,
    private val productos: List<Producto>,
    private val onAddToCart: (Producto) -> Unit,
    private val onViewBenefits: (Producto) -> Unit
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val binding = ItemProductoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        holder.bind(productos[position])
    }

    override fun getItemCount(): Int = productos.size

    inner class ProductoViewHolder(private val binding: ItemProductoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(producto: Producto) {
            with(binding) {
                productName.text = producto.nombre
                productPrice.text = "$${"%.2f".format(producto.precio)}"

                // Cargar imagen de forma asíncrona sin Glide
                loadImageAsync(producto.imagenUrl)

                addToCartButton.setOnClickListener {
                    if (auth.currentUser != null) {
                        onAddToCart(producto)
                    } else {
                        Toast.makeText(
                            context,
                            "Inicia sesión para agregar al carrito",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                benefitsButton.setOnClickListener {
                    onViewBenefits(producto)
                }
            }
        }

        private fun loadImageAsync(imageUrl: String?) {
            if (imageUrl.isNullOrEmpty()) return

            thread {
                try {
                    val url = java.net.URL(imageUrl)
                    val bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    binding.productImage.post {
                        binding.productImage.setImageBitmap(bitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}