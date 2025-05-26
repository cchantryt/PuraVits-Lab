package com.example.puravitslab.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.puravitslab.R
import com.example.puravitslab.databinding.ItemProductoCarritoBinding
import com.example.puravitslab.models.CarritoItem

class CarritoAdapter(
    private val items: List<CarritoItem>,
    private val onDeleteClick: (CarritoItem) -> Unit,
    private val onPersonalizarClick: (CarritoItem) -> Unit,
    private val onBenefitsClick: (CarritoItem) -> Unit,
    private val onCantidadChanged: (CarritoItem, Int) -> Unit // Nueva callback para cambios
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(private val binding: ItemProductoCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarritoItem) {
            binding.productName.text = item.nombre
            binding.productPrice.text = "$${"%.2f".format(item.precio)}"

            binding.txtCantidad.text = item.cantidad.toString()

            // Controles de cantidad
            binding.btnIncrementar.setOnClickListener {
                val nuevaCantidad = item.cantidad + 1
                onCantidadChanged(item, nuevaCantidad)
            }

            binding.btnDecrementar.setOnClickListener {
                val nuevaCantidad = if (item.cantidad > 1) item.cantidad - 1 else 1
                onCantidadChanged(item, nuevaCantidad)
            }

            // Mostrar imagen según el color
            when {
                item.colorPersonalizado.contains("#FF0000") ->
                    binding.productImage.setImageResource(R.drawable.balsamo_rojo)
                item.colorPersonalizado.contains("#0000FF") ->
                    binding.productImage.setImageResource(R.drawable.balsamo_azul)
                item.colorPersonalizado.contains("#00FF00") ->
                    binding.productImage.setImageResource(R.drawable.balsamo_verde)
                else ->
                    binding.productImage.setImageResource(R.drawable.balsamo_rojo) // Imagen por defecto
            }

            // Mostrar emoji del aroma en el TextView específico
            val emojiAroma = when (item.aroma.toLowerCase()) {
                "fresa" -> "🍓"
                "vainilla" -> "🍦"
                "mentolado" -> "🌿"
                "coco" -> "🥥"
                "cereza" -> "🍒"
                "mango" -> "🥭"
                else -> "✨"
            }
            binding.aroma.text = emojiAroma

            // Mostrar detalles (sin el emoji repetido)
            if (item.esPersonalizado) {
                binding.productDetails.text = """
                • Color: ${item.colorPersonalizado}
                • Aroma: ${item.aroma}
                • Hidratación: ${item.hidratacion}/5
            """.trimIndent()
                binding.productDetails.visibility = View.VISIBLE
            } else {
                binding.productDetails.visibility = View.GONE
            }

            // Configurar clics
            binding.deleteButton.setOnClickListener { onDeleteClick(item) }
            binding.botonPersonalizar.setOnClickListener { onPersonalizarClick(item) }
            binding.benefitsButton.setOnClickListener { onBenefitsClick(item) }

            // Ocultar botón de personalizar si ya es personalizado
            binding.botonPersonalizar.visibility = if (item.esPersonalizado) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemProductoCarritoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CarritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}