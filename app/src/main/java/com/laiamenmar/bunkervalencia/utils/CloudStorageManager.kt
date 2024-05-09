package com.laiamenmar.bunkervalencia.utils

import android.content.Context
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.io.File

class CloudStorageManager(context: Context,realtimeManager: RealtimeManager) { // borrar el realtime
    private val storage = Firebase.storage
    private val storageRef = storage.reference
    private  val authManager = AuthManager(context, realtimeManager ) //Borrar
    private val userId = authManager.getCurrentUser()?.uid //borrar

    fun getStorageReference(): StorageReference {
        return storageRef.child("photo").child(userId?:"")
    }

    suspend fun  uploadFile(fileName: String, filePath: Uri){
        val fileRef = getStorageReference().child(fileName)
        val uploadTask = fileRef.putFile(filePath)
        uploadTask.await()
    }


    suspend fun getUserImages(): List<String> {
        val imageUrls = mutableListOf<String>()
        val listResult: ListResult = getStorageReference().listAll().await()
        for(item in listResult.items){
            val url = item.downloadUrl.await().toString()
            imageUrls.add(url)
        }
        return imageUrls
    }


   fun getStorageReferenceBoulders(): StorageReference {
        return storageRef.child("boulders")
    }

    suspend fun uploadFileBoulder(fileName: String, filePath: Uri, key: String){
        val fileRef = getStorageReferenceBoulders().child(key).child(fileName)
        val uploadTask = fileRef.putFile(filePath)
        uploadTask.await()
    }

    /*suspend fun getImages(key: String): List<String> {
        val imageUrls = mutableListOf<String>()
        val listResult: ListResult = getStorageReference().child(key).listAll().await()
         for(item in listResult.items){
             val url = item.downloadUrl.await().toString()
             imageUrls.add(url)
         }
        return imageUrls
    }

    suspend fun getImage(key: String): String? {
        val listResult: ListResult = getStorageReference().child(key).list(1).await()
        val imageUrl = listResult.items.firstOrNull()?.downloadUrl?.await()?.toString()
        return imageUrl
    }*/
}



