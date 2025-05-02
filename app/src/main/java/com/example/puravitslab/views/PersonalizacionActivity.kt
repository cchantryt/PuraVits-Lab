package com.example.puravitslab.views

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.puravitslab.R
import com.example.puravitslab.controllers.PersonalizacionController
import com.example.puravitslab.databinding.ActivityPersonalizacionBinding
import com.example.puravitslab.MainActivity

class PersonalizacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalizacionBinding
    private lateinit var controller: PersonalizacionController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = PersonalizacionController(this)

        // Botones de color
        binding.buttonRed.setOnClickListener { changeBaseColor(R.color.red) }
        binding.buttonGreen.setOnClickListener { changeBaseColor(R.color.green) }
        binding.buttonBlue.setOnClickListener { changeBaseColor(R.color.blue) }

        binding.buttonSave.setOnClickListener {
            val nombreBalsamo = binding.editBalmName.text.toString()
            if (controller.validarNombreBalsamo(nombreBalsamo)) {
                guardarPersonalizacion(nombreBalsamo)
            } else {
                Toast.makeText(this, "Por favor ingresa un nombre válido", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backbutton.setOnClickListener {
            volverAMainActivity()
        }
    }

    private fun changeBaseColor(colorResId: Int) {
        val color = ContextCompat.getColor(this, colorResId)
        binding.balmBase.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

    private fun guardarPersonalizacion(nombreBalsamo: String) {
        controller.mostrarDialogoConfirmacion(nombreBalsamo) {
            val nombreFinal = controller.obtenerNombreBalsamo(nombreBalsamo)
            binding.nombrebalsamo.text = nombreFinal

            // Aquí podrías guardar en SharedPreferences o Firebase
            // controller.guardarEnBD(nombreFinal, colorSeleccionado)

            volverAMainActivity()
        }
    }

    private fun volverAMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}