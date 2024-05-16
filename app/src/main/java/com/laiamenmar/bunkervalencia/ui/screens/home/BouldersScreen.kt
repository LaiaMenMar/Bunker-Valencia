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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.laiamenmar.bunkervalencia.model.BoulderModel
import com.laiamenmar.bunkervalencia.model.Constants_Climb
import com.laiamenmar.bunkervalencia.model.UserModel
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.navigation.AppScreens
import com.laiamenmar.bunkervalencia.ui.theme.difficulty_1
import com.laiamenmar.bunkervalencia.ui.theme.difficulty_2
import com.laiamenmar.bunkervalencia.ui.theme.difficulty_3
import com.laiamenmar.bunkervalencia.ui.theme.difficulty_4
import com.laiamenmar.bunkervalencia.ui.theme.difficulty_5
import com.laiamenmar.bunkervalencia.ui.theme.difficulty_6
import com.laiamenmar.bunkervalencia.ui.theme.primaryLight
import com.laiamenmar.bunkervalencia.ui.theme.tertiaryLight

import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable()
fun BouldersScreen(
    realtime: RealtimeManager,
    authManager: AuthManager,
    homeViewModel: HomeViewModel,
    navigation: NavController
) {
    val scope = rememberCoroutineScope()
    val currentUser: UserModel? by homeViewModel.currentUser.observeAsState()
    val dialogAddBoulder: Boolean by homeViewModel.dialogAddBoulder.observeAsState(false)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
            .padding(12.dp)
    ) {

        BoulderList(homeViewModel, realtime, scope, authManager, navigation)

        AddBoulderDialog(
            dialogAddBoulder = dialogAddBoulder,
            onDismiss = { homeViewModel.dialogAddBoulder_close() },
            onAdd = {
                scope.launch {
                    homeViewModel.onBoulder_Add(realtime)
                }
            },
            homeViewModel = homeViewModel
        )

        /*    SubmitBoulderDialog(
                dialogAddBoulder = dialogAddBoulder,
                onDismiss = { homeViewModel.dialogAddBoulder_close() },
                onSubmit = {
                    scope.launch {
                        homeViewModel.onBoulder_Add(realtime)
                    }
                },
                homeViewModel = homeViewModel,
                update = false,
                boulderOld = BoulderModel()
            )*/

        if (currentUser != null && currentUser?.router_setter == true) {
            FabDialog(Modifier.align(Alignment.BottomEnd), homeViewModel)
        }
    }
}

@Composable
fun AddBoulderDialog(
    dialogAddBoulder: Boolean,
    onDismiss: () -> Unit,
    onAdd: () -> Unit,
    homeViewModel: HomeViewModel
) {

    val wall: String by homeViewModel.wallInput.observeAsState(initial = "Muro 35")
    val grade: String by homeViewModel.gradeInput.observeAsState(initial = "4a")
    val active: Boolean by homeViewModel.activeInput.observeAsState(initial = true)
    val noteRouteSeter: String by homeViewModel.noteInput.observeAsState(initial = "")

    var sliderPosition by remember { mutableStateOf(0f) }
    var dialogBackgroundColor by remember { mutableStateOf(difficulty_1) }


    if (dialogAddBoulder) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(dialogBackgroundColor)
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
                        grade
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                DifficultySlider(
                    value = grade,
                    onValueChanged = {
                        homeViewModel.onBoulderChanged(
                            noteRouteSeter,
                            wall,
                            active,
                            it,
                        )
                    },

                    onSliderValueChanged = { sliderValue ->
                        sliderPosition = sliderValue
                        dialogBackgroundColor =
                            getColorForPosition(Constants_Climb.routeGrades[sliderValue.toInt()])
                    }
                )

                NoteTextField(
                    noteRouteSeter
                ) {
                    homeViewModel.onBoulderChanged(
                        it,
                        wall,
                        active,
                        grade,
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                Button(
                    onClick = {
                        onAdd()
                        dialogBackgroundColor = difficulty_1
                    },
                    Modifier.fillMaxWidth()
                ) {
                    Text(text = "Añadir", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun UpdateBoulderDialog(
    dialogUpdateBoulder: Boolean,
    onDismiss: () -> Unit,
    onUpdate: () -> Unit,
    homeViewModel: HomeViewModel,
    boulderOld: BoulderModel
) {

    homeViewModel.getBoulder(boulderOld)

    val wall: String by homeViewModel.wallInput1.observeAsState(initial = "")
    val grade: String by homeViewModel.gradeInput1.observeAsState(initial = "")
    val active: Boolean by homeViewModel.activeInput1.observeAsState(initial = true)
    val noteRouteSeter: String by homeViewModel.noteInput1.observeAsState(initial = "")

    var sliderPosition by remember { mutableStateOf(0f) }
    var dialogBackgroundColor by remember { mutableStateOf(difficulty_1) }

    dialogBackgroundColor = getColorlikeColor(boulderOld.color)


    if (dialogUpdateBoulder) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(dialogBackgroundColor)
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
                        homeViewModel.onBoulderChangedUpdate(
                            noteRouteSeter,
                            wall,
                            it,
                            grade,
                        )
                    }
                }
                Walls_DropDownMenu(
                    wall
                ) {

                    homeViewModel.onBoulderChangedUpdate(
                        noteRouteSeter,
                        it,
                        active,
                        grade,
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                DifficultySlider(
                    grade,
                    onValueChanged = {
                        homeViewModel.onBoulderChangedUpdate(
                            noteRouteSeter,
                            wall,
                            active,
                            it,
                        )
                    },

                    onSliderValueChanged = { sliderValue ->
                        sliderPosition = sliderValue
                        dialogBackgroundColor =
                            getColorForPosition(Constants_Climb.routeGrades[sliderValue.toInt()])
                    }
                )

                NoteTextField(
                    noteRouteSeter
                ) { homeViewModel.onBoulderChangedUpdate(it, wall, active, grade) }

                Spacer(modifier = Modifier.size(8.dp))

                Button(
                    onClick = { onUpdate() },
                    Modifier.fillMaxWidth()
                ) {
                    Text(text = "Actualizar", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun BoulderList(
    homeViewModel: HomeViewModel,
    realtime: RealtimeManager,
    scope: CoroutineScope,
    authManager: AuthManager,
    navigation: NavController
) {
    val bouldersListFlow by realtime.getBouldersFlow().collectAsState(emptyList())

    var selectedWall by remember { mutableStateOf("") }
    val allAvailableColors = listOf(
        "difficulty_1",
        "difficulty_2",
        "difficulty_3",
        "difficulty_4",
        "difficulty_5",
        "difficulty_6"
    )

    var selectedColors by remember { mutableStateOf(allAvailableColors.toSet()) }

    Column {
        Walls_DropDownMenu(
            value = selectedWall,
            onValueChanged = { wall ->
                selectedWall = wall
            }
        )
        Row {
            IconButton(
                onClick = {
                    selectedWall = ""
                    selectedColors = allAvailableColors.toSet()
                },
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear Filter",
                    tint = Color.Gray,

                    )
            }

            ColorsChips(
                selectedColors = selectedColors,
                availableColors = allAvailableColors,
                onColorSelected = { color ->
                    selectedColors = selectedColors + color
                },
                onColorDeselected = { color ->
                    selectedColors = selectedColors - color
                },
                modifier = Modifier.weight(1f)
            )
        }

        val filteredBoulders = bouldersListFlow.filter { boulder ->
            (selectedWall.isBlank() || boulder.wall_id == selectedWall) && (boulder.color in selectedColors)
        }

        if (!filteredBoulders.isNullOrEmpty()) {
            LazyColumn {
                filteredBoulders.forEach { boulder ->
                    item {
                        ItemBoulder(
                            realtime = realtime,
                            homeViewModel = homeViewModel,
                            boulder = boulder,
                            scope = scope,
                            authManager = authManager,
                            navigation
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
}

@Composable
fun ItemBoulder(
    realtime: RealtimeManager,
    homeViewModel: HomeViewModel,
    boulder: BoulderModel,
    scope: CoroutineScope,
    authManager: AuthManager,
    navigation: NavController

) {
    val currentUser: UserModel? by homeViewModel.currentUser.observeAsState()
    var dialogUpdateBoulder by remember { mutableStateOf(false) }
    var dialogDeleteBoulder by remember { mutableStateOf(false) }

    val date = Date(boulder.id)
    val sdf = SimpleDateFormat("dd/MM/yyyy")
    val formattedDate = sdf.format(date)

    val today = Calendar.getInstance().time

    DeleteBoulderDialog(
        dialogDeleteBoulder = dialogDeleteBoulder,
        onDismiss = { dialogDeleteBoulder = false },
        onBoulderConfirmDelete = {
            scope.launch { homeViewModel.onBoulder_Delete(realtime, boulder.key) }
            dialogDeleteBoulder = false
        })

    /*   SubmitBoulderDialog (
           dialogAddBoulder = dialogUpdateBoulder,
           onDismiss = { dialogUpdateBoulder = false},
           onSubmit = {
               scope.launch {   homeViewModel.onBoulder_Update(realtime, boulder) }
               dialogUpdateBoulder = false },
           homeViewModel = homeViewModel,
           update = true,
           boulderOld = boulder
       )*/

    UpdateBoulderDialog(
        dialogUpdateBoulder = dialogUpdateBoulder,
        onDismiss = { dialogUpdateBoulder = false },
        onUpdate = {
            scope.launch {
                homeViewModel.onBoulder_Update(realtime, boulder)
            }
            dialogUpdateBoulder = false
        },
        homeViewModel = homeViewModel,
        boulderOld = boulder
    )

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                homeViewModel.setSelectedBoulder(boulder)
                navigation.navigate(AppScreens.BoulderDetailScreen.route)
            },
        border = BorderStroke(2.dp, Color.Gray),

        ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = boulder.wall_id,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formattedDate,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                    if (truncateTimeFromDate(today) == truncateTimeFromDate(date)) {
                        Text(
                            text = "Nuevo".uppercase(),
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = primaryLight
                        )
                    }
                }


                Column(modifier = Modifier.weight(1f)) {
                    val buttonColors = if (boulder.color == "difficulty_6") {
                        ButtonDefaults.buttonColors(
                            contentColor = Color.White,
                            containerColor = getColorlikeColor(boulder.color)
                        )
                    } else {
                        ButtonDefaults.buttonColors(
                            contentColor = Color.Black,
                            containerColor = getColorlikeColor(boulder.color)
                        )
                    }
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .width(300.dp)
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        colors = buttonColors
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
                                    modifier = Modifier.size(28.dp),
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = "likes",
                                    tint = Color.Gray
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    modifier = Modifier.size(28.dp),
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
                                    modifier = Modifier.size(28.dp),
                                    imageVector = Icons.Outlined.RocketLaunch,
                                    contentDescription = "done",
                                    tint = Color.Gray
                                )
                            },
                            selectedIcon = {
                                Icon(
                                    modifier = Modifier.size(28.dp),
                                    imageVector = Icons.Outlined.RocketLaunch,
                                    contentDescription = "done",
                                    tint = tertiaryLight
                                )
                            },
                            isSelected = true
                        ) {
                            // rt = !rt
                        }

                    }
                }
            }
            if (currentUser != null && currentUser?.router_setter == true) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { dialogUpdateBoulder = true },
                        imageVector = Icons.Default.Edit,
                        contentDescription = "edit",
                        tint = Color.Gray
                    )
                    Icon(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(24.dp)
                            .clickable { dialogDeleteBoulder = true },
                        imageVector = Icons.Default.Delete,
                        contentDescription = "delete",
                        tint = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun FabDialog(modifier: Modifier, homeViewModel: HomeViewModel) {
    FloatingActionButton(onClick = {
        homeViewModel.dialogAddBoulder_show()
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
            onDismissRequest = { onDismiss },
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

fun getColorlikeColor(colorString: String): Color {
    return when (colorString) {
        "difficulty_1" -> difficulty_1
        "difficulty_2" -> difficulty_2
        "difficulty_3" -> difficulty_3
        "difficulty_4" -> difficulty_4
        "difficulty_5" -> difficulty_5
        "difficulty_6" -> difficulty_6
        else -> Color.Gray
    }
}

fun getColorForPosition(grade: String): Color {
    return when (grade) {
        "4a", "4b", "4c" -> difficulty_1
        "5a", "5b", "5c", "6a", "6a+" -> difficulty_2
        "6b", "6b+", "6c", "6c+" -> difficulty_3
        "7a", "7a+" -> difficulty_4
        "7b", "7b+", "7c", "7c+" -> difficulty_5
        "8a", "8a+", "8b", "8b+", "8c", "8c+" -> difficulty_6
        else -> Color.DarkGray
    }
}

fun getMaxGradeForColor(color: String): String {
    return when (color) {
        "difficulty_1" -> "4a"
        "difficulty_2" -> "5a"
        "difficulty_3" -> "6b"
        "difficulty_4" -> "7a"
        "difficulty_5" -> "7b"
        "difficulty_6" -> "8a"
        else -> "N/A"
    }
}

fun truncateTimeFromDate(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}


/*
@Composable
fun SubmitBoulderDialog(
    dialogAddBoulder: Boolean,
    onDismiss: () -> Unit,
    onSubmit: () -> Unit,
    homeViewModel: HomeViewModel,
    update: Boolean,
    boulderOld: BoulderModel
) {
    var sliderPosition by remember { mutableStateOf(0f) }
    var dialogBackgroundColor by remember { mutableStateOf(difficulty_1) }

    homeViewModel.cleanBoulder()
    Log.d("Laia", "ES nulo + ${boulderOld}")
    Log.d("Laia", "ES update + ${update}")

    if(update) {
        Log.d("Laia", "Dentro Update")

        Log.d("Laia", "Dentro del updateSlider1 + ${sliderPosition}")
        homeViewModel.getBoulder(boulderOld)

        val initialGradeIndex = Constants_Climb.routeGrades.indexOf(boulderOld.grade)

        sliderPosition = initialGradeIndex.toFloat()
        Log.d("Laia", "Slider2 + ${sliderPosition}")

        dialogBackgroundColor = getColorlikeColor(boulderOld.color)

    }

    val wall: String by homeViewModel.wallInput.observeAsState(initial = "Muro 35")
    val grade: String by homeViewModel.gradeInput.observeAsState(initial = "4a")
    val active: Boolean by homeViewModel.activeInput.observeAsState(initial = true)
    val noteRouteSeter: String by homeViewModel.noteInput.observeAsState(initial = "")

    if (dialogAddBoulder) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(dialogBackgroundColor)
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
                        grade
                    )
                }

                Spacer(modifier = Modifier.size(8.dp))

                DifficultySlider(
                    value = grade,
                    onValueChanged = {
                        homeViewModel.onBoulderChanged(
                            noteRouteSeter,
                            wall,
                            active,
                            it,
                        )
                    },

                    onSliderValueChanged = { sliderValue ->
                        sliderPosition = sliderValue
                        dialogBackgroundColor =
                            getColorForPosition(Constants_Climb.routeGrades[sliderValue.toInt()])
                    }
                )

                NoteTextField(
                    noteRouteSeter
                ) { homeViewModel.onBoulderChanged(
                    it,
                    wall,
                    active,
                    grade,
                ) }

                Spacer(modifier = Modifier.size(8.dp))

                Button(
                    onClick = {
                        onSubmit()
                        dialogBackgroundColor = difficulty_1
                    },
                    Modifier.fillMaxWidth()
                ) {
                    if (update){
                    Text(text = "Actualizar", fontSize = 16.sp)
                } else { Text(text = "Añadir", fontSize = 16.sp)}
                }
            }
        }
    }
}*/
