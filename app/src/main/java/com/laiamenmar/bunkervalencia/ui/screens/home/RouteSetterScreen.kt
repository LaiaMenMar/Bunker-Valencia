package com.laiamenmar.bunkervalencia.ui.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.laiamenmar.bunkervalencia.R
import com.laiamenmar.bunkervalencia.model.UserModel
import com.laiamenmar.bunkervalencia.ui.HomeViewModel
import com.laiamenmar.bunkervalencia.ui.screens.TopBarWelcome
import com.laiamenmar.bunkervalencia.ui.theme.primaryContainerLight
import com.laiamenmar.bunkervalencia.ui.theme.secondaryLight


import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RouteSetterScreen (
    realtime: RealtimeManager,
    homeViewModel: HomeViewModel, navigation: NavController
) {
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopBarWelcome(homeViewModel = homeViewModel, navigation= navigation )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TitleScreen("Equipadores", Modifier.align(Alignment.CenterHorizontally))

                UserList(realtime, scope)
            }
        }
    )
}

@Composable
fun UserList(realtime: RealtimeManager, scope: CoroutineScope) {
    val usersListFlow by realtime.getUsersFlow().collectAsState(emptyList())

    LazyColumn {
        items(usersListFlow) { user ->
            UserListItem(user = user, scope, realtime)
        }
    }
}

@Composable
fun UserListItem(user: UserModel, scope: CoroutineScope, realtime: RealtimeManager, ) {
    var isChecked by remember { mutableStateOf(user.router_setter) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        if (user != null && user?.urlPhoto.toString() != "null") {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user?.urlPhoto)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen",
                placeholder = painterResource(id = R.drawable.profile),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
            )

        } else {
            Image(
                painter = painterResource(R.drawable.profile),
                contentDescription = "Foto de perfil por defecto",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
            )
        }

        Text(text = user.display_name,
          //  color = md_theme_light_onPrimaryContainer,
            modifier = Modifier.weight(1f).padding(start= 8.dp))

        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked= it
                scope.launch {
                    realtime.updateUserRouterSetter(user.user_id, it)
                }},

            modifier = Modifier.padding(start = 8.dp),
           colors = CheckboxDefaults.colors(
                checkedColor = secondaryLight,
                uncheckedColor = primaryContainerLight
            ))
    }
}



