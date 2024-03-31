package com.laiamenmar.bunkervalencia.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laiamenmar.bunkervalencia.model.Constants_Climb
import com.laiamenmar.bunkervalencia.ui.HomeViewModel


@Composable
fun TitleDialog(title: String, modifier: Modifier){
    Text(
        text = title.uppercase(),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
fun Activation_Switch(value: Boolean, onValueChanged: (Boolean) -> Unit) {
   // var switchState by remember { mutableStateOf(true) }
        Text(text = "Activo")
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = value,
            onCheckedChange = onValueChanged
        )
}

@Composable
fun Walls_DropDownMenu(value: String, onValueChanged: (String) -> Unit) {
    val walls = Constants_Climb.wallNames
    var expanded by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChanged,
            enabled = false,
            readOnly = true,
            modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(disabledTextColor  = Color.Black)
            )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            walls.forEach { wall ->
                DropdownMenuItem(
                    text = {
                        Text(text = wall)
                    },
                    onClick = {
                        expanded = false
                        onValueChanged(wall)
                    }
                )
            }
    }
}

@Composable
fun DifficultySlider(value: String, onValueChanged: (String) -> Unit) {
    var sliderPosition by remember { mutableStateOf(6f)}
    val grades = Constants_Climb.routeGrades

        Text(text = "Grado: ${grades[sliderPosition.toInt()]}")
        Spacer(modifier = Modifier.height(4.dp))
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onValueChanged(grades[it.toInt()])
            },
            valueRange = 0f..(grades.size - 1).toFloat(),
            steps = grades.size - 1,
            modifier = Modifier.fillMaxWidth()
        )
}


@Composable
fun NoteTextField(value: String, onValueChanged: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        placeholder = { Text(text = "Notas equipador") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = false,
        maxLines = 3,
        colors = TextFieldDefaults.colors())
}
fun getColorNameForGradePosition(position: Int): String {
    return when (position) {
        in 0..5 -> "Blanco" // 4a a 5c
        in 6..11 -> "Verde" // 6a a 6c+
        in 12..17 -> "Naranja" // 7a a 7c+
        in 18..21 -> "Morado" // 8a a 8b+
        in 22..23 -> "Negro" // 8c y 8c+
        else -> "Color no definido" // Manejo de casos no definidos
    }
}

fun getColorFromString(colorName: String): Color {
    return when (colorName.lowercase()) {
        "blanco" -> Color.White
        "verde" -> Color.Green
        "naranja" -> Color(0xFFF44336)
        "morado" -> Color(0xFF673AB7)
        "negro" -> Color.Black
        else -> Color.Gray // Color por defecto en caso de que el nombre no sea reconocido
    }
}




