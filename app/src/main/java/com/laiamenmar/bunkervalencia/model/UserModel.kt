package com.laiamenmar.bunkervalencia.model

data class UserModel(
    val id: String?,
    val userId: String,
    val displayName: String,
    val avatarUrl: String,
    val isRouterSetter: Boolean
) {
    fun toMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "user_id" to this.userId,
            "display_name" to this.displayName,
            "avatar_Url" to this.avatarUrl,
            "router_setter" to this.isRouterSetter
        )

    }

}
