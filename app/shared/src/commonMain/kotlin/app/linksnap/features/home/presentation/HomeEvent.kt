package app.linksnap.features.home.presentation

sealed class HomeEvent {

    object Refresh: HomeEvent()
    data class Summarize(val url: String): HomeEvent()
    data class  ToggleFavorite(val linkId: String): HomeEvent()
    data class  UpdateSearchQuery(val query: String): HomeEvent()
    data class  MarkAsRead(val linkId: String): HomeEvent()

}