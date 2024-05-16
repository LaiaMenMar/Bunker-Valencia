/**
 * RealtimeManager.kt: Este archivo contiene la clase `RealtimeManager`, que gestiona las
 * operaciones en tiempo real con la base de datos de Firebase.
 *
 * Autor: Laia Méndez Martínez
 * Función: Proporciona métodos para interactuar con la base de datos en tiempo real,
 * como la creación de usuarios, obtención de datos de usuario y gestión de búlderes.
 * Fecha de creación: 2024-01-10
 */

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
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * RealtimeManager: Esta clase gestiona las operaciones en tiempo real con la base de datos
 * de Firebase.
 * Proporciona métodos para crear usuarios y obtener información de usuarios.
 * También maneja la lógica para interactuar con la base de datos de búlderes.
 *
 * @property usersReference Referencia a la base de datos de usuarios en Firebase.
 * @property boulderReference Referencia a la base de datos de búlderes en Firebase.
 */
class RealtimeManager() {
    private val usersReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("users")
    private val boulderReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("boulders")

    /**
     *
     * Colección Users
     *
     * */

    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param userId ID único del usuario.
     * @param displayName Nombre para mostrar del usuario.
     * @param email Correo electrónico del usuario.
     * @param urlPhoto URL de la foto de perfil del usuario.
     */
    suspend fun createUser(
        userId: String,
        displayName: String?,
        email: String?,
        urlPhoto: String?
    ) {
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
                    Log.d(
                        "Laia_RealtimeManager", "Usuario agregado con éxito a la base " +
                                "de datos"
                    )
                }
                .addOnFailureListener { e ->
                    Log.e(
                        "Laia_RealtimeManager", "Error al agregar usuario a la base " +
                                "de datos", e
                    )
                }
        }
    }

    /**
     * Obtiene la información del usuario actual de la base de datos.
     *
     * @param userId ID único del usuario actual a obtener.
     * @return Objeto UserModel que representa al usuario actual, o null si no se encuentra.
     * Esta función se utiliza para obtener el usuario actual y proporcionarlo al HomeViewModel.
     */
    suspend fun getuser(userId: String): UserModel? {
        val userRef = usersReference.child(userId)
        val completableDeferred = CompletableDeferred<UserModel?>()

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(UserModel::class.java)
                completableDeferred.complete(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                completableDeferred.complete(null)
            }
        })
        return completableDeferred.await()
    }

    /**
     * Obtiene el nombre de usuario asociado a un ID desde la base de datos para añadirlo
     * como atributo al boulder.
     *
     * @param userId ID único del usuario del que se desea obtener el nombre.
     * @return Nombre de usuario correspondiente al ID proporcionado, o null si el usuario no existe.
     * Esta función se utiliza para obtener el nombre de usuario a partir de su ID para su uso
     * en la aplicación.
     */
    private suspend fun getUserNameById(userId: String): String? {
        return try {
            val userSnapshot = usersReference.child(userId).get().await()
            if (userSnapshot.exists()) {
                userSnapshot.child("display_name").value.toString()
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(
                "Laia_RealtimeManager", "Error al obtener el nombre del equipador " +
                        "al añadir el bloque $e", e
            )
            null
        }
    }

    /**
     * Actualiza el valor del atributo 'router_setter' para un usuario específico en la base
     * de datos.
     *
     * @param userId ID único del usuario cuyo atributo 'router_setter' se actualizará.
     * @param newRouterSetterValue Nuevo valor del atributo 'router_setter'.
     * Esta función se utiliza para actualizar el valor del atributo 'router_setter' de un usuario
     * en la base de datos y darle permisos de equipador.
     */
    fun updateUserRouterSetter(userId: String, newRouterSetterValue: Boolean) {
        usersReference.child(userId).child("router_setter").setValue(newRouterSetterValue)
            .addOnSuccessListener {
                Log.d(
                    "Laia_RealtimeManager", "Valor de router_setter actualizado " +
                            "con éxito en la base de datos"
                )
            }
            .addOnFailureListener { e ->
                Log.e(
                    "Laia_RealtimeManager", "Error al actualizar valor de router_setter" +
                            " en la base de datos", e
                )
            }
    }

    /**
     * Obtiene un flujo que emite una lista de UserModel reflejando los datos de usuarios en
     * tiempo real en la base de datos ordenados por nombre.
     * Cada vez que hay un cambio en los datos de usuarios en la base de datos,
     * el flujo emite una lista actualizada de UserModel.
     * Este método permite observar cambios en los datos de usuarios en tiempo real.
     * Se utiliza para permitir que el administrador elija a los equipadores mientras observa
     * los cambios en tiempo real de los datos de usuarios.
     *
     * @return Un flujo de lista de UserModel que refleja los datos de usuarios en tiempo real
     * en la base de datos.
     */
    fun getUsersFlow(): Flow<List<UserModel>> {
        val flow = callbackFlow {
            val listener = usersReference
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val users = snapshot.children
                            .mapNotNull { snapshot ->
                                val user = snapshot.getValue(UserModel::class.java)
                                snapshot.key?.let { key ->
                                    user?.copy(user_id = key)
                                }
                            }
                            .sortedBy { it.display_name }
                        trySend(users).isSuccess
                    }

                    override fun onCancelled(error: DatabaseError) {
                        close(error.toException())
                    }
                })
            awaitClose { usersReference.removeEventListener(listener) }
        }
        return flow
    }

    /**
     *
     * Colección Boulders
     *
     * */

    /**
     * Agrega un nuevo boulder a la base de datos en tiempo real.
     * El nombre del equipador se obtiene utilizando el ID del usuario asociado al boulder.
     * Este método actualiza el nombre del equipador en el modelo del boulder
     * y luego lo guarda en la base de datos.
     *
     * @param boulder El boulder que se va a agregar a la base de datos.
     */
    suspend fun addBoulder(boulder: BoulderModel) {
        val name = getUserNameById(boulder.uid_routeSeter)
        val boulderWithName = name?.let { boulder.copy(name_routeSeter = it) }
        val key = boulderReference.push().key
        if (key != null) {
            boulderReference.child(key).setValue(boulderWithName)
        }
    }

    /**
     * Elimina un boulder de la base de datos en tiempo real.
     *
     * @param boulderKey La clave del boulder que se va a eliminar.
     */
    fun deleteBoulder(boulderKey: String) {
        boulderReference.child(boulderKey).removeValue()
    }

    /**
     * Actualiza la información de un boulder en la base de datos en tiempo real.
     *
     * @param updatedBoulder El boulder actualizado que se va a guardar en la base de datos.
     */
    fun updateBoulder(updatedBoulder: BoulderModel) {
        updatedBoulder?.key?.let {
            boulderReference.child(it).setValue(updatedBoulder)
                .addOnSuccessListener {
                    Log.d(
                        "Laia_RealtimeManager",
                        "Boulder actualizado con éxito en" +
                                " la base de datos " +
                                "${updatedBoulder.grade} la key ${updatedBoulder.key} "
                    )
                }
                .addOnFailureListener { e ->
                    // Manejar el fallo de la operación, si es necesario
                    Log.e(
                        "Laia_RealtimeManager", "Error al actualizar el boulder en" +
                                " la base de datos", e
                    )
                }
        }
    }


    /**
     * Obtiene un flujo de la lista de boulders almacenados en la base de datos en tiempo real.
     * La lista de boulders se ordena por ID en orden descendente correspondiente
     * a la fecha de creación.
     *
     * @return Flujo de lista de objetos BoulderModel representando los boulders almacenados
     * en la base de datos.
     */

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