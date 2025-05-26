package com.example.puravitslab.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.puravitslab.R
import com.example.puravitslab.databinding.ItemPedidoBinding
import com.example.puravitslab.databinding.ItemProductoHistorialBinding
import com.example.puravitslab.models.Pedido

class HistorialAdapter(private val pedidos: List<Pedido>) :
    RecyclerView.Adapter<HistorialAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(private val binding: ItemPedidoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val productosContainer = binding.productosContainer

        fun bind(pedido: Pedido) {
            binding.fechaPedido.text = "Fecha: ${pedido.fecha ?: "N/A"}"
            binding.totalPedido.text = "Total: $${"%.2f".format(pedido.total)}"
            binding.estadoPedido.text = "Estado: ${pedido.estado ?: "Pendiente"}"

            // Limpiar contenedor de productos
            productosContainer.removeAllViews()

            // Añadir cada producto al contenedor
            pedido.productos.forEach { producto ->
                val productoBinding = ItemProductoHistorialBinding.inflate(
                    LayoutInflater.from(itemView.context),
                    productosContainer,
                    false
                )

                with(productoBinding) {
                    productName.text = producto.nombre
                    productPrice.text = "$${"%.2f".format(producto.precio)} x ${producto.cantidad}"
                    txtCantidad.text = producto.cantidad.toString()

                    // Mostrar emoji del aroma
                    val emojiAroma = when (producto.aroma.toLowerCase()) {
                        "fresa" -> "🍓"
                        "vainilla" -> "🍦"
                        "mentolado" -> "🌿"
                        "coco" -> "🥥"
                        "cereza" -> "🍒"
                        "mango" -> "🥭"
                        else -> "✨"
                    }
                    aroma.text = emojiAroma

                    // Mostrar detalles de personalización si es personalizado
                    if (producto.esPersonalizado) {
                        productDetails.text = """
                            • Color: ${producto.colorPersonalizado}
                            • Aroma: ${producto.aroma}
                            • Hidratación: ${producto.hidratacion}/5
                        """.trimIndent()
                        productDetails.visibility = View.VISIBLE
                    } else {
                        productDetails.visibility = View.GONE
                    }

                    // Mostrar imagen según el color (similar al carrito)
                    when {
                        producto.colorPersonalizado.contains("#FF0000") ->
                            productImage.setImageResource(R.drawable.balsamo_rojo)
                        producto.colorPersonalizado.contains("#0000FF") ->
                            productImage.setImageResource(R.drawable.balsamo_azul)
                        producto.colorPersonalizado.contains("#00FF00") ->
                            productImage.setImageResource(R.drawable.balsamo_verde)
                        else ->
                            productImage.setImageResource(R.drawable.balsamo_rojo)
                    }
                }

                productosContainer.addView(productoBinding.root)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemPedidoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PedidoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        holder.bind(pedidos[position])
    }

    override fun getItemCount(): Int = pedidos.size
}