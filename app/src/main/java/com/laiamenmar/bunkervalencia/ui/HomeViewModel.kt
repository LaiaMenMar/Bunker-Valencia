package com.laiamenmar.bunkervalencia.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.laiamenmar.bunkervalencia.model.RouteModel

class HomeViewModel : ViewModel() {
    /* Botón cerrar app */
    private val _showDialogCloseApp = MutableLiveData<Boolean>()
    val showDialogCloseApp: LiveData<Boolean> = _showDialogCloseApp

    fun showDialogCloseApp() {
        _showDialogCloseApp.value = true
    }

    fun closeDialogCloseApp() {
        _showDialogCloseApp.value = false
    }



    /* Utilizado en el botón flotante */
    private val _showDialogAddRoute = MutableLiveData<Boolean>()
    val showDialogAddRoute: LiveData<Boolean> = _showDialogAddRoute

    fun showDialogAddRouteClose() {
        _showDialogAddRoute.value = false
    }

    fun onRouteAdd(routename: String) {
        _showDialogAddRoute.value = false
       // Log.i("Laia", route)
        _routes.add(RouteModel(routename))
    }

    fun onShowDialogClick() {
        _showDialogAddRoute.value = true
    }

    /* Utilizado en la lista de rutas */
    fun onCheckBoxSelected(routaModel: RouteModel) {

    }

    private val _routes = mutableStateListOf<RouteModel>()
    val route: List<RouteModel> = _routes
}

