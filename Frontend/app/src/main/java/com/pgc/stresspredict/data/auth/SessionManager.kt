package com.pgc.stresspredict.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton  // 👈 Para que sea una única instancia en toda la app
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context  // 👈 Contexto inyectado por Hilt
) {
    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveAuthData(token: String, email: String) {
        prefs.edit {
            putString(KEY_AUTH_TOKEN, token.trim())  // 👈 Asegura limpiar espacios
            putString(KEY_USER_EMAIL, email)
            apply()  // Cambia a commit() si necesitas confirmación inmediata
        }
    }

    fun getAuthToken(): String? = prefs.getString(KEY_AUTH_TOKEN, null)

    fun getUserEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)

    fun isLoggedIn(): Boolean = getAuthToken() != null

    fun logout() {
        prefs.edit {
            remove(KEY_AUTH_TOKEN)
            remove(KEY_USER_EMAIL)
            apply()
        }
    }

    fun getValidToken(): String {
        return getAuthToken()?.takeIf { it.isNotBlank() }
            ?: throw IllegalStateException("Usuario no autenticado")
    }

    fun getAuthHeader(): String {
        return "Bearer ${getValidToken().trim()}"
    }

    // 👇 Constantes como propiedades privadas (mejor que companion object)
    private companion object {
        const val PREFS_NAME = "stress_predict_session"
        const val KEY_AUTH_TOKEN = "auth_token"
        const val KEY_USER_EMAIL = "user_email"
    }
}