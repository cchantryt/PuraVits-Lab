package com.example.puravitslab.controllers

import com.example.puravitslab.models.Comentario

class ComunidadController {
    // Simula datos mock (en un caso real, aquí iría la conexión a Firebase/API)
    fun getComentariosMock(): List<Comentario> {
        return listOf(
            Comentario("Ana S.", "Pitahaya Vibrante", "¡El color rosa es perfecto! 🌸", 5),
            Comentario("Carlos M.", "Uchuva Hidratante", "Lo uso después del gym. 💪", 4),
            Comentario("Luisa P.", "Pitahaya Vibrante", "Brillo natural para el día. ✨", 5),
            Comentario("Mario G.", "Mix Tropical", "Huele a vacaciones en la playa. 🏖️", 5),
            Comentario("Sofía R.", "Uchuva Hidratante", "Demasiado suave para mi gusto. 😕", 2),
            Comentario("David L.", "Pitahaya Nocturna", "Perfecto para fiestas. 🌙", 5),
            Comentario("Elena C.", "Mix Tropical", "El envase es adorable. 💝", 4),
            Comentario("Jorge H.", "Uchuva Hidratante", "Carísimo pero efectivo. 💸", 3),
            Comentario("Laura V.", "Pitahaya Vibrante", "¡Mi favorito desde 2024! 💘", 5),
            Comentario("Pablo D.", "Pitahaya Nocturna", "No brilla tanto como esperaba. 🌑", 2)
        )
    }

    //  para enviar comentarios (simulado)
    fun enviarComentario(comentario: Comentario, callback: (Boolean) -> Unit) {
        // En un caso real, aquí se enviaría a Firebase
        callback(true) // Simula éxito
    }
}