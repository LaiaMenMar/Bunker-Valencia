package com.laiamenmar.bunkervalencia.utils

import android.content.Context
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.laiamenmar.bunkervalencia.model.BoulderModel
import com.laiamenmar.bunkervalencia.model.UserModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class RealtimeManager (context: Context) {

    private val usersReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
    private val boulderReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("boulders")

    suspend fun createUser(userId: String, displayName: String?, email: String?, urlPhoto: String?) {
        val userSnapshot = usersReference.child(userId).get().await()
        if (!userSnapshot.exists()) {
            val user = UserModel(
                user_id = userId,
                display_name = displayName.toString(),
                email = email.toString(),
                urlPhoto = urlPhoto.toString()
                ).toMap()

            usersReference.child(userId).setValue(user)
                .addOnSuccessListener {
                    Log.d("RealtimeManager", "Usuario agregado con éxito a la base de datos")
                }
                .addOnFailureListener { e ->
                    // Manejar el fallo de la operación, si es necesario
                    Log.e("RealtimeManager", "Error al agregar usuario a la base de datos", e)
                }
        }
    }

    suspend fun getUserNameById(userId: String): String? {
        return try {
            val userSnapshot = usersReference.child(userId.toString()).get().await()
            if (userSnapshot.exists()) {
                userSnapshot.child("display_name").value.toString()
            } else {
                null // Si el usuario no existe, devuelve null
            }
        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir durante la consulta
            Log.e("laia", "Error al obtener el nombre del equipador al añadir el bloque", e)
            null
        }
    }


    /* Boulder metodos*/
  // fun addBoulder(boulder: LiveData<BoulderModel>) {
    suspend fun addBoulder(boulder: BoulderModel) {
        val name = getUserNameById(boulder.uid_routeSeter)
        val boulderWithName = name?.let { boulder.copy(name_routeSeter = it) }
        val key = boulderReference.push().key
        if (key != null) {
            boulderReference.child(key).setValue(boulderWithName)
        }
    }
    suspend fun deleteBoulder(boulderKey: String) {
        boulderReference.child(boulderKey).removeValue()
    }

    suspend fun updateBoulder(updatedBoulder: BoulderModel) {
        Log.d("laia", "Hola Boulder actualizado con éxito a la base de datos ${updatedBoulder.grade} la key ${updatedBoulder.key} ")

        updatedBoulder?.key?.let {
            boulderReference.child(it).setValue(updatedBoulder)
                .addOnSuccessListener {
                    Log.d("laia", "succes? Boulder actualizado con éxito a la base de datos ${updatedBoulder.grade} la key ${updatedBoulder.key} ")
                }
                .addOnFailureListener { e ->
                    // Manejar el fallo de la operación, si es necesario
                    Log.e("laia", "Error al agregar actualizar el boulder a la base de datos", e)
                }
        }
    }



    /* Listado de boulders, flujo continuo de datos, producir, transformar y consumir de manera asincrona. */

 /*   fun getBouldersFlow(): Flow<List<BoulderModel>> {
        //en el ejemplo sólo se muestran los contactos del ususario logeado
       // val idFilter = authManager.getCurrentUser()?.uid
        val flow = callbackFlow {
            // recibes actualizaciones inmediatas si hay cambio en la bbdd
            val listener = boulderReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //snapshot, captura de los datos en ese momento. Hay que convertirlo en una lista de objetos boulder
                    val boulders = snapshot.children.mapNotNull {  snapshot ->
                        val boulder = snapshot.getValue(BoulderModel::class.java)
                        // para obtener la key del boulder
                        snapshot.key?.let {
                            boulder?.copy(key = it) }
                    }
                    trySend(boulders).isSuccess
                  //  trySend(boulders.filter { it.uid == idFilter }).isSuccess
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            })
            awaitClose { boulderReference.removeEventListener(listener) }
        }
        return flow
    }*/

    fun getBouldersFlow(): Flow<List<BoulderModel>> {
        val flow = callbackFlow {
            val listener = boulderReference
                .orderByChild("id")
                .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val boulders = snapshot.children
                        .mapNotNull { snapshot ->
                            val boulder = snapshot.getValue(BoulderModel::class.java)
                            snapshot.key?.let { key ->
                                boulder?.copy(key = key)
                            }
                        }
                        .sortedByDescending { it.id }
                    trySend(boulders).isSuccess
                   // trySend(boulders.filter { it.uid == idFilter }).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            })
            awaitClose { boulderReference.removeEventListener(listener) }
        }
        return flow
    }
}