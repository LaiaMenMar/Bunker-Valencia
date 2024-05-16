/**
 * UserModel.kt: Este archivo contiene la implementación de la clase UserModel.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define la clase UserModel para representar la información de los usuarios de la aplicación.
 * Fecha de creación: 2024-04-09
 */

package com.laiamenmar.bunkervalencia.model

/**
 * Modelo de datos para representar un usuario autenticado en la aplicación.
 *
 * @property user_id El ID único del usuario proporcionado por Firebase en la autenticación.
 * @property display_name El nombre del usuario.
 * @property email El correo electrónico del usuario.
 * @property urlPhoto La URL de la foto de perfil del usuario (puede ser null si no está disponible).
 * @property router_setter Indica si el usuario es un equipador de rutas o no (por defecto es false).
 * @constructor Crea un objeto UserModel con los parámetros especificados.
 */
data class UserModel(
    val user_id: String,
    val display_name: String,
    val email: String,
    val urlPhoto: String?,
    val router_setter: Boolean = false
) {

    /**
     * Constructor secundario que inicializa un objeto UserModel con valores predeterminados.
     *
     * @param user_id El ID único del usuario proporcionado por Firebase en la autenticación.
     * @param display_name El nombre del usuario.
     * @param email El correo electrónico del usuario.
     * @param urlPhoto La URL de la foto de perfil del usuario (puede ser null si no está disponible).
     * @param router_setter Indica si el usuario es un equipador de rutas o no (por defecto es false).
     */
    constructor() : this("", "", "", null, false)

    /**
     * Convierte el objeto UserModel a un mapa mutable para facilitar el almacenamiento en la base de datos.
     *
     * @return Un mapa mutable con los datos del usuario.
     */
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
