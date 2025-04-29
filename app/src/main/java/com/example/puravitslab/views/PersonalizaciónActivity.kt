package com.example.puravitslab.views

import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.puravitslab.R
import com.example.puravitslab.databinding.ActivityPersonalizacionBinding

class PersonalizaciónActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalizacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón para cambiar color a rojo
        binding.buttonRed.setOnClickListener {
            changeBaseColor(R.color.red) // Color rojo
        }

        // Botón para cambiar color a verde
        binding.buttonGreen.setOnClickListener {
            changeBaseColor(R.color.green) // Color verde
        }

        // Botón para cambiar color a azul
        binding.buttonBlue.setOnClickListener {
            changeBaseColor(R.color.blue) // Color azul
        }

        // Botón guardar
        binding.buttonSave.setOnClickListener {
            // Lógica para guardar el nombre y color seleccionado
            saveCustomization()
        }
    }

    private fun changeBaseColor(colorResId: Int) {
        val color = ContextCompat.getColor(this, colorResId)
        binding.balmBase.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    private fun saveCustomization() {
        // Lógica para guardar la personalización, como el nombre y color del bálsamo
    }
}