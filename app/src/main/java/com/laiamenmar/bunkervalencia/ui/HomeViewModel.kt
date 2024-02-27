package com.laiamenmar.bunkervalencia.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laiamenmar.bunkervalencia.model.BoulderModel

class HomeViewModel: ViewModel() {
    /* Botón cerrar app */
    private val _showDialogCloseApp = MutableLiveData<Boolean>()
    val showDialogCloseApp: LiveData<Boolean> = _showDialogCloseApp

    fun showDialogCloseApp() {
        _showDialogCloseApp.value = true
    }

    fun closeDialogCloseApp() {
        _showDialogCloseApp.value = false
    }

    ///////////////////////////////////////////////////////////////////////////

    /* Utilizado en el botón flotante en la página de boulders */
    private val _showDialogAdd_Boulder = MutableLiveData<Boolean>()
    val showDialogAdd_Boulder: LiveData<Boolean> = _showDialogAdd_Boulder


    fun showDialogAdd_Boulder_Close() {
        _showDialogAdd_Boulder.value = false
    }

    fun onBoulder_Add(boulder: String) {
        _showDialogAdd_Boulder.value = false
        _boulders.add(BoulderModel(boulder = boulder))
    }


    fun onShowDialog_Boulder_Click() {
        _showDialogAdd_Boulder.value = true
    }


    //En la card del item

    fun onCheckCardSelected(boulderModel: BoulderModel) {

        //que pasa cuando seleciionas la card con el boulder

    }

   private val _boulders = mutableStateListOf<BoulderModel>()
   val boulder: List<BoulderModel> = _boulders

}

