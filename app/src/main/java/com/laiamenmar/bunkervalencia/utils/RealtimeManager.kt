package com.laiamenmar.bunkervalencia.utils

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.laiamenmar.bunkervalencia.model.BoulderModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeManager (context: Context) {
    private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("boulders")
    private val authManager = AuthManager(context)


    /* Boulder metodos*/
  // fun addBoulder(boulder: LiveData<BoulderModel>) {
        fun addBoulder(boulder: BoulderModel) {

        val key = databaseReference.push().key //crea la clave
        if (key != null) {
            databaseReference.child(key).setValue(boulder)
        }
    }

    fun deleteBoulder(boulderKey: String) {
        databaseReference.child(boulderKey).removeValue()
    }

/*
    fun updateContact(boulderKey: String, updatedBoulder: BoulderModel) {
        databaseReference.child(boulderKey).setValue(updatedBoulder)
    }
*/


    /* Listado de boulders, flujo continuo de datos, producir, transformar y consumir de manera asincrona. */

    fun getBouldersFlow(): Flow<List<BoulderModel>> {
        //en el ejemplo sólo se muestran los contactos del ususario logeado
       // val idFilter = authManager.getCurrentUser()?.uid
        val flow = callbackFlow {
            // recibes actualizaciones inmediatas si hay cambio en la bbdd
            val listener = databaseReference.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //snapshot, captura de los datos en ese momento. Hay que convertirlo en una lista de objetos boulder
                    val boulders = snapshot.children.mapNotNull {  snapshot ->
                        val boulder = snapshot.getValue(BoulderModel::class.java)
                        // para obtener la key del boulder
                        snapshot.key?.let { boulder?.copy(key = it) }
                    }
                    trySend(boulders).isSuccess
                  //  trySend(boulders.filter { it.uid == idFilter }).isSuccess
                }
                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            })
            awaitClose { databaseReference.removeEventListener(listener) }
        }
        return flow
    }
}