package org.lasalle.recipeapp.data.services

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import org.lasalle.recipeapp.models.AuthResponse
import org.lasalle.recipeapp.models.LoginBody
import org.lasalle.recipeapp.models.RegisterBody
import org.lasalle.recipeapp.models.User
import org.lasalle.recipeapp.models.UserResponse

interface AuthService {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterBody): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginBody): AuthResponse

    @GET("user/{id}")
    suspend fun getUserId(@Path ("id")id: Int): UserResponse
}