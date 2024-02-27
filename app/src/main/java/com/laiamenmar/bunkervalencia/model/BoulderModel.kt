package com.laiamenmar.bunkervalencia.model

data class BoulderModel(
    val uid: Long = System.currentTimeMillis(),
    val boulder: String,
    //val uid: String = "",
    // val id: Int = 0,
    // val wall_id: Int =0,
    //   val grade: Int = 0,
    //equipados, repeticiones, grado, fehca, activo

    var selected: Boolean = false //check de la card
)
