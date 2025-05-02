package com.example.puravitslab.views

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.puravitslab.MainActivity
import com.example.puravitslab.controllers.ComunidadController
import com.example.puravitslab.databinding.ActivityComunidadBinding
import com.example.puravitslab.adapters.ComentarioAdapter

class ComunidadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityComunidadBinding
    private val controller = ComunidadController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComunidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar RecyclerView con datos mock del Controller
        val comentarios = controller.getComentariosMock()
        binding.rvComentarios.layoutManager = LinearLayoutManager(this)
        binding.rvComentarios.adapter = ComentarioAdapter(comentarios)

        binding.backbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}