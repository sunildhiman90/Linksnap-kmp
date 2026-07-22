package app.linksnap.features.favorites.presentation

sealed class FavoritesEvent {
    object Refresh: FavoritesEvent()
    data class RemoveFavorites(val linkId: String): FavoritesEvent()
    data class MarkAsRead(val linkId: String): FavoritesEvent()
}