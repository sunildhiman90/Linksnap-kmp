package app.linksnap.features.favorites.presentation

import app.linksnap.models.LinkSummary

data class FavoritesState(
    val isLoading: Boolean = false,
    val favorites: List<LinkSummary> = emptyList(),
    val error: String? = null,
)