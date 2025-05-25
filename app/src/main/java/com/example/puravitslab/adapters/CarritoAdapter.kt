package com.example.puravitslab.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.puravitslab.databinding.ItemProductoCarritoBinding
import com.example.puravitslab.models.CarritoItem

class CarritoAdapter(
    private val items: List<CarritoItem>,
    private val onDeleteClick: (CarritoItem) -> Unit,
    private val onPersonalizarClick: (CarritoItem) -> Unit,
    private val onBenefitsClick: (CarritoItem) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    inner class CarritoViewHolder(private val binding: ItemProductoCarritoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarritoItem) {
            binding.productName.text = item.nombre
            binding.productPrice.text = "$${"%.2f".format(item.precio)}"

            // Configurar clics
            binding.deleteButton.setOnClickListener { onDeleteClick(item) }
            binding.botonPersonalizar.setOnClickListener { onPersonalizarClick(item) }
            binding.benefitsButton.setOnClickListener { onBenefitsClick(item) }

            // Ocultar botón de personalizar si no es personalizable
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