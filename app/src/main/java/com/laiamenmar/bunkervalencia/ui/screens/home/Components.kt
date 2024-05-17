/**
 * Components.kt: Este archivo contiene la definición de varios componentes Compose reutilizables
 * en diferentes partes de la aplicación, como diálogos, controles de usuario y tarjetas.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define varios componentes Compose reutilizables para mantener un código limpio y modular.
 * Fecha de creación: 2024/04/01
 */

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

/**
 * Muestra un diálogo con un título en texto grande y negrita.
 *
 * @param title El título que se mostrará en el diálogo.
 * @param modifier El modificador que se aplicará al componente de texto del título.
 */
@Composable
fun TitleDialog(title: String, modifier: Modifier){
    Text(
        text = title.uppercase(),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

/**
 * Muestra un interruptor de activación junto con un texto descriptivo.
 *
 * @param value El valor actual del interruptor.
 * @param onValueChanged La lambda que se llamará cuando cambie el valor del interruptor.
 */
@Composable
fun Activation_Switch(value: Boolean, onValueChanged: (Boolean) -> Unit) {
        Text(text = "Activo")
        Spacer(modifier = Modifier.width(8.dp))
        Switch(
            checked = value,
            onCheckedChange = onValueChanged
        )
}

/**
 * Muestra un menú desplegable de selección de paredes.
 *
 * @param value El valor actualmente seleccionado en el menú desplegable.
 * @param onValueChanged La lambda que se llamará cuando se seleccione una nueva pared
 * en el menú desplegable.
 */
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

/**
 * Muestra un control deslizante para seleccionar el grado de dificultad de una ruta.
 *
 * @param value El valor actualmente seleccionado en el control deslizante.
 * @param onValueChanged La lambda que se llamará cuando se seleccione un nuevo grado de dificultad.
 * @param onSliderValueChanged La lambda que se llamará cuando se cambie el valor del
 * control deslizante.
 */
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

/**
 * Muestra un campo de texto para ingresar notas del equipador.
 *
 * @param value El valor actualmente ingresado en el campo de texto.
 * @param onValueChanged La lambda que se llamará cuando se ingresen nuevas notas.
 */
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

/**
 * Muestra un icono social seleccionable que puede estar marcado como seleccionado o no seleccionado.
 *
 * @param modifier La modificación aplicada al icono social.
 * @param unselecetedIcon La lambda que muestra el icono cuando no está seleccionado.
 * @param selectedIcon La lambda que muestra el icono cuando está seleccionado.
 * @param isSelected Indica si el icono social está seleccionado.
 * @param onItemSelected La lambda que se llama cuando se selecciona el icono.
 */
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

/**
 * Muestra una fila horizontal de chips de colores seleccionables.
 *
 * @param selectedColors Los colores seleccionados actualmente.
 * @param availableColors Lista de colores disponibles para seleccionar.
 * @param onColorSelected La lambda que se llama cuando se selecciona un color.
 * @param onColorDeselected La lambda que se llama cuando se deselecciona un color.
 * @param modifier La modificación aplicada a la fila de chips de colores.
 */
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

/**
 * Muestra un chip con un color de fondo y un texto asociado.
 *
 * @param backgroundColor El color de fondo del chip.
 * @param gradeText El texto asociado al chip.
 * @param onClick La lambda que se llama cuando se hace clic en el chip.
 * @param modifier La modificación aplicada al chip.
 */
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

/**
 * Muestra un título de pantalla con el texto proporcionado..
 *
 * @param title El título de la pantalla.
 * @param modifier La modificación aplicada al título de la pantalla.
 */
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










