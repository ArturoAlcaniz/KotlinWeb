package com.example.PracticaKotlin.model

enum class respuesta(val tipo: String) {
    FAIL("Fallo inesperado"),
    JOINEDCHAT_OK("Se ha unido al chat correctamente"),
    SENTCHAT_OK("Mensaje enviado correctamente"),
    SPAMM("Debes esperar almenos 10 segundos para enviar mas mensajes")
}