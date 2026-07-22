package app.linksnap.features.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linksnap.features.home.data.LinkRepository
import app.linksnap.models.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(
    private val repository: LinkRepository
): ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    fun onEvent(event: DetailEvent) {
        when(event) {
            is DetailEvent.LoadDetail -> {
               loadDetail(event.id)
            }

            is DetailEvent.ToggleFavorite -> {
                toggleFav()
            }

            is DetailEvent.MarkAsRead -> {
                markAsRead()
            }
        }
    }

    private fun loadDetail(
        id: String
    ) {
        viewModelScope.launch {

            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            when(val result = repository.getLinkDetail(id)) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            link = result.data,
                            error = null
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }

                else -> {}
            }
        }
    }

    private fun toggleFav() {
        viewModelScope.launch {
            val currentLink = _state.value.link ?: return@launch
            val result = repository.toggleFavorite(currentLink.id)
            if (result is NetworkResult.Success) {
                _state.update {
                    it.copy(
                        link = it.link?.copy(
                            isFavorite = !it.link.isFavorite
                        )
                    )
                }
            }
        }
    }

    private fun markAsRead() {
        viewModelScope.launch {
            val currentLink = _state.value.link ?: return@launch
            repository.markAsRead(currentLink.id)
        }
    }

}