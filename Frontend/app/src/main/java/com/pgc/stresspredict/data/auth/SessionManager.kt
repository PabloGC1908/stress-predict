package com.pgc.stresspredict.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Guarda los datos de autenticación de forma segura
     * @param token Token JWT o similar
     * @param email Email del usuario
     * @throws IllegalArgumentException si el token está vacío
     */
    fun saveAuthData(token: String, email: String) {
        require(token.isNotBlank()) { "El token no puede estar vacío" }
        require(email.isNotBlank()) { "El email no puede estar vacío" }

        prefs.edit(commit = true) {  // Usamos commit() para escritura sincrónica
            putString(KEY_AUTH_TOKEN, token.trim())
            putString(KEY_USER_EMAIL, email.trim().lowercase())
        }
    }

    /**
     * Obtiene el token de autenticación
     * @return Token o null si no existe
     */
    fun getAuthToken(): String? = prefs.getString(KEY_AUTH_TOKEN, null)?.takeIf { it.isNotBlank() }

    /**
     * Obtiene el email del usuario
     * @return Email o null si no existe
     */
    fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)?.takeIf { it.isNotBlank() }

    /**
     * Verifica si hay una sesión activa
     * @return true si hay un token válido
     */
    fun isLoggedIn(): Boolean = getAuthToken() != null

    /**
     * Cierra la sesión limpiando todos los datos
     */
    fun logout() {
        prefs.edit(commit = true) {
            clear()  // Limpia todas las preferencias
        }
    }

    /**
     * Obtiene el token validando que exista
     * @throws IllegalStateException si no hay token
     * @return Token de autenticación
     */
    @Throws(IllegalStateException::class)
    fun getValidToken(): String {
        return getAuthToken() ?: throw IllegalStateException("Usuario no autenticado")
    }

    /**
     * Genera el header de autorización
     * @throws IllegalStateException si no hay token
     * @return String en formato "Bearer [token]"
     */
    fun getAuthHeader(): String = "Bearer ${getValidToken()}"

    /**
     * Verifica y obtiene el email validando que exista
     * @throws IllegalStateException si no hay email guardado
     * @return Email del usuario
     */
    @Throws(IllegalStateException::class)
    fun getValidEmail(): String {
        return getUserEmail() ?: throw IllegalStateException("Email no disponible")
    }

    companion object {
        private const val PREFS_NAME = "com.pgc.stresspredict.SESSION_PREFS"
        private const val KEY_AUTH_TOKEN = "auth_token_v2"
        private const val KEY_USER_EMAIL = "user_email_v2"
    }
}