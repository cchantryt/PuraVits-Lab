package com.example.puravitslab.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puravitslab.adapters.ComentarioAdapter
import com.example.puravitslab.controllers.ComunidadController
import com.example.puravitslab.databinding.ActivityComunidadBinding
import com.example.puravitslab.models.Comentario

class ComunidadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComunidadBinding
    private lateinit var controller: ComunidadController
    private lateinit var adapter: ComentarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComunidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = ComunidadController()
        adapter = ComentarioAdapter(mutableListOf())

        binding.rvComentarios.layoutManager = LinearLayoutManager(this)
        binding.rvComentarios.adapter = adapter

        cargarComentarios()

        binding.backbutton.setOnClickListener { finish() }
    }

    private fun cargarComentarios() {
        controller.cargarComentarios { comentarios ->
            adapter.actualizarLista(comentarios)
        }
    }

}