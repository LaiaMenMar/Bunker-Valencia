package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.laiamenmar.bunkervalencia.R
import com.laiamenmar.bunkervalencia.model.BoulderModel
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager


@Composable()
fun BouldersScreen(
    realtime: RealtimeManager,
    authManager: AuthManager,
    homeViewModel: HomeViewModel,
) {
    // estado de dialogo para añadir bloque
    val DialogAddBoulder: Boolean by homeViewModel.DialogAddBoulder.observeAsState(false)
    val DialogDeleteBoulder: Boolean by homeViewModel.DialogDeleteBoulder.observeAsState(false)

    /* Obtienes la lista de contactos a traves de un flujo, deberia estar en el HomeViewModel*/
    // val bouldersListFlow by realtime.getBouldersFlow().collectAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .padding(12.dp)
    ) {
        AddBoulderDialog(
            dialogAddBoulder = DialogAddBoulder,
            onDismiss = { homeViewModel.DialogDeleteBoulder_close() },
            // onBoulderAdded = { homeViewModel.onBoulder_Add(realtime, ?¿?¿) },
            authManager = authManager,
            realtime = realtime,
            homeViewModel = homeViewModel
        )
        FabDialog(Modifier.align(Alignment.BottomEnd), homeViewModel)
        BoulderList(DialogDeleteBoulder, homeViewModel, realtime)
    }
}

@Composable
fun AddBoulderDialog(
    dialogAddBoulder: Boolean,
    onDismiss: () -> Unit,/* onBoulderAdded: (RealtimeManager, BoulderModel) -> Unit,*/
    authManager: AuthManager,
    realtime: RealtimeManager,
    homeViewModel: HomeViewModel
) {
    val noteRouteSeter: String by homeViewModel.noteInput.observeAsState(initial= "")
    var uidRouteSeter = authManager.getCurrentUser()
    val wall: String by homeViewModel.wallInput.observeAsState(initial= "")
    val grade: String by homeViewModel.gradeInput.observeAsState(initial= "")
    val active: Boolean by homeViewModel.activeInput.observeAsState(initial= true)

    if (dialogAddBoulder) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    TitleDialog("Bloque", Modifier.weight(1f))
                    Activation_Switch(active, {homeViewModel.onBoulderChanged(noteRouteSeter, wall, it, grade)})
                }
                Walls_DropDownMenu(wall, {homeViewModel.onBoulderChanged(noteRouteSeter, it, active, grade)})

                Spacer(modifier = Modifier.size(8.dp))

                DifficultySlider(grade, {homeViewModel.onBoulderChanged(noteRouteSeter, wall, active, it)})

                NoteTextField(noteRouteSeter, {homeViewModel.onBoulderChanged(it, wall, active, grade)})

                Spacer(modifier = Modifier.size(8.dp))

                Button(onClick = {
                  //  val newBoulder =
                       // BoulderModel(note = myBoulderparametro, uid_routeSeter = uid.toString())
              //          BoulderModel(note = noteRouteSeter_Boulder, uid_routeSeter = uidRouteSeter_Boulder.toString(), wall_id="", grade= grade_Boulder, active=true)
                 //   homeViewModel.onBoulder_Add(realtime, newBoulder)
                //    noteRouteSeter_Boulder = ""


                }, Modifier.fillMaxWidth()) {
                    Text(text = "Añadir", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun BoulderList(
    DialogDeleteBoulder: Boolean,
    homeViewModel: HomeViewModel,
    realtime: RealtimeManager
) {
    val bouldersListFlow by realtime.getBouldersFlow().collectAsState(emptyList())
    //  val myBouldersList: List<BoulderModel> = homeViewModel.boulder

    //(!bouldersListFlow.isNullOrEmpty())
    if (true) {
        LazyColumn {
            /*items(myBouldersList, key = { it.id }) { boulder ->
                ItemBoulder(delete = delete, boulderModel = boulder, homeViewModel = homeViewModel)
            }*/
            bouldersListFlow.forEach { boulder ->
                item {
                    ItemBoulder(
                        DialogDeleteBoulder = DialogDeleteBoulder,
                        realtime = realtime,
                        homeViewModel = homeViewModel,
                        boulder = boulder
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No se encontraron \n bloques",
                fontSize = 18.sp, fontWeight = FontWeight.Thin, textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun ItemBoulder(
    DialogDeleteBoulder: Boolean,
    realtime: RealtimeManager,
    homeViewModel: HomeViewModel,
    boulder: BoulderModel
) {
    // esto deberia estar en el view model
    val onDeleteBoulderConfirmed: () -> Unit = {}

    DeleteBoulderDialog(
        DialogDeleteBoulder = DialogDeleteBoulder,
        onDismiss = { homeViewModel.DialogDeleteBoulder_close() },
        onBoulderConfirmDelete = { homeViewModel.onBoulder_Delete(realtime, boulder) })

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        border = BorderStroke(2.dp, Color.Gray),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                painter = painterResource(id = R.drawable.bloque),
                contentDescription = "",
                modifier = Modifier.weight(1f)
            )
            Column(modifier = Modifier.weight(3f)) {
                Text(
                    text = boulder.note,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    /*  modifier = Modifier
                          .padding(horizontal = 4.dp)*/
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = boulder.uid_routeSeter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                /* Checkbox(
                     checked = boulderModel.selected,
                     onCheckedChange = { homeViewModel.onCheckCardSelected(boulderModel)
                     })*/

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton(
                        onClick = {
                            homeViewModel.DialogDeleteBoulder_show()

                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Borrar bloque"
                        )
                    }
                }
            }
        }
    }
}

// Elementos para añadir boulder
@Composable
fun FabDialog(modifier: Modifier, homeViewModel: HomeViewModel) {
    FloatingActionButton(onClick = {
        homeViewModel.DialogAddBoulder_show()
    }, modifier = modifier) {
        Icon(Icons.Filled.Add, "Añadir Bloque")
    }

}


@Composable
fun DeleteBoulderDialog(
    DialogDeleteBoulder: Boolean,
    onDismiss: () -> Unit,
    onBoulderConfirmDelete: () -> Unit
) {
    if (DialogDeleteBoulder) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text("Eliminar bloque") },
            text = { Text("¿Estás seguro que deseas eliminar el bloque?") },
            confirmButton = {
                Button(
                    onClick = onBoulderConfirmDelete
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
