package com.example.puravitslab.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puravitslab.MainActivity
import com.example.puravitslab.R
import com.example.puravitslab.controllers.ComunidadController
import com.example.puravitslab.databinding.ActivityComunidadBinding
import com.example.puravitslab.adapters.ComentarioAdapter
import com.example.puravitslab.models.Comentario

class ComunidadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComunidadBinding
    private val controller = ComunidadController()
    private lateinit var adapter: ComentarioAdapter
    private val listaComentarios = mutableListOf<Comentario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComunidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar con datos mock
        listaComentarios.addAll(controller.getComentariosMock())

        // Configurar RecyclerView
        adapter = ComentarioAdapter(listaComentarios)
        binding.rvComentarios.layoutManager = LinearLayoutManager(this)
        binding.rvComentarios.adapter = adapter

        binding.fabAddComment.setOnClickListener {
            mostrarDialogoComentario()
        }
        binding.backbutton.setOnClickListener {
            finish()
        }
    }

    private fun mostrarDialogoComentario() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_comment, null)
        val spinner = dialogView.findViewById<Spinner>(R.id.spinnerProductos)

        val productos = arrayOf("Pitahaya Vibrante", "Uchuva Hidratante", "Mix Tropical", "Pitahaya Nocturna")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productos)

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Publicar") { _, _ ->
                val producto = spinner.selectedItem.toString()
                val texto = dialogView.findViewById<EditText>(R.id.etComentario).text.toString()
                val valoracion = dialogView.findViewById<RatingBar>(R.id.ratingBar).rating.toInt()

                // Obtener usuario actual (ejemplo)
                val usuarioActual = "Usuario Demo" // Reemplaza con tu lógica de autenticación

                val nuevoComentario = Comentario(
                    usuario = usuarioActual,
                    producto = producto,
                    texto = texto,
                    valoracion = valoracion
                )

                // adapter
                adapter.agregarComentario(nuevoComentario)
                binding.rvComentarios.smoothScrollToPosition(0)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}