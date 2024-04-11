package com.laiamenmar.bunkervalencia.model

data class BoulderModel(
    val key: String? = null,
    val id: Long = System.currentTimeMillis(),
    val note: String,
    val uid_routeSeter: String = "",
    val wall_id: String,
    val grade: String,
    val active: Boolean,
    val color: String = "",
    val likes: Int = 0,
    val ascents: Int = 0,
    val name_routeSeter: String = ""
   // var selected: () -> Unit = false //check de la card

){
    constructor() : this("", 0, "", "", "", "", true)
}
