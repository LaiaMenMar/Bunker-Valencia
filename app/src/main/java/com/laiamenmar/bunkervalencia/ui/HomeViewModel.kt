package com.laiamenmar.bunkervalencia.ui

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laiamenmar.bunkervalencia.model.BoulderModel
import com.laiamenmar.bunkervalencia.model.UserModel
import com.laiamenmar.bunkervalencia.ui.theme.*
import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(): ViewModel() {

    //   class HomeViewModel(private val realtime: RealtimeManager) : ViewModel() {
    private val _currentUser = MutableLiveData<UserModel>()
    val currentUser: LiveData<UserModel> = _currentUser

    // Función para actualizar el usuario actual
    fun setCurrentUser(user: UserModel) {
        _currentUser.value = user
    }

    /* Géstion de los Dialog */

    /* Botón cerrar app */
    private val _dialogCloseApp = MutableLiveData<Boolean>()
    val dialogCloseApp: LiveData<Boolean> = _dialogCloseApp

    fun dialogCloseApp_show() {
        _dialogCloseApp.value = true
    }

    fun dialogCloseApp_close() {
        _dialogCloseApp.value = false
    }

    /**** ScreenBoulders *****/
    /* Dialogo para añadir boulder */
    private val _dialogAddBoulder = MutableLiveData<Boolean>()
    val dialogAddBoulder: LiveData<Boolean> = _dialogAddBoulder
    fun dialogAddBoulder_close() {
        _dialogAddBoulder.value = false
    }
    fun dialogAddBoulder_show() {
        _dialogAddBoulder.value = true
    }

    /**********************************************/



    //En la card del item
    fun onCheckCardSelected(boulderModel: BoulderModel) {
        //que pasa cuando seleciionas la card con el boulder
    }

    /* Parámetros del Objeto boulder */
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

    /***NO LO ENTIENDO****/

    /* Parámetros del Objeto boulder actualizado*/
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

    private val _boulder = MutableLiveData<BoulderModel>()
    val boulder: LiveData<BoulderModel> = _boulder

    private val _boulderUpdate = MutableLiveData<BoulderModel>()
    val boulderUpdate: LiveData<BoulderModel> = _boulderUpdate

    private val _boulderSelected = MutableLiveData<BoulderModel>()
    val boulderSelected: LiveData<BoulderModel> = _boulderSelected


    private val _bouldersList = mutableStateListOf<BoulderModel>()
    val bouldersList: List<BoulderModel> = _bouldersList

    private val _bouldersListFlow = MutableStateFlow<List<BoulderModel>>(emptyList())
    val bouldersListFlow: StateFlow<List<BoulderModel>> = _bouldersListFlow
/*
    init {
        fetchBoulders()
    }
*/
    private fun fetchBoulders(realtime: RealtimeManager) {
        viewModelScope.launch {
            realtime.getBouldersFlow().collect {
                _bouldersListFlow.value = it
            }
        }
    }
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

    fun onBoulderChanged1(
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

    fun getBoulder (boulder: BoulderModel){
        Log.d("Laia", "Dentro del get  ${_wallInput.value } ${_gradeInput.value }")
        _noteInput1.value = boulder.note
        _wallInput1.value = boulder.wall_id
        _activeInput1.value = boulder.active
        _gradeInput1.value = boulder.grade
    }

    fun cleanBoulder() {
        Log.d("Laia", "Estas en el clean  ${_wallInput.value } ${_gradeInput.value }")
        _noteInput.value = ""
        _wallInput.value = "Muro 35"
        _activeInput.value = true
        _gradeInput.value = "4a"
        Log.d("Laia", "Estas en el clean al final ${_wallInput.value } ${_gradeInput.value }")
    }


    private fun createBoulder() {
        val color: String = getColorForGrade(_gradeInput.value!!)
        _gradeColor.value = color

        _boulder.value = BoulderModel()

        _boulder.value = BoulderModel(
            note = _noteInput.value.toString(),
            uid_routeSeter = _currentUser.value!!.user_id,
            wall_id = _wallInput.value.toString(),
            grade =  _gradeInput.value.toString(),
            active = _activeInput.value!!,
            color = color
        )
    }

    private fun updateBoulder(boulderOld: BoulderModel) {
        val color: String = getColorForGrade(_gradeInput1.value!!)
        _gradeColor1.value = color

        _boulderUpdate.value = BoulderModel(
            key = boulderOld.key,
            id = boulderOld.id,
            note = _noteInput1.value.toString(),
            uid_routeSeter = boulderOld.uid_routeSeter,
            wall_id = _wallInput1.value.toString(),
            grade =  _gradeInput1.value.toString(),
            active = _activeInput1.value!!,
            color = color,
            name_routeSeter = boulderOld.name_routeSeter)
    }




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


    suspend fun onBoulder_Add(realtime: RealtimeManager) {
        Log.d("laia", "Boulder actualizado con éxito a la base de datos en el homeviewmopdel ${_boulder.value} ")

        createBoulder()

        realtime.addBoulder(_boulder.value!!)

        _noteInput.value = ""
        _wallInput.value = "Muro 35"
        _activeInput.value = true
        _gradeInput.value = "4a"

        _dialogAddBoulder.value = false
    }

    suspend fun onBoulder_Delete(realtime: RealtimeManager, key: String?) {
        Log.d(
            "laia",
            "key ${key}"
        )
        realtime.deleteBoulder(key ?: "")
    }

    suspend fun onBoulder_Update(realtime: RealtimeManager, boulderOld: BoulderModel) {
        updateBoulder(boulderOld)
        realtime.updateBoulder(_boulderUpdate.value!!)



      /*  _noteInput.value = ""
        _wallInput.value = "Muro 35"
        _activeInput.value = true
        _gradeInput.value = "4a"*/
    }



    fun getColorForGrade(grade: String): String {
        return when (grade) {
            "4a", "4b", "4c" -> "difficulty_1"
            "5a", "5b", "5c" -> "difficulty_2"
            "6a", "6a+", "6b", "6b+", -> "difficulty_3"
            "6c", "6c+", "7a", "7a+",   -> "difficulty_4"
            "7b", "7b+", "7c", "7c+" -> "difficulty_5"
            "8a", "8a+", "8b", "8b+", "8c", "8c+" -> "difficulty_6"
            else -> "Color no definido"
        }
    }

    fun get (timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
/*
    private val _selectedWall = MutableStateFlow("")
    val selectedWall = _selectedWall.asStateFlow()

    fun setSelectedWall(wall: String) {
        _selectedWall.value = wall
    }

    fun cleanFilter(){
        _selectedWall.value = ""
    }*/



}


