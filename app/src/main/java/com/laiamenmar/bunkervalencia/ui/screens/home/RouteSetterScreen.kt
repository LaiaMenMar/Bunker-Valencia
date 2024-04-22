package com.laiamenmar.bunkervalencia.ui.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.laiamenmar.bunkervalencia.model.UserModel
import com.laiamenmar.bunkervalencia.ui.theme.md_theme_light_onPrimaryContainer
import com.laiamenmar.bunkervalencia.ui.theme.md_theme_light_onSecondaryContainer
import com.laiamenmar.bunkervalencia.ui.theme.md_theme_light_secondaryContainer
import com.laiamenmar.bunkervalencia.utils.RealtimeManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RouteSetterScreen (realtime: RealtimeManager) {
    val scope = rememberCoroutineScope()
   Column(modifier = Modifier
       .fillMaxSize()
       .background(color = Color.LightGray)
       .padding(12.dp)) {

       Text(text = "Elige Equipador")

        Spacer(modifier = Modifier.height(4.dp))

        UserList(realtime, scope)
   }
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
        Text(text = user.display_name,
            color = md_theme_light_onPrimaryContainer,
            modifier = Modifier.weight(1f))

        Checkbox(
            checked = isChecked,
            onCheckedChange = { isChecked= it
                scope.launch {
                    realtime.updateUserRouterSetter(user.user_id, it)
                }},

            modifier = Modifier.padding(start = 8.dp),
          /*  colors = CheckboxDefaults.colors(
                checkedColor = md_theme_light_secondaryContainer,
                uncheckedColor = md_theme_light_onSecondaryContainer
            )*/)
    }
}
