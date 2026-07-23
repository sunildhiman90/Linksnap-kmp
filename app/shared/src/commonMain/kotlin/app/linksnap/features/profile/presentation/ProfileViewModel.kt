package app.linksnap.features.profile.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linksnap.features.auth.data.AuthRepository
import app.linksnap.features.auth.data.SessionManager
import app.linksnap.features.home.data.LinkRepository
import app.linksnap.models.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: LinkRepository,
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
): ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {

        onEvent(ProfileEvent.LoadProfile)

        viewModelScope.launch(dispatcher) {
            repository.refreshEvents.collect {
                loadProfile()
            }
        }

    }

    fun onEvent(event: ProfileEvent) {
        when(event) {
            is ProfileEvent.LoadProfile -> {
                loadProfile()
            }

            is ProfileEvent.Logout -> {
                logout()
            }
        }
    }

    private fun loadProfile() {

        viewModelScope.launch(dispatcher) {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val user = sessionManager.user.value
            _state.update {
                it.copy(
                    user = user,
                )
            }


            val statsResult =  repository.getUserStats()

            when(statsResult) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            linksCount = statsResult.data,
                        )
                    }

                }
                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
                else -> {}
            }

            when(val recentLinksResult = repository.getLinks()) {
                is NetworkResult.Success -> {

                    val links = recentLinksResult.data.filter {
                        it.lastReadAt != null
                    }.sortedByDescending {
                        it.lastReadAt
                    }

                    val recent = if (!links.isEmpty()) {
                        links.take(3)
                    } else {
                       recentLinksResult.data.sortedByDescending {
                           it.createdAt
                       }.take(3)
                    }


                    _state.update {
                        it.copy(
                            recentLinks = recent
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }

                else -> {}
            }

            _state.update {
                it.copy(
                    isLoading = false
                )
            }

        }

    }

    private fun logout() {

        viewModelScope.launch(dispatcher) {
            authRepository.logout()
            _state.update {
                it.copy(
                    isLoggedOut = true
                )
            }
        }
    }

}