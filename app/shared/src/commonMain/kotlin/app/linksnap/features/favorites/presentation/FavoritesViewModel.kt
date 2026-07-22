package app.linksnap.features.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linksnap.features.home.data.LinkRepository
import app.linksnap.models.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: LinkRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state = _state.asStateFlow()


    init {

        onEvent(FavoritesEvent.Refresh)
        viewModelScope.launch(dispatcher) {
            repository.refreshEvents.collect {
                loadFavorites(isSilent = true)
            }
        }
    }


    fun onEvent(event: FavoritesEvent) {
        when(event) {
            is FavoritesEvent.Refresh -> {
                loadFavorites(isSilent = true)
            }

            is FavoritesEvent.RemoveFavorites -> {
                removeFavorites(event.linkId)
            }

            is FavoritesEvent.MarkAsRead -> {
                markAsRead(event.linkId)
            }
        }
    }

    private fun loadFavorites(isSilent: Boolean) {
        viewModelScope.launch(dispatcher) {
            if (!isSilent) {
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }
            }
            when (val result = repository.getFavorites()) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            favorites = result.data,
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


    private fun removeFavorites(linkId: String) {
        viewModelScope.launch(dispatcher) {

            //optimistic updates
            val links = _state.value.favorites.toMutableList()

            _state.update { state ->
                state.copy(
                    favorites = state.favorites.filter { it.id != linkId }
                )
            }


            when (val result = repository.toggleFavorite(linkId)) {
                is NetworkResult.Success -> {
                    //they willl be updated from refresh events
                }

                is NetworkResult.Error -> {

                    //revert
                    _state.update { state ->
                        state.copy(
                            favorites = links
                        )
                    }
                }

                else -> {}
            }
        }
    }

    private fun markAsRead(linkId: String) {
        viewModelScope.launch(dispatcher) {
            repository.markAsRead(linkId)
        }
    }



}