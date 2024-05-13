package com.laiamenmar.bunkervalencia.ui.screens.home


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
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

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
@Composable
fun DifficultySlider(value: String, onValueChanged: (String) -> Unit, onSliderValueChanged: (Float) -> Unit) {
    var sliderPosition by remember { mutableStateOf(0f) }
    val grades = Constants_Climb.routeGrades

    val initialGradeIndex = Constants_Climb.routeGrades.indexOf(value)
    sliderPosition = initialGradeIndex.toFloat()

    Text(text = "Grado: $value")
    Spacer(modifier = Modifier.height(4.dp))
    Slider(
        value = sliderPosition,
        onValueChange = {
            sliderPosition = it
            //  val newGrade = grades[it.toInt()]
            onValueChanged(grades[it.toInt()])
            onSliderValueChanged(it)
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
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}


@Composable
fun ColorsChips(
    selectedColors: Set<String>,
    availableColors: List<String>,
    onColorSelected: (String) -> Unit,
    onColorDeselected: (String) -> Unit,
    modifier: Modifier
) {
    LazyRow(
        contentPadding = PaddingValues(vertical = 4.dp),

    ) {
        items(availableColors) { color ->
            val isSelected = selectedColors.contains(color)
            Chip(
                backgroundColor = if (isSelected) getColorlikeColor(color) else Color.Gray,
                gradeText = getMaxGradeForColor(color),
                onClick = {
                    if (isSelected) {
                        onColorDeselected(color)
                    } else {
                        onColorSelected(color)
                    }
                },
                modifier = modifier.padding(horizontal = 2.dp, vertical = 2.dp)
            )
        }
    }
}


@Composable
fun Chip(
    backgroundColor: Color,
    gradeText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        border = BorderStroke(
            width = 1.dp,
            color = Color.Gray
        ),
        color = backgroundColor,
        modifier = modifier
    ) {
        Text(
            text = gradeText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = if (gradeText== "8c+"){Color.White}else {Color.Black}
        )
    }
}

@Composable
fun TitleScreen(title: String, modifier: Modifier) {
    Text(
        text = title.uppercase(),
        style = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.25.sp
        ),
        modifier = modifier.padding(16.dp)
    )
}










