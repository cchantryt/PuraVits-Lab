package com.example.puravitslab.utils

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.puravitslab.R
import com.example.puravitslab.views.BeneficiosActivity
import com.example.puravitslab.views.ComunidadActivity
import com.example.puravitslab.views.ProductosActivity
import com.example.puravitslab.views.PersonalizaciónActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class Navigation : AppCompatActivity() {

    protected fun setupBottomNavigation(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_customize -> {
                    if (this::class.java != PersonalizaciónActivity::class.java) {
                        startActivity(Intent(this, PersonalizaciónActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    true
                }
                R.id.nav_benefits -> {
                    if (this::class.java != BeneficiosActivity::class.java) {
                        startActivity(Intent(this, BeneficiosActivity::class.java))
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    true
                }
                R.id.nav_products -> {
                    if (this::class.java != ProductosActivity::class.java) {
                        startActivity(Intent(this, ProductosActivity::class.java))
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
                else -> false
            }
        }
    }
}