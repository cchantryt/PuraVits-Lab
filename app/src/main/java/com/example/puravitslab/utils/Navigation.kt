package com.example.puravitslab.utils

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.puravitslab.R
import com.example.puravitslab.views.BeneficiosActivity
import com.example.puravitslab.views.CarritoActivity
import com.example.puravitslab.views.ComunidadActivity
import com.example.puravitslab.views.ProductosActivity
import com.example.puravitslab.views.PersonalizacionActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class Navigation : AppCompatActivity() {

    protected fun setupBottomNavigation(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_customize -> {
                    if (this::class.java != PersonalizacionActivity::class.java) {
                        startActivity(Intent(this, PersonalizacionActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    true
                }
                R.id.nav_community -> {
                    if (this::class.java != ComunidadActivity::class.java) {
                        startActivity(Intent(this, ComunidadActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    true
                }
                R.id.carrito -> {
                    if (this::class.java != CarritoActivity::class.java) {
                        startActivity(Intent(this, CarritoActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    true
                }
                else -> false
            }
        }
    }
}