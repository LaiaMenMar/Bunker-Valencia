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
import javax.inject.Inject

sealed class AuthRes<out T> {
    /**
     * Resultado de exito o de error
     */
    data class Success<T>(val data: T) : AuthRes<T>()
    data class Error(val errorMessage: String) : AuthRes<Nothing>()
}

/*class AuthManager  (private val context: Context) {*/
  //  class AuthManager  @Inject constructor(private val context: Context) {
    class AuthManager (private val context: Context) {
    /*
    * Instancia mediante delegacion para que sólo se incialice cuando se necesatio
    * */
    private val auth: FirebaseAuth by lazy { Firebase.auth }

    private val signInClient = Identity.getSignInClient(context)
    /*
    * las funcoines suspendidas no se puyeden ejecutar directamente desde un composabel
    * */
    suspend fun singInAnonymously(): AuthRes<FirebaseUser> {
        return try {
            val result = auth.signInAnonymously().await()
            AuthRes.Success(result.user ?: throw Exception("Error al iniciar sesión"))
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")

        }
    }

    suspend fun createUserWithEmailandPassword(
        email: String,
        password: String
    ): AuthRes<FirebaseUser?> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success(result.user ?: throw Exception("Error al iniciar sesión"))
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }

    fun signOut() {
        auth.signOut()
        signInClient.signOut()
    }

    /*Ususario actual logeado en la app*/
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthRes<FirebaseUser?> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success(authResult.user)
        } catch(e: Exception) {
            AuthRes.Error(e.message ?: "Error al iniciar sesión")
        }
    }
    suspend fun resetPassword(email: String): AuthRes<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            AuthRes.Success(Unit)
        } catch(e: Exception) {
            AuthRes.Error(e.message ?: "Error al restablecer la contraseña")
        }
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }
    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount>? {
        return try {
            val account = task.getResult(ApiException::class.java)
            AuthRes.Success(account)
        } catch (e: ApiException) {
            AuthRes.Error(e.message ?: "Google sign-in failed.")
        }
    }
    suspend fun signInWithGoogleCredential(credential: AuthCredential): AuthRes<FirebaseUser>? {
        return try {
            val firebaseUser = auth.signInWithCredential(credential).await()
            firebaseUser.user?.let {
                AuthRes.Success(it)
            } ?: throw Exception("Sign in with Google failed.")
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Sign in with Google failed.")
        }
    }

    fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }



}

