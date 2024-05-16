/**
 * BoulderModel.kt: Este archivo contiene la implementación de la clase BoulderModel.
 *
 * Autor: Laia Méndez Martínez
 * Función: Define la clase BoulderModel para representar la información de los búlders de la aplicación.
 * Fecha de creación: 2024-02-26
 */
package com.laiamenmar.bunkervalencia.model

/**
 * Modelo de datos para representar un boulder.
 *
 * @param key La clave del boulder asignada por Firebase al crear el nodo (opcional, se inicializa como null).
 * @param id El ID del boulder (por defecto se genera utilizando System.currentTimeMillis()).
 * @param note La nota asociada al boulder por el equipador.
 * @param uid_routeSeter El ID del usuario equipador que creo la ruta del boulder.
 * @param wall_id El nombre de la pared donde esta ubicado el boulder.
 * @param grade El grado de dificultad del boulder.
 * @param active Indica si el boulder está disponible o no.
 * @param color El color del boulder que representa la dificultad .
 * @param likes El número de "me gustas" del boulder.
 * @param ascents El número de ascensos del boulder.
 * @param name_routeSeter El nombre del usuario equipador que estableció la ruta del boulder.
 * @constructor Crea un objeto BoulderModel con los parámetros especificados.
 */

data class BoulderModel(
    var key: String? = null,
    val id: Long = System.currentTimeMillis(),
    val note: String,
    val uid_routeSeter: String = "",
    val wall_id: String,
    val grade: String,
    val active: Boolean,
    val color: String = "",
    val likes: Int = 0,
    val ascents: Int = 0,
    val name_routeSeter: String = ""
    // var selected: () -> Unit = false //check de la card

) {
    /**
     * Constructor secundario que inicializa un objeto BoulderModel con valores predeterminados.
     *
     * @param key La clave del boulder asignada por Firebase al crear el nodo (opcional, se inicializa como null).
     * @param id El ID del boulder (por defecto se genera utilizando System.currentTimeMillis()).
     * @param note La nota asociada al boulder por el equipador.
     * @param uid_routeSeter El ID del usuario equipador que creo la ruta del boulder.
     * @param wall_id El nombre de la pared donde esta ubicado el boulder.
     * @param grade El grado de dificultad del boulder.
     * @param active Indica si el boulder está disponible o no.
     * @param color El color del boulder que representa la dificultad .
     * @param likes El número de "me gustas" del boulder.
     * @param ascents El número de ascensos del boulder.
     * @param name_routeSeter El nombre del usuario equipador que estableció la ruta del boulder.
     */
    constructor() : this("", 0, "", "", "", "", true)
}
