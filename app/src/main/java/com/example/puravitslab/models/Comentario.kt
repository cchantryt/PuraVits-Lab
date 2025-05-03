package com.example.puravitslab.models

data class Comentario(
    val usuario: String,
    val producto: String,  // Nombre del bálsamo labial (ej: "Pitahaya Vibrante")
    val texto: String,
    val valoracion: Int    // 1-5 estrellas
)