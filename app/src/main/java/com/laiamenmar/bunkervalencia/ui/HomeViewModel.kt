package com.laiamenmar.bunkervalencia.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laiamenmar.bunkervalencia.model.BoulderModel
import com.laiamenmar.bunkervalencia.utils.RealtimeManager

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
    private val _boulders = mutableStateListOf<BoulderModel>()
    val boulder: List<BoulderModel> = _boulders


    /* Añadir boulder */
    private val _DialogAddBoulder = MutableLiveData<Boolean>()
    val DialogAddBoulder: LiveData<Boolean> = _DialogAddBoulder

    fun DialogAddBoulder_close() {
        _DialogAddBoulder.value = false
    }
    fun DialogAddBoulder_show() {
        _DialogAddBoulder.value = true
    }
    fun onBoulder_Add(realtime: RealtimeManager, boulder: BoulderModel) {
        realtime.addBoulder(boulder)
        _DialogAddBoulder.value = false
    }

    fun addBoulderParams(name: String, uid: String, realtime: RealtimeManager) {
        val newBoulder = BoulderModel(name = name, uid_routeSeter = uid)
        realtime.addBoulder(newBoulder)
        _DialogAddBoulder.value = false
    }

    /* Borrar boulder */
    private val _DialogDeleteBoulder = MutableLiveData<Boolean>()
    val DialogDeleteBoulder: LiveData<Boolean> = _DialogDeleteBoulder

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








}

