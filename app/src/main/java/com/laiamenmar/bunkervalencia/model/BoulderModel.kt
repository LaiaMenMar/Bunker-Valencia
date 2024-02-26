package com.laiamenmar.bunkervalencia.model

data class BoulderModel(
    val name: String,
    val uid: String = "",
    // val id: Int = 0,
    //  val wall_id: Int =0,
    //   val grade: Int = 0,
    var selected: Boolean = false //check de la card
)
