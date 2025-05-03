package com.example.puravitslab.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.puravitslab.databinding.ItemComentarioBinding
import com.example.puravitslab.models.Comentario

class ComentarioAdapter(private val comentarios: MutableList<Comentario>) :
    RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder>() {

    inner class ComentarioViewHolder(val binding: ItemComentarioBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val binding = ItemComentarioBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ComentarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {
        val comentario = comentarios[position]
        holder.binding.tvUsuario.text = comentario.usuario
        holder.binding.tvProducto.text = comentario.producto
        holder.binding.tvComentario.text = comentario.texto
        holder.binding.tvValoracion.text = "⭐".repeat(comentario.valoracion)
    }

    override fun getItemCount() = comentarios.size

    //  para añadir nuevos comentarios
    fun agregarComentario(nuevoComentario: Comentario) {
        comentarios.add(0, nuevoComentario) // Añade al inicio de la lista
        notifyItemInserted(0)
    }
}