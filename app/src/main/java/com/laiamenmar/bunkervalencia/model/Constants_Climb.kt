/**
 * Constants_Climb.kt: Contiene constantes relacionadas con la escalada, como grados de dificultad de rutas y nombres de muros.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define las constantes utilizadas en la aplicación relacionadas con la escalada, como los grados de dificultad de rutas y los nombres de los muros.
 * Fecha de creación: 2024-04-01
 */
package com.laiamenmar.bunkervalencia.model

/**
 * Objeto singleton (sólo puede tener una única instancia) que contiene constantes relacionadas con la escalada.
 */
object Constants_Climb {

    /**
     * Lista de grados de dificultad de rutas de escalada.
     */
    val routeGrades = listOf(
        "4a", "4b", "4c", "5a", "5b", "5c", "6a", "6a+", "6b", "6b+",
        "6c", "6c+", "7a", "7a+", "7b", "7b+", "7c", "7c+", "8a", "8a+",
        "8b", "8b+", "8c", "8c+"
    )

    /**
     * Lista de nombres de muros de escalada.
     */
    val wallNames = listOf(
        "Muro 35", "Muro 25", "Isla Central", "Muro de atrás", "Muro entrada"
    )
}