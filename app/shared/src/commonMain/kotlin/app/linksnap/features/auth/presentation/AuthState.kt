package app.linksnap.features.auth.presentation

import app.linksnap.models.User

data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)
