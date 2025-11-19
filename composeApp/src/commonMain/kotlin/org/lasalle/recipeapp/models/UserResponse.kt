package org.lasalle.recipeapp.models

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val message: String,
    val user: User
)