package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
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
    val boulder: BoulderModel? by homeViewModel.boulder.observeAsState()
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
        BoulderList(DialogDeleteBoulder, homeViewModel, realtime)
        AddBoulderDialog(
            dialogAddBoulder = DialogAddBoulder,
            onDismiss = { homeViewModel.DialogAddBoulder_close() },
            onAdd = { boulder?.let { homeViewModel.onBoulder_Add(realtime, it) } },
            authManager = authManager,
            homeViewModel = homeViewModel
        )
        FabDialog(Modifier.align(Alignment.BottomEnd), homeViewModel)
    }
}

@Composable
fun AddBoulderDialog(
    dialogAddBoulder: Boolean,
    onDismiss: () -> Unit,
    onAdd: () -> Unit,
    authManager: AuthManager,
    homeViewModel: HomeViewModel
) {
    val noteRouteSeter: String by homeViewModel.noteInput.observeAsState(initial = "")
    var uid = authManager.getCurrentUser()
    val wall: String by homeViewModel.wallInput.observeAsState(initial = "")
    val grade: String by homeViewModel.gradeInput.observeAsState(initial = "6a")
    val active: Boolean by homeViewModel.activeInput.observeAsState(initial = true)

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
                    Activation_Switch(
                        active
                    ) {
                        homeViewModel.onBoulderChanged(
                            noteRouteSeter,
                            wall,
                            it,
                            grade,
                            uid.toString()
                        )
                    }
                }
                Walls_DropDownMenu(
                    wall
                ) {
                    homeViewModel.onBoulderChanged(
                        noteRouteSeter,
                        it,
                        active,
                        grade,
                        uid.toString()
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                DifficultySlider(
                    grade
                ) {
                    homeViewModel.onBoulderChanged(
                        noteRouteSeter,
                        wall,
                        active,
                        it,
                        uid.toString()
                    )
                }

                NoteTextField(
                    noteRouteSeter
                ) { homeViewModel.onBoulderChanged(it, wall, active, grade, uid.toString()) }

                Spacer(modifier = Modifier.size(8.dp))

                Button(
                    onClick = { onAdd() },
                    Modifier.fillMaxWidth()
                ) {
                    Text(text = "Añadir", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun BoulderList(
    dialogDeleteBoulder: Boolean,
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
                        dialogDeleteBoulder = dialogDeleteBoulder,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemBoulder(
    dialogDeleteBoulder: Boolean,
    realtime: RealtimeManager,
    homeViewModel: HomeViewModel,
    boulder: BoulderModel
) {
    // esto deberia estar en el view model
    val onDeleteBoulderConfirmed: () -> Unit = {}

    /*    DeleteBoulderDialog(
            dialogDeleteBoulder = dialogDeleteBoulder,
            onDismiss = { homeViewModel.DialogDeleteBoulder_close() },
            onBoulderConfirmDelete = { homeViewModel.onBoulder_Delete(realtime, boulder) })*/

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        border = BorderStroke(2.dp, Color.Gray),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column() {
                    Text(
                        text = boulder.wall_id,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Equipador Pablo Perez",
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                }
                Column (){
                    Button(
                        onClick = { /* Manejar el evento del clic */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),

                        colors = ButtonDefaults.buttonColors(
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = boulder.grade,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Row(Modifier.padding(top = 8.dp, start = 32.dp)) {
                        SocialIcon(
                            modifier = Modifier.weight(1f),
                            unselecetedIcon = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "likes",
                                    tint = Color.Red
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "likes",
                                    tint = Color.Red
                                )
                            },
                            isSelected = true
                        ) {
                            // chat = !chat
                        }

                        SocialIcon(
                            modifier = Modifier.weight(1f),
                            unselecetedIcon = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "likes",
                                    tint = Color.Red
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "likes",
                                    tint = Color.Green
                                )
                            },
                            isSelected = true
                        ) {
                            // rt = !rt
                        }

                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Default.Edit,
                    contentDescription = "likes",
                    tint = Color.Gray
                )
                Icon(
                    modifier = Modifier.padding(start = 16.dp).size(24.dp),
                    imageVector = Icons.Default.Delete,
                    contentDescription = "likes",
                    tint = Color.Gray
                )
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
    dialogDeleteBoulder: Boolean,
    onDismiss: () -> Unit,
    onBoulderConfirmDelete: () -> Unit
) {
    if (dialogDeleteBoulder) {
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



