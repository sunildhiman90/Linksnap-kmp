package app.linksnap.features.auth.data

import app.linksnap.models.User
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class SessionManager(
    private val settings: Settings,
    private val json: Json,
) {

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_USER = "auth_user"
    }

    private val _token = MutableStateFlow<String?>(settings.getStringOrNull(KEY_TOKEN))
    val token = _token.asStateFlow()


    private val _user = MutableStateFlow<User?>(settings.getStringOrNull(KEY_USER)?.let {
        try {
            json.decodeFromString<User>(it)
        } catch (e: Exception) {
            null
        }
    })
    val user = _user.asStateFlow()

    fun saveSession(newToken: String, newUser: User) {
        _token.value = newToken
        _user.value = newUser

        settings.putString(KEY_TOKEN, newToken)
        settings.putString(KEY_USER, json.encodeToString(User.serializer(), newUser))
    }

    fun clearSession() {
        _token.value = null
        _user.value = null

        settings.remove(KEY_TOKEN)
        settings.remove(KEY_USER)
    }

    fun isUserLoggedIn() = token.value != null

}