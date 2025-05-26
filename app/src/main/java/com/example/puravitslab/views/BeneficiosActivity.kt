package com.example.puravitslab.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.puravitslab.MainActivity
import com.example.puravitslab.controllers.BeneficiosController
import com.example.puravitslab.databinding.ActivityBeneficiosBinding

class BeneficiosActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBeneficiosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeneficiosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Acción del botón atrás
        binding.backbutton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}