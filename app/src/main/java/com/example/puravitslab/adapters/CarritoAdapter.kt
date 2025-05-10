package com.example.puravitslab.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.puravitslab.models.CarritoItem
import com.example.puravitslab.databinding.ItemProductoCarritoBinding

class CarritoAdapter(private val items: List<CarritoItem>, private val onDeleteClick: (CarritoItem) -> Unit) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val binding = ItemProductoCarritoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarritoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, onDeleteClick)
    }

    override fun getItemCount(): Int = items.size

    class CarritoViewHolder(private val binding: ItemProductoCarritoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CarritoItem, onDeleteClick: (CarritoItem) -> Unit) {
            binding.productName.text = item.nombre
            binding.productPrice.text = "$${"%.2f".format(item.precio)}"
            // Aquí puedes cargar la imagen con Glide o Picasso si es necesario
            binding.deleteButton.setOnClickListener { onDeleteClick(item) }
        }
    }
}