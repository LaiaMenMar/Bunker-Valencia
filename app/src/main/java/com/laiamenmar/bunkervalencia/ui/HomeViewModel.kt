/**
 * HomeViewModel.kt: Este archivo contiene la implementación del ViewModel para la pantalla de
 * inicio.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define el ViewModel para la pantalla de inicio, proporcionando lógica para la gestión
 * de datos y eventos relacionados con la interfaz de usuario.
 * Fecha de creación: 2024-02-26
 */
package com.laiamenmar.bunkervalencia.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laiamenmar.bunkervalencia.model.BoulderModel
import com.laiamenmar.bunkervalencia.model.UserModel
import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ViewModel para la pantalla de inicio.
 * Proporciona la lógica para gestionar la pantalla de inicio, incluida la gestión de datos y
 * eventos relacionados con la interfaz de usuario y la gestión de imagenes.
 */
class HomeViewModel() : ViewModel() {
    /**
     * @property gallery Flujo compartido de imágenes para la visualización del búlder.
     * @property selectedBoulder LiveData del búlder seleccionado para ver la imagen.
     */
    private val _gallery = MutableSharedFlow<List<String>>()
    val gallery: Flow<List<String>> = _gallery

    private val _selectedBoulder = MutableLiveData<BoulderModel?>()
    val selectedBoulder: LiveData<BoulderModel?> = _selectedBoulder

    /**
     * Actualiza el flujo de imágenes del búlder.
     *
     * @param images Lista de URLs de imágenes correspondientes al búlder seleccionado.
     */
    suspend fun updateGallery(images: List<String>) {
        _gallery.emit(images)
    }

    /**
     * Establece el búlder seleccionado para ver su imagen en la vista de detalle.
     *
     * @param boulder El búlder seleccionado del que se mostrará la imagen. Puede ser nulo si no
     * hay ningún búlder seleccionado.
     */
    fun setSelectedBoulder(boulder: BoulderModel?) {
        _selectedBoulder.value = boulder
    }

    /**
     * @property currentUser LiveData del usuario actual.
     */
    private val _currentUser = MutableLiveData<UserModel>()
    val currentUser: LiveData<UserModel> = _currentUser

    /**
     * Establece el usuario actual.
     *
     * @param user El objeto UserModel que representa al usuario actual.
     */
    fun setCurrentUser(user: UserModel) {
        _currentUser.value = user
    }

    /**
     * Parámetros para la gestión de los dialogos.
     *
     * @property dialogCloseApp LiveData para controlar el diálogo de cierre de la aplicación
     * desde HomeScreen.
     * @property dialogAddBoulder LiveData para controlar el diálogo de añadir un búlder desde
     * BouldersScreen.
     */
    private val _dialogCloseApp = MutableLiveData<Boolean>()
    val dialogCloseApp: LiveData<Boolean> = _dialogCloseApp

    private val _dialogAddBoulder = MutableLiveData<Boolean>()
    val dialogAddBoulder: LiveData<Boolean> = _dialogAddBoulder

    /**
     * Muestra el diálogo para cerrar la aplicación.
     */
    fun dialogCloseApp_show() {
        _dialogCloseApp.value = true
    }

    /**
     * Cierra el diálogo para cerrar la aplicación.
     */
    fun dialogCloseApp_close() {
        _dialogCloseApp.value = false
    }

    /**
     * Cierra el diálogo para añadir un búlder.
     */
    fun dialogAddBoulder_close() {
        _dialogAddBoulder.value = false
    }

    /**
     * Muestra el diálogo para añadir un búlder.
     */
    fun dialogAddBoulder_show() {
        _dialogAddBoulder.value = true
    }

    /**
     * Objeto búlder.
     *
     * @property boulder LiveData para el búlder actual.
     * @property boulderUpdate LiveData para el búlder actualizado.
     * @property bouldersListFlow Flujo de estado para la lista de búlderes.
     */
    private val _boulder = MutableLiveData<BoulderModel>()
    val boulder: LiveData<BoulderModel> = _boulder

    private val _boulderUpdate = MutableLiveData<BoulderModel>()
    val boulderUpdate: LiveData<BoulderModel> = _boulderUpdate

    private val _bouldersListFlow = MutableStateFlow<List<BoulderModel>>(emptyList())
    val bouldersListFlow: StateFlow<List<BoulderModel>> = _bouldersListFlow

    /**
     * Parámetros del objeto búlder.
     *
     * @property noteInput LiveData para la nota del búlder.
     * @property wallInput LiveData para la pared del búlder.
     * @property gradeInput LiveData para la dificultad del búlder.
     * @property gradeColor LiveData para el color de la dificultad del búlder.
     * @property activeInput LiveData para la disponibilidad del búlder.
     */

    private val _noteInput = MutableLiveData<String>()
    val noteInput: LiveData<String> = _noteInput

    private val _wallInput = MutableLiveData<String>()
    val wallInput: LiveData<String> = _wallInput

    private val _gradeInput = MutableLiveData<String>()
    val gradeInput: LiveData<String> = _gradeInput

    private val _gradeColor = MutableLiveData<String>()
    val gradeColor: LiveData<String> = _gradeColor

    private val _activeInput = MutableLiveData<Boolean>()
    val activeInput: LiveData<Boolean> = _activeInput

    /**
     * Parámetros del objeto búlder actualizado.
     *
     * @property noteInput1 LiveData para la nota del búlder actualizado.
     * @property wallInput1 LiveData para la pared del búlder actualizado.
     * @property gradeInput1 LiveData para la dificultad del búlder actualizado.
     * @property gradeColor1 LiveData para el color de la dificultad del búlder actualizado.
     * @property activeInput1 LiveData para la disponibilidad del búlder actualizado.
     */
    private val _noteInput1 = MutableLiveData<String>()
    val noteInput1: LiveData<String> = _noteInput1

    private val _wallInput1 = MutableLiveData<String>()
    val wallInput1: LiveData<String> = _wallInput1

    private val _gradeInput1 = MutableLiveData<String>()
    val gradeInput1: LiveData<String> = _gradeInput1

    private val _gradeColor1 = MutableLiveData<String>()
    val gradeColor1: LiveData<String> = _gradeColor1

    private val _activeInput1 = MutableLiveData<Boolean>()
    val activeInput1: LiveData<Boolean> = _activeInput1

    /**
     * Actualiza los datos del búlder en la vista con los valores proporcionados en BouldersScreen.
     *
     * @param note La nota del búlder.
     * @param wall La pared del búlder.
     * @param active La disponibilidad del búlder.
     * @param grade La dificultad del búlder.
     */
    fun onBoulderChanged(
        note: String,
        wall: String,
        active: Boolean,
        grade: String,
    ) {
        _noteInput.value = note
        _wallInput.value = wall
        _activeInput.value = active
        _gradeInput.value = grade
    }


    fun onBoulderChangedUpdate(
        note: String,
        wall: String,
        active: Boolean,
        grade: String,
    ) {
        _noteInput1.value = note
        _wallInput1.value = wall
        _activeInput1.value = active
        _gradeInput1.value = grade
    }

    /**
     * Actualiza los datos del búlder actualizado en la vista con los valores proporcionados.
     *
     * @param note La nota del búlder actualizado.
     * @param wall La pared del búlder actualizado.
     * @param active La disponibilidad del búlder actualizado.
     * @param grade La dificultad del búlder actualizado.
     */
    fun getBoulder(boulder: BoulderModel) {
        _noteInput1.value = boulder.note
        _wallInput1.value = boulder.wall_id
        _activeInput1.value = boulder.active
        _gradeInput1.value = boulder.grade
    }

    /**
     * Función para crear un nuevo búlder con los datos proporcionados y agregarlo
     * a la base de datos en tiempo real.
     * Utiliza los valores actuales de [_noteInput], [_wallInput], [_gradeInput], [_activeInput],
     * y [_currentUser] y obtiene el [_gradeColor] en función del [_gradeInput]
     * para crear un objeto [BoulderModel] con los valores adecuados
     * y luego, lo agrega a la base de datos mediante [RealtimeManager].
     */
    private fun createBoulder() {
        val color: String = getColorForGrade(_gradeInput.value!!)
        _gradeColor.value = color

        _boulder.value = BoulderModel()

        _boulder.value = BoulderModel(
            note = _noteInput.value.toString(),
            uid_routeSeter = _currentUser.value!!.user_id,
            wall_id = _wallInput.value.toString(),
            grade = _gradeInput.value.toString(),
            active = _activeInput.value!!,
            color = color
        )
    }

    /**
     * Función para actualizar un búlder existente en la base de datos en tiempo real.
     * Utiliza los valores actuales de [_noteInput1], [_wallInput1], [_gradeInput1],
     * [_activeInput1], y  utiliza del [boulderOld] su key, el id y el name_routeSeter
     * para crear un objeto [BoulderModel] actualizado y luego lo envía al [RealtimeManager]
     * para su actualización.
     *
     * @param boulderOld El búlder existente que se va a actualizar.
     */
    private fun updateBoulder(boulderOld: BoulderModel) {
        val color: String = getColorForGrade(_gradeInput1.value!!)
        _gradeColor1.value = color

        _boulderUpdate.value = BoulderModel(
            key = boulderOld.key,
            id = boulderOld.id,
            note = _noteInput1.value.toString(),
            uid_routeSeter = boulderOld.uid_routeSeter,
            wall_id = _wallInput1.value.toString(),
            grade = _gradeInput1.value.toString(),
            active = _activeInput1.value!!,
            color = color,
            name_routeSeter = boulderOld.name_routeSeter
        )
    }

    /**
     * Función para agregar un nuevo búlder a la base de datos en tiempo real.
     * Llama a [createBoulder()] para crear un nuevo búlder y lo agrega a la base de datos
     * utilizando el [RealtimeManager].
     * Para finalizar, se limpian los campos de entrada y se restablecer los valores
     * predeterminados antes de cerrar el diálogo
     *
     * @param realtime El [RealtimeManager] utilizado para la comunicación con la base de
     * datos en tiempo real.
     */
    suspend fun onBoulder_Add(realtime: RealtimeManager) {
        createBoulder()

        realtime.addBoulder(_boulder.value!!)

        _noteInput.value = ""
        _wallInput.value = "Muro 35"
        _activeInput.value = true
        _gradeInput.value = "4a"

        _dialogAddBoulder.value = false
    }

    /**
     * Función para eliminar un búlder de la base de datos en tiempo real.
     * Utiliza la clave del búlder proporcionada para eliminarlo de la base de datos
     * utilizando el [RealtimeManager].
     *
     * @param realtime El [RealtimeManager] utilizado para la comunicación con la base
     * de datos en tiempo real.
     * @param key La clave del búlder que se va a eliminar.
     */
    suspend fun onBoulder_Delete(realtime: RealtimeManager, key: String?) {
        realtime.deleteBoulder(key ?: "")
    }

    /**
     * Función para actualizar un búlder existente en la base de datos en tiempo real.
     * Llama a [updateBoulder()] para crear un objeto [BoulderModel] actualizado y luego
     * lo envía al [RealtimeManager] para su actualización en la base de datos.
     *
     * @param realtime El [RealtimeManager] utilizado para la comunicación con la base de
     * datos en tiempo real.
     * @param boulderOld El búlder existente que se va a actualizar.
     */
    suspend fun onBoulder_Update(realtime: RealtimeManager, boulderOld: BoulderModel) {
        updateBoulder(boulderOld)
        realtime.updateBoulder(_boulderUpdate.value!!)
    }

    /**
     * Función auxiliar para obtener el color correspondiente a una determinada dificultad de
     * búlder.
     * Devuelve el nombre del recurso del color basado en la dificultad proporcionada.
     *
     * @param grade La dificultad del búlder.
     * @return El nombre del recurso del color correspondiente.
     */
    fun getColorForGrade(grade: String): String {
        return when (grade) {
            "4a", "4b", "4c" -> "difficulty_1"
            "5a", "5b", "5c","6a", "6a+" -> "difficulty_2"
             "6b", "6b+", "6c", "6c+"  -> "difficulty_3"
             "7a", "7a+" -> "difficulty_4"
            "7b", "7b+", "7c", "7c+" -> "difficulty_5"
            "8a", "8a+", "8b", "8b+", "8c", "8c+" -> "difficulty_6"
            else -> "Color no definido"
        }
    }

    /**
     * Función auxiliar para obtener una cadena formateada de una marca de tiempo.
     * Convierte una marca de tiempo en un formato de fecha y hora legible.
     *
     * @param timestamp La marca de tiempo en milisegundos.
     * @return La cadena formateada de la marca de tiempo.
     */
    fun get(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}


/*
    fun cleanBoulder() {
        Log.d("Laia", "Estas en el clean  ${_wallInput.value} ${_gradeInput.value}")
        _noteInput.value = ""
        _wallInput.value = "Muro 35"
        _activeInput.value = true
        _gradeInput.value = "4a"
        Log.d("Laia", "Estas en el clean al final ${_wallInput.value} ${_gradeInput.value}")
    }*/

/*   fun onBoulderUpdate(
           note: String,
           wall: String,
           active: Boolean,
           grade: String,
           ui: String,
           boulderOld: BoulderModel
       ) {
           val color: String = getColorForGrade(grade)

           _noteInput1.value = note
           _wallInput1.value = wall
           _activeInput1.value = active
           _gradeInput1.value = grade

           _gradeColor1.value = color

           _boulderUpdate.value = BoulderModel(
               key = boulderOld.key,
               note = note,
               uid_routeSeter = ui,
               wall_id = wall,
               grade = grade,
               active = active,
               color = color
           )
       } */



