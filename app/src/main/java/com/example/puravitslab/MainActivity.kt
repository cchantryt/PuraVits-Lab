package com.example.puravitslab

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.puravitslab.databinding.ActivityMainBinding
import com.example.puravitslab.views.RegistroActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.puravitslab.utils.Navigation

class MainActivity : Navigation() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileButton.setOnClickListener{
            startActivity(Intent(this, RegistroActivity::class.java))
            finish()
        }

        //Barra navegacion inferior
        setupBottomNavigation(binding.bottomNavigationView)
    }
}

/* Funcionalidades
* Registro e inicio de sesión - LISTO
* Simulador de color (Filtro IG)
* Personalizacion del balsamo - EN PROCESO
* Pantalla de visualizacion de beneficios - EN PROCESO.
* Sistema de pedidos (pagos contra entrega)
* Seccion feedback y comunidad
* Notificaciones push
* Historial de pedidos
* */