package app.linksnap.features.detail.presentation

sealed class DetailEvent {

    data class LoadDetail(val id: String): DetailEvent()
    object ToggleFavorite: DetailEvent()
    object MarkAsRead: DetailEvent()

}