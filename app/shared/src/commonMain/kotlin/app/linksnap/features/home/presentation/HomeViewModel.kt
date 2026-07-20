package app.linksnap.features.home.presentation

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


class HomeViewModel(
    private val repository: LinkRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        onEvent(HomeEvent.Refresh)
        viewModelScope.launch(dispatcher) {
            repository.refreshEvents.collect {
                loadLinks(isSilent = true)
            }
        }
    }


    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.Refresh -> {
                loadLinks(isSilent = false)
            }

            is HomeEvent.Summarize -> {
                summarizeLink(event.url)
            }

            is HomeEvent.ToggleFavorite -> {
                toggleFavorite(event.linkId)
            }

            is HomeEvent.UpdateSearchQuery -> {
                _state.update {
                    it.copy(
                        searchQuery = event.query
                    )
                }
            }

            is HomeEvent.MarkAsRead -> {
                markAsRead(event.linkId)
            }

            else -> {}
        }
    }


    private fun loadLinks(isSilent: Boolean) {
        viewModelScope.launch(dispatcher) {
            if (!isSilent) {
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }
            }
            when (val result = repository.getLinks()) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            links = result.data,
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

    private fun summarizeLink(link: String) {
        viewModelScope.launch(dispatcher) {
            _state.update {
                it.copy(
                    isSummarizing = true
                )
            }
            when (val result = repository.summarizeLink(link)) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            isSummarizing = false,
                            error = null
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isSummarizing = false,
                            error = result.message
                        )
                    }
                }

                else -> {}
            }
        }
    }

    private fun toggleFavorite(linkId: String) {
        viewModelScope.launch(dispatcher) {

            //optimistic updates
            val links = _state.value.links.toMutableList()
            val targetLink = links.find { it.id == linkId }
            val wasFavorite = targetLink?.isFavorite == true

            _state.update { state ->
                state.copy(
                    links = state.links.map {
                        if (it.id == linkId) {
                            it.copy(isFavorite = !wasFavorite)
                        } else {
                            it
                        }
                    }
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
                            links = state.links.map {
                                if (it.id == linkId) {
                                    it.copy(isFavorite = wasFavorite)
                                } else {
                                    it
                                }
                            }
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