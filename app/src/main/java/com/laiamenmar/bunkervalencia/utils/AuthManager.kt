package com.laiamenmar.bunkervalencia.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

sealed class AuthRes <out T> {
    /**
     * Resultado de exito o de error
     */
    data class Success <T> (val data: T): AuthRes <T>()
    data class Error (val errorMessage: String): AuthRes <Nothing>()
}

class AuthManager {
/*
* Instancia mediante delegacion para que sólo se incialice cuando se necesatio
* */
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    suspend fun singInAnonymously(): AuthRes<FirebaseUser> {
        return try {
            val result = auth.signInAnonymously().await() /*devuelve un objeto firebaseuser*/
            AuthRes.Success(result.user?: throw Exception ("Error al iniciar sesión"))
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")

        }



    }
}