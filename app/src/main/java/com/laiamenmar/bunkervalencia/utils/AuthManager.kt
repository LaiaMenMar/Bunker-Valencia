/**
 * AuthManager.kt: Este archivo contiene la clase `AuthManager`, que gestiona la autenticación
 * de usuarios utilizando Firebase Authentication.
 *
 * Autor: Laia Méndez Martínez
 * Función: Proporciona métodos para registrar usuarios, iniciar sesión, cerrar sesión y obtener
 * información sobre el usuario actualmente autenticado.
 * Fecha de creación: 2024-01-11
 */
package com.laiamenmar.bunkervalencia.utils

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.laiamenmar.bunkervalencia.R
import kotlinx.coroutines.tasks.await


/**
 * Clase sellada que representa el resultado de una operación de autenticación.
 *
 * @param T Tipo de datos que puede contener el resultado en caso de éxito.
 *
 * Resultado exitoso que contiene los datos especificados.
 * @property data Datos obtenidos en la operación exitosa.
 *
 * Resultado de error que contiene un mensaje descriptivo.
 * @property errorMessage Mensaje de error que describe la razón del fallo.
 */
sealed class AuthRes<out T> {
    data class Success<T>(val data: T) : AuthRes<T>()
    data class Error(val errorMessage: String) : AuthRes<Nothing>()
}

/**
 * Clase que gestiona las operaciones de autenticación de usuarios utilizando Firebase Authentication.
 *
 * @property context Contexto de la aplicación.
 * @property realtimeManager Instancia de RealtimeManager para realizar operaciones en tiempo real.
 * @property auth Instancia de FirebaseAuth para la autenticación de usuarios.
 * @property signInClient Cliente de inicio de sesión para gestionar la autenticación.
 */
class AuthManager(private val context: Context, private val realtimeManager: RealtimeManager) {
    private val auth: FirebaseAuth by lazy { Firebase.auth }
    private val signInClient = Identity.getSignInClient(context)

    /**
     * Inicia sesión de forma anónima.
     *
     * @return Objeto AuthRes que indica el resultado de la operación.
     */
    suspend fun singInAnonymously(): AuthRes<FirebaseUser> {
        return try {
            val result = auth.signInAnonymously().await()
            AuthRes.Success(
                result.user ?: throw Exception("Error al iniciar sesión de manera anónima")
            )
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión de manera anónima")
        }
    }

    /**
     * Crea un nuevo usuario utilizando su dirección de correo electrónico y contraseña.
     *
     * @param email Dirección de correo electrónico del nuevo usuario.
     * @param password Contraseña del nuevo usuario.
     * @return Objeto AuthRes que indica el resultado de la operación.
     */
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): AuthRes<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()

            if (result != null && result.user != null) {
                val userId = result.user?.uid
                val displayName = result.user?.email?.split("@")?.get(0)

                realtimeManager.createUser(
                    userId.toString(),
                    displayName,
                    result.user?.email.toString(),
                    result.user?.photoUrl.toString()
                )
            }

            AuthRes.Success(
                result.user ?: throw Exception("Error al crear usuario con email y contraseña")
            )

        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al al crear usuario con email y contraseña")
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun signOut() {
        auth.signOut()
        signInClient.signOut()
    }

    /**
     * Obtiene el usuario actualmente autenticado en la aplicación.
     *
     * @return Objeto FirebaseUser que representa al usuario actual, o null si no hay ningún
     * usuario autenticado.
     */
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    /**
     * Inicia sesión con un correo electrónico y una contraseña proporcionados.
     *
     * @param email Dirección de correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @return Objeto AuthRes que representa el resultado de la operación de inicio de sesión.
     * Si la operación es exitosa, contiene el objeto FirebaseUser correspondiente al usuario
     * autenticado.
     * Si la operación falla, contiene un objeto AuthRes.Error con el mensaje de error correspondiente.
     */
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): AuthRes<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión con email y contraseña")
        }
    }

    /**
     * Envía un correo electrónico para restablecer la contraseña del usuario.
     *
     * @param email Dirección de correo electrónico del usuario.
     * @return
     * [AuthRes] que indica el resultado de la operación.
     * Si tiene éxito, devuelve [Unit];
     * de lo contrario, devuelve un mensaje de error.
     */
    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit)
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al enviar email para restablecer la contraseña")
        }
    }
    /**
     * Cliente de inicio de sesión de Google utilizado para iniciar sesión con Google.
     */
    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    /**
     * Maneja el resultado de inicio de sesión de Google.
     *
     * @param task Tarea que contiene el resultado del inicio de sesión.
     * @return Objeto AuthRes que representa el resultado del inicio de sesión con Google.
     */
    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount>? {
        return try {
            val account = task.getResult(ApiException::class.java)
            AuthRes.Success(account)
        } catch (e: ApiException) {
            AuthRes.Error(e.message ?: "Inicio con Google fallido")
        }
    }

    /**
     * Inicia sesión con las credenciales de Google.
     *
     * @param credential Credenciales de autenticación de Google.
     * @return Objeto AuthRes que representa el resultado del inicio de sesión con Google.
     */
    suspend fun signInWithGoogleCredential(credential: AuthCredential): AuthRes<FirebaseUser>? {
        return try {
            val firebaseUser = auth.signInWithCredential(credential).await()
            firebaseUser.user?.let {
                val userId = it.uid
                realtimeManager.createUser(
                    userId.toString(),
                    it.displayName,
                    it.email,
                    it.photoUrl.toString()
                )

                AuthRes.Success(it)
            } ?: throw Exception("Inicio con Google fallido")
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Inicio con Google fallido")
        }
    }

    /**
     * Inicia sesión con Google.
     *
     * @param googleSignInLauncher Lanzador de actividad para el inicio de sesión con Google.
     */
    fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

}

