package com.example.puravitslab.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.puravitslab.R
import com.example.puravitslab.databinding.ItemPedidoBinding
import com.example.puravitslab.models.Pedido

class HistorialAdapter(private val pedidos: List<Pedido>) :
    RecyclerView.Adapter<HistorialAdapter.PedidoViewHolder>() {

    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fecha = itemView.findViewById<TextView>(R.id.fechaPedido)
        val total = itemView.findViewById<TextView>(R.id.totalPedido)
        val estado = itemView.findViewById<TextView>(R.id.estadoPedido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PedidoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pedido, parent, false)
        return PedidoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedido = pedidos[position]
        holder.fecha.text = "Fecha: ${pedido.fecha ?: "N/A"}"
        holder.total.text = "Total: $${pedido.total}"
        holder.estado.text = "Estado: ${pedido.estado ?: "Pendiente"}"
    }

    override fun getItemCount(): Int = pedidos.size
}
