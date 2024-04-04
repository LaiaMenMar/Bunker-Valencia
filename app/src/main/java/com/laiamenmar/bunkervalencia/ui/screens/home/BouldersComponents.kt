package com.laiamenmar.bunkervalencia.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laiamenmar.bunkervalencia.model.Constants_Climb


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

@SuppressLint("SuspiciousIndentation")
@Composable
fun DifficultySlider(value: String, onValueChanged: (String) -> Unit) {
    var sliderPosition by remember { mutableStateOf(6f)}
    val grades = Constants_Climb.routeGrades

        Text(text = "Grado: $value")
        Spacer(modifier = Modifier.height(4.dp))
        Slider(
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
              //  val newGrade = grades[it.toInt()]
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

                    /*CARDS*/
@Composable
fun SocialIcon(
    modifier: Modifier,
    unselecetedIcon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit,
    isSelected: Boolean,
    onItemSelected: () -> Unit
) {
    val defaultValue = 1
    Row(
        modifier = modifier.clickable { onItemSelected() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelected) {
            selectedIcon()
        } else {
            unselecetedIcon()
        }

        Text(
            text = if (isSelected) (defaultValue + 1).toString() else defaultValue.toString(),
            color = Color(0xFF7E8B98),
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
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




