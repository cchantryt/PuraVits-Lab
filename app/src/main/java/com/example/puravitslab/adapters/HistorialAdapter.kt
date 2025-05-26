package com.example.puravitslab.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.puravitslab.R
import com.example.puravitslab.databinding.DialogValoracionBinding
import com.example.puravitslab.databinding.ItemPedidoBinding
import com.example.puravitslab.databinding.ItemProductoHistorialBinding
import com.example.puravitslab.models.Pedido
import com.example.puravitslab.models.ProductoPedido
import com.example.puravitslab.views.HistorialPedidosActivity

class HistorialAdapter(
    private val pedidos: List<Pedido>,
    private val onRepetirPedidoClick: (Pedido) -> Unit,
    private val activity: HistorialPedidosActivity // Ahora es propiedad de clase
) : RecyclerView.Adapter<HistorialAdapter.PedidoViewHolder>() {

    inner class PedidoViewHolder(
        private val binding: ItemPedidoBinding,
        private val activity: HistorialPedidosActivity
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pedido: Pedido) {
            binding.fechaPedido.text = "Fecha: ${pedido.fecha ?: "N/A"}"
            binding.totalPedido.text = "Total: $${"%.2f".format(pedido.total)}"
            binding.estadoPedido.text = "Estado: ${pedido.estado ?: "Pendiente"}"

            binding.btnRepetirPedido.setOnClickListener {
                onRepetirPedidoClick(pedido)
            }

            binding.productosContainer.removeAllViews()
            pedido.productos.forEach { producto ->
                val productoBinding = ItemProductoHistorialBinding.inflate(
                    LayoutInflater.from(activity),
                    binding.productosContainer,
                    false
                )

                with(productoBinding) {
                    // Configurar imagen según el color
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

                    productName.text = producto.nombre
                    productPrice.text = "$${"%.2f".format(producto.precio)} x ${producto.cantidad}"

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

                    if (producto.esPersonalizado) {
                        val nombreColor = getNombreColor(producto.colorPersonalizado)
                        productDetails.text = """
                            • Color: $nombreColor
                            • Aroma: ${producto.aroma.replaceFirstChar { it.uppercase() }}
                            • Nivel de hidratación: ${producto.hidratacion}/5
                            • Precio: $${"%.2f".format(producto.precio)}
                        """.trimIndent()
                        productDetails.visibility = View.VISIBLE
                    } else {
                        productDetails.visibility = View.GONE
                    }

                    btnValorar.setOnClickListener {
                        mostrarDialogoValoracion(producto)
                    }
                }

                binding.productosContainer.addView(productoBinding.root)
            }
        }

        private fun mostrarDialogoValoracion(producto: ProductoPedido) {
            val dialogBinding = DialogValoracionBinding.inflate(LayoutInflater.from(activity))
            var valoracionSeleccionada = 0

            // Configuración del RatingBar
            dialogBinding.ratingBar.apply {
                rating = 0f // Inicia sin estrellas seleccionadas
                stepSize = 1f
                numStars = 5
                setIsIndicator(false)

                setOnRatingBarChangeListener { _, rating, _ ->
                    valoracionSeleccionada = rating.toInt()
                    Log.d("Rating", "Valoración seleccionada: $valoracionSeleccionada")
                }
            }

            val dialog = AlertDialog.Builder(activity)
                .setView(dialogBinding.root)
                .setTitle("¿Cómo calificarías este producto?")
                .setPositiveButton("Enviar") { _, _ ->
                    if (valoracionSeleccionada == 0) {
                        Toast.makeText(activity, "Selecciona al menos 1 estrella", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    val comentario = dialogBinding.etComentario.text.toString()
                    val colorCodificado = producto.colorPersonalizado.replace("#", "COLOR")
                    val productoId = "${colorCodificado}_${producto.aroma}_${producto.hidratacion}_${producto.precio.toInt()}"

                    activity.guardarValoracion(
                        productoId,
                        comentario,
                        valoracionSeleccionada,
                        producto.colorPersonalizado,
                        producto.aroma,
                        producto.hidratacion,
                        producto.precio
                    )
                }
                .setNegativeButton("Cancelar", null)
                .create()

            // Mostrar teclado automáticamente para el comentario
            dialogBinding.etComentario.requestFocus()
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

            dialog.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val binding = ItemPedidoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PedidoViewHolder(binding, activity)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        holder.bind(pedidos[position])
    }
    private fun getNombreColor(colorHex: String): String {
        return when(colorHex.uppercase()) {
            "#FF0000" -> "Rojo"
            "#00FF00" -> "Verde"
            "#0000FF" -> "Azul"
            else -> "Color personalizado"
        }
    }
    override fun getItemCount(): Int = pedidos.size
}