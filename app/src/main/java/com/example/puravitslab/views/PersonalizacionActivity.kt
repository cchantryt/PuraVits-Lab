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
import com.example.puravitslab.models.ProductoPersonalizado
import com.google.firebase.auth.FirebaseAuth

class PersonalizacionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPersonalizacionBinding
    private lateinit var controller: PersonalizacionController
    private var colorSeleccionado: String = "#FF0000"

    companion object {
        private val COLOR_MAP = mapOf(
            R.color.red to "#FF0000",
            R.color.green to "#00FF00",
            R.color.blue to "#0000FF"
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalizacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        controller = PersonalizacionController(this)
        setupColorButtons()
        setupSaveButton()
        setupBackButton()
    }

    private fun setupColorButtons() {
        binding.buttonRed.setOnClickListener { setColor(R.color.red) }
        binding.buttonGreen.setOnClickListener { setColor(R.color.green) }
        binding.buttonBlue.setOnClickListener { setColor(R.color.blue) }
    }

    private fun setColor(colorResId: Int) {
        COLOR_MAP[colorResId]?.let { hexColor ->
            colorSeleccionado = hexColor
            val color = ContextCompat.getColor(this, colorResId)
            binding.balmBase.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            controller.setColorSeleccionado(hexColor)
        }
    }

    private fun setupSaveButton() {
        binding.buttonSave.setOnClickListener {
            val nombreBalsamo = binding.editBalmName.text.toString()

            if (!controller.validarNombreBalsamo(nombreBalsamo)) {
                showError("El nombre debe tener entre 2 y 30 caracteres")
                return@setOnClickListener
            }

            confirmarGuardado(nombreBalsamo)
        }
    }

    private fun confirmarGuardado(nombreBalsamo: String) {
        controller.mostrarDialogoConfirmacion(
            nombreBalsamo = nombreBalsamo,
            onConfirm = {
                // Mostrar progreso
                binding.buttonSave.isEnabled = false

                guardarProducto(nombreBalsamo)
            },
            onCancel = {
                showMessage("Creación cancelada")
            }
        )
    }

    private fun guardarProducto(nombreBalsamo: String) {
        controller.guardarProductoPersonalizado(
            nombre = nombreBalsamo,
            onSuccess = { producto ->
                showSuccess("¡Bálsamo creado!")
                setResult(RESULT_OK)
                finish()
            },
            onFailure = { error ->
                showError(error)
                binding.buttonSave.isEnabled = true
            }
        )
    }

    private fun setupBackButton() {
        binding.backbutton.setOnClickListener {
            finish()
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccess(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}