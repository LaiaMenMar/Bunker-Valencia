package com.laiamenmar.bunkervalencia.model

data class Note(
    var id: String? = null,
    var userId: String = "",
    val title: String = "",
    val content: String = ""
)