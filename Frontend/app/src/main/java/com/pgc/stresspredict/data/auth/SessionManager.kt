package com.pgc.stresspredict.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    companion object {
        private const val PREFS_NAME = "stress_predict_session"
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USER_ID = "user_id"
    }

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Guardar datos de sesión después de login/registro
    fun saveAuthData(token: String, email: String, userId: Long) {
        prefs.edit {
            putString(KEY_AUTH_TOKEN, token)
            putString(KEY_USER_EMAIL, email)
            putLong(KEY_USER_ID, userId)
        }
    }

    // Obtener token JWT
    fun getAuthToken(): String? = prefs.getString(KEY_AUTH_TOKEN, null)

    // Obtener email del usuario
    fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)

    // Obtener ID del usuario
    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, -1L)

    // Verificar si hay sesión activa
    fun isLoggedIn(): Boolean = getAuthToken() != null

    // Cerrar sesión (limpiar datos)
    fun logout() {
        prefs.edit {
            remove(KEY_AUTH_TOKEN)
            remove(KEY_USER_EMAIL)
            remove(KEY_USER_ID)
            apply()
        }
    }

    // Para interceptores de Retrofit (opcional)
    fun getAuthHeader(): String? {
        return getAuthToken()?.let { "Bearer $it" }
    }
}