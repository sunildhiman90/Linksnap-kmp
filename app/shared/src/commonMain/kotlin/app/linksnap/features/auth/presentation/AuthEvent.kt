package app.linksnap.features.auth.presentation

sealed class AuthEvent {

    data class LoginWithGoogle(
        val idToken: String,
        val email: String,
        val name: String?,
        val profilePicUrl: String?,
    ): AuthEvent()

    object Logout: AuthEvent()
    object CheckSession: AuthEvent()

}