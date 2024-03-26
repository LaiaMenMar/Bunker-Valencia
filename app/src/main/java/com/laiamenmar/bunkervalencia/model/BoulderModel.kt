package com.laiamenmar.bunkervalencia.model

data class BoulderModel(
    val key: String? = null,
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val uid_routeSeter: String = "",
    // val id: Int = 0,
    // val wall_id: Int =0,
    //   val grade: Int = 0,
    //equipados, repeticiones, grado, fehca, activo

   // var selected: () -> Unit = false //check de la card
){
    constructor() : this("", 0, "", "")
}
