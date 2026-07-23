package app.linksnap.features.profile.presentation

import app.linksnap.models.LinkSummary
import app.linksnap.models.User

data class ProfileState(
    val user: User? = null,
    val linksCount: Int = 0,
    val recentLinks: List<LinkSummary> = emptyList(),
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
)