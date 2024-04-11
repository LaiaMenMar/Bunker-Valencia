package com.laiamenmar.bunkervalencia.model

data class UserModel(
    val user_id: String,
    val display_name: String,
    val email: String,
    val urlPhoto: String?,
    val router_setter: Boolean = false
) {
    fun toMap(): MutableMap<String, Any?> {
        return mutableMapOf(
            "user_id" to this.user_id,
            "display_name" to this.display_name,
            "email" to this.email,
            "urlPhoto" to this.urlPhoto,
            "router_setter" to this.router_setter
        )

    }
}
