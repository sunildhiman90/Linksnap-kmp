package app.linksnap.models

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val idToken: String,
    val name: String? = null,
    val email: String,
    val profilePicUrl: String? = null,
)