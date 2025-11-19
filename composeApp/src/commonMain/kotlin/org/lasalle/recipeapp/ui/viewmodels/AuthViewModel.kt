package org.lasalle.recipeapp.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.lasalle.recipeapp.data.services.KtorfitFactory
import org.lasalle.recipeapp.data.services.Preferences
import org.lasalle.recipeapp.models.LoginBody
import org.lasalle.recipeapp.models.RegisterBody

class AuthViewModel(): ViewModel() {
    // TODO: Utilizar este mensaje para un snackbar o similar en la UI
    var message by mutableStateOf("")
    val authService = KtorfitFactory.getAuthService()
    var isLogged by mutableStateOf(Preferences.getIsLogged())

    fun register(name: String, email: String, password: String){
        viewModelScope.launch {
            try {
                val register = RegisterBody(
                    name = name,
                    email = email,
                    password = password
                )
                val result = authService.register(register)
                if(result.isLogged){
                    // TODO: Navegar a la pantalla principal
                    isLogged = true
                    Preferences.saveUserId(result.userId)
                    Preferences.saveIsLogged(true)
                }else{
                    message = result.message
                }
                println("Registro exitoso: ${result}")
            }catch (e: Exception){
                message = "No se pudo registrar el usuario."
                println("Error en el registro: ${e}")
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = LoginBody(
                    email = email,
                    password = password
                )
                val response = authService.login(request)
                if (response.isLogged) {
                    // TODO: NAVEGAR A HOME Y GUARDAR EL USERID EN MEMORIA
                    isLogged = true
                    Preferences.saveIsLogged(true)
                    Preferences.saveUserId(response.userId)
                }
                else{
                    message = response.message
                }
            }catch (e: Exception){
                message = "No se pudo iniciar sesión."
                println("Error en el login: ${e}")
            }
        }
    }
}