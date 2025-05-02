package com.example.puravitslab.controllers

import android.content.Context
import android.content.Intent
import android.graphics.Color
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.puravitslab.R
import com.example.puravitslab.models.Beneficio

class BeneficiosController(private val context: Context, private val container: LinearLayout) {
    private val benefits = listOf(
        Beneficio(
            R.drawable.ic_launcher_foreground,
            "Relaja los músculos y reduce la tensión",
            Color.parseColor("#E8F5E9")
        ),
        Beneficio(
            R.drawable.ic_launcher_foreground,
            "Hidrata y nutre profundamente la piel",
            Color.parseColor("#E3F2FD")
        ),
        Beneficio(
            R.drawable.ic_launcher_foreground,
            "Elaborado con ingredientes naturales",
            Color.parseColor("#FFF3E0")
        )
    )

    fun showBenefits() {
        val inflater = LayoutInflater.from(context)
        benefits.forEach { benefit ->
            val card = createCard(inflater, benefit)
            container.addView(card)
        }
    }

    private fun createCard(inflater: LayoutInflater, benefit: Beneficio): View {
        val card = MaterialCardView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            radius = 16f
            cardElevation = 8f
            setCardBackgroundColor(benefit.backgroundColor)
        }

        val content = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(16, 16, 16, 16)
        }

        val image = ImageView(context).apply {
            setImageResource(benefit.iconResId)
            layoutParams = LinearLayout.LayoutParams(48, 48)
        }

        val text = TextView(context).apply {
            text = benefit.description
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(16, 0, 0, 0)
            }
        }

        content.addView(image)
        content.addView(text)
        card.addView(content)

        return card
    }

    fun shareBenefits(view: View) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Beneficios del Bálsamo")
            putExtra(Intent.EXTRA_TEXT, "Descubre los beneficios del bálsamo natural...")
        }
        context.startActivity(Intent.createChooser(intent, "Compartir con"))
        Snackbar.make(view, "Compartiendo beneficios...", Snackbar.LENGTH_SHORT).show()
    }
}