package com.laiamenmar.bunkervalencia.model

data class RouteModel(
// val id: Long = System.currentTimeMillis(), guarda la fecha en milisegundos
    val name: String,
    val uid: String = "",
    // val id: Int = 0,
    //  val wall_id: Int =0,
   //   val grade: Int = 0,
    var selected: Boolean = false //check de la card
) {
}