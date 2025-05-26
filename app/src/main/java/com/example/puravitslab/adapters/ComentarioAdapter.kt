package com.example.puravitslab.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.puravitslab.R
import com.example.puravitslab.databinding.ItemComentarioCompletoBinding
import com.example.puravitslab.models.Comentario

class ComentarioAdapter(private val comentarios: MutableList<Comentario>) :
    RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder>() {

    inner class ComentarioViewHolder(val binding: ItemComentarioCompletoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComentarioViewHolder {
        val binding = ItemComentarioCompletoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ComentarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComentarioViewHolder, position: Int) {
        val comentario = comentarios[position]
        with(holder.binding) {
            tvUsuario.text = comentario.email
            tvValoracion.text = "⭐".repeat(comentario.valoracion)
            tvComentario.text = comentario.texto
            tvFecha.text = comentario.fecha

            // Mostrar características del producto de forma legible
            val emojiAroma = when (comentario.aroma.lowercase()) {
                "fresa" -> "🍓"
                "vainilla" -> "🍦"
                "mentolado" -> "🌿"
                "coco" -> "🥥"
                "cereza" -> "🍒"
                "mango" -> "🥭"
                else -> "✨"
            }

            tvProducto.text = "$emojiAroma ${comentario.aroma.replaceFirstChar { it.uppercase() }} - " +
                    "${comentario.colorNombre} - " +
                    "Hidratación: ${comentario.hidratacion}/5 - " +
                    "$${"%.2f".format(comentario.precio)}"

            // Configurar imagen según el color
            when {
                comentario.colorHex.contains("#FF0000") -> ivProducto.setImageResource(R.drawable.balsamo_rojo)
                comentario.colorHex.contains("#00FF00") -> ivProducto.setImageResource(R.drawable.balsamo_verde)
                comentario.colorHex.contains("#0000FF") -> ivProducto.setImageResource(R.drawable.balsamo_azul)
                else -> ivProducto.setImageResource(R.drawable.balsamo_rojo)
            }
        }
    }

    private fun getEmoji(aroma: String): String {
        return when (aroma.toLowerCase()) {
            "fresa" -> "🍓"
            "vainilla" -> "🍦"
            "mentolado" -> "🌿"
            "coco" -> "🥥"
            "cereza" -> "🍒"
            "mango" -> "🥭"
            else -> "✨"
        }
    }

    override fun getItemCount() = comentarios.size

    fun actualizarLista(nuevaLista: List<Comentario>) {
        comentarios.clear()
        comentarios.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}