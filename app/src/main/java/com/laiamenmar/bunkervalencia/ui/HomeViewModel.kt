package com.laiamenmar.bunkervalencia.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laiamenmar.bunkervalencia.model.BoulderModel
import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    /* Géstion de los Dialog */

    /* Botón cerrar app */
    private val _DialogCloseApp = MutableLiveData<Boolean>()
    val DialogCloseApp: LiveData<Boolean> = _DialogCloseApp

    fun DialogCloseApp_show() {
        _DialogCloseApp.value = true
    }

    fun DialogCloseApp_close() {
        _DialogCloseApp.value = false
    }

    /* ScreenBoulders*/
    /* Añadir boulder */
    private val _DialogAddBoulder = MutableLiveData<Boolean>()
    val DialogAddBoulder: LiveData<Boolean> = _DialogAddBoulder

    fun DialogAddBoulder_close() {
        _DialogAddBoulder.value = false
    }

    fun DialogAddBoulder_show() {
        _DialogAddBoulder.value = true
    }

    fun DialogDeleteBoulder_close() {
        _DialogDeleteBoulder.value = false
    }

    fun DialogDeleteBoulder_show() {
        _DialogDeleteBoulder.value = true
    }

    fun onBoulder_Delete(realtime: RealtimeManager, boulder: BoulderModel) {
        realtime.deleteBoulder(boulder.key ?: "")
        _DialogDeleteBoulder.value = false
    }

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

    private val _activeInput = MutableLiveData<Boolean>()
    val activeInput: LiveData<Boolean> = _activeInput

    private val _boulder = MutableLiveData<BoulderModel>()
    val boulder: LiveData<BoulderModel> = _boulder

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
            ui: String
        ) {
            _noteInput.value = note
            _wallInput.value = wall
            _activeInput.value = active
            _gradeInput.value = grade

            _boulder.value = BoulderModel(
                note = note,
                uid_routeSeter = ui,
                wall_id = wall,
                grade = grade,
                active = active
            )

            //aqui funciones de habilitar por ejemplo
        }

        fun onBoulder_Add(realtime: RealtimeManager, boulder: BoulderModel) {
            realtime.addBoulder(boulder)
            _noteInput.value = ""
            _DialogAddBoulder.value = false
        }

        /*  fun addBoulderParams(note: String, uid: String, grade: String, realtime: RealtimeManager) {
          val newBoulder = BoulderModel(note = name, uid_routeSeter = uid)
          realtime.addBoulder(newBoulder)
          _DialogAddBoulder.value = false
      }*/

        /* Borrar boulder */
        private val _DialogDeleteBoulder = MutableLiveData<Boolean>()
        val DialogDeleteBoulder: LiveData<Boolean> = _DialogDeleteBoulder
    }


