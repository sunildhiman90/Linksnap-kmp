package app.linksnap.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String?,
    val email: String,
    val profilePicUrl: String?,
    val googleId: String? = null,
    val isPro: Boolean = false,
)