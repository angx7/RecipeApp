package org.lasalle.recipeapp.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val id: Int,
    val name: String,
    val recipeCount: Int
)