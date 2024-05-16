/**
 * CloudStorageManager.kt: Este archivo contiene la clase `CloudStorageManager`, que gestiona el
 * almacenamiento en la nube utilizando Firebase Storage.
 *
 * Autor: Laia Méndez Martínez
 * Función: Proporciona métodos para cargar y eliminar imágenes relacionadas con los búlderes en la
 * nube, así como para obtener las URL de las imágenes almacenadas.
 * También incluye funcionalidades para cargar y obtener imágenes de perfil de usuario.
 * Fecha de creación: 2024-05-10
 */
package com.laiamenmar.bunkervalencia.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

/**
 * Clase que gestiona el almacenamiento en la nube utilizando Firebase Storage para la imagen de
 * los búlderes en la aplicación.
 *
 * @property context Contexto de la aplicación.
 * @property realtimeManager Instancia de RealtimeManager para realizar operaciones en tiempo real.
 *
 * @property storageRef Referencia principal al almacenamiento en la nube.
 */
class CloudStorageManager(
    context: Context,
    realtimeManager: RealtimeManager
) {
    private val storageRef = Firebase.storage.reference

    /**
     * Obtiene una referencia al nodo "boulders" en el almacenamiento de Firebase.
     *
     * @return Referencia al nodo "boulders" en el almacenamiento de Firebase.
     */
    private fun getStorageReferenceBoulders(): StorageReference {
        return storageRef.child("boulders")
    }

    /**
     * Sube un archivo al almacenamiento en la nube para un búlder específico.
     *
     * @param fileName Nombre del archivo a cargar.
     * @param filePath URI del archivo a cargar.
     * @param key Clave única del búlder al que se asociará el archivo.
     */
    suspend fun uploadFileBoulder(fileName: String, filePath: Uri, key: String) {
        val fileRef = getStorageReferenceBoulders().child(key).child(fileName)
        val uploadTask = fileRef.putFile(filePath)
        uploadTask.await()
    }

    /**
     * Elimina todas las imágenes asociadas a un boulder específico del almacenamiento de Firebase.
     *
     * @param key Clave única del boulder del que se eliminarán las imágenes.
     */
    suspend fun deleteBoulderImage(key: String) {
        val listResult: ListResult =
            getStorageReferenceBoulders().child(key ?: "").listAll().await()
        try {
            listResult.items.forEach { item ->
                item.delete()
            }
            getStorageReferenceBoulders().child(key ?: "").delete().await()
        } catch (e: Exception) {
            Log.e(
                "Laia_CloudStorageManager", "Error al borrar las imagenes del boulder $e",
                e
            )
            null
        }
    }

    /**
     * Obtiene las URL de todas las imágenes asociadas a un boulder específico del almacenamiento
     * de Firebase.
     *
     * @param key Clave única del boulder del que se obtendrán las imágenes.
     * @return Lista de cadenas que representan las URL de las imágenes del boulder.
     */
    suspend fun getBoulderImage(key: String): List<String> {
        val imageUrls = mutableListOf<String>()
        val listResult: ListResult =
            getStorageReferenceBoulders().child(key ?: "").listAll().await()
        for (item in listResult.items) {
            val url = item.downloadUrl.await().toString()
            imageUrls.add(url)
        }
        return imageUrls.toList()
    }
}
/*
    private val authManager = AuthManager(context, realtimeManager)
    private val userId = authManager.getCurrentUser()?.uid

    fun getStorageReference(): StorageReference {
        return storageRef.child("photo").child(userId ?: "")
    }

    suspend fun getUserImages(): List<String> {
        val imageUrls = mutableListOf<String>()
        val listResult: ListResult = getStorageReference().listAll().await()
        for (item in listResult.items) {
            val url = item.downloadUrl.await().toString()
            imageUrls.add(url)
        }
        return imageUrls
    }
}*/

