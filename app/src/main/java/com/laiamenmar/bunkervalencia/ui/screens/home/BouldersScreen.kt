package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.components.Lazy
import com.laiamenmar.bunkervalencia.model.BoulderModel
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.utils.AuthManager
import com.laiamenmar.bunkervalencia.utils.RealtimeManager

@Composable()
fun BouldersScreen(
    realtime: RealtimeManager,
    authManager: AuthManager,
    homeViewModel: HomeViewModel
) {
    val showDialogAdd_Boulder: Boolean by homeViewModel.showDialogAdd_Boulder.observeAsState(false)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.DarkGray)
            .padding(12.dp)
    ) {
        Text(text = "Bloques")
        AddBoulderDialog(showDialogAdd_Boulder, onDismiss = {homeViewModel.showDialogAdd_Boulder_Close()}, onBoulderAdded = { homeViewModel.onBoulder_Add(it)})
        FabDialog(Modifier.align(Alignment.BottomEnd), homeViewModel)
        BoulderList(homeViewModel)
    }
}

@Composable
fun BoulderList(homeViewModel: HomeViewModel) {
    val myBoulders:List<BoulderModel> = homeViewModel.boulder

    LazyColumn {
        items(myBoulders, key = {it.uid}){ boulder ->
            ItemBoulder(boulderModel = boulder , homeViewModel = homeViewModel)
        }
    }

}

@Composable
fun ItemBoulder(boulderModel: BoulderModel, homeViewModel: HomeViewModel) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        border = BorderStroke(2.dp, Color.Gray),
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Número de route: ${boulderModel.uid}",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
            )
            Checkbox(
                checked = boulderModel.selected,
                onCheckedChange = { homeViewModel.onCheckCardSelected(boulderModel)
                })
        }

    }

}

// Elementos para añadir boulder
@Composable
fun FabDialog(modifier: Modifier, homeViewModel: HomeViewModel) {
    FloatingActionButton(onClick = {
        homeViewModel.onShowDialog_Boulder_Click()
    }, modifier = modifier) {
        Icon(Icons.Filled.Add, "Añadir Boulder")
    }

}

@Composable
fun AddBoulderDialog(show: Boolean, onDismiss: () -> Unit, onBoulderAdded: (String) -> Unit) {
    var myBoulder by remember { mutableStateOf("") }

    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Bloque",
                    fontSize = 18.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(value = myBoulder, onValueChange = { myBoulder = it })
                Spacer(modifier = Modifier.size(16.dp))
                Button(onClick = {
                    //añadir ruta a Firebase
                    onBoulderAdded(myBoulder)
                    myBoulder = ""
                }, Modifier.fillMaxWidth()) {
                    Text(text = "Añadir", fontSize = 16.sp)
                }
            }
        }
    }
}
