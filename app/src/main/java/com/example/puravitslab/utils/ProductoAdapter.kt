package com.example.puravitslab.adapters

        import android.content.Context
        import android.graphics.BitmapFactory
        import android.view.LayoutInflater
        import android.view.ViewGroup
        import androidx.recyclerview.widget.RecyclerView
        import com.example.puravitslab.databinding.ItemProductoBinding
        import com.example.puravitslab.models.Producto
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

                    binding.addToCartButton.setOnClickListener { onAddToCart(producto) }
                    binding.benefitsButton.setOnClickListener { onViewBenefits(producto) }
                }
            }
        }