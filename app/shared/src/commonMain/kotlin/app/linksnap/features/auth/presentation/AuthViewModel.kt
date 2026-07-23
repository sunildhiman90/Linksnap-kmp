package app.linksnap.features.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.linksnap.features.auth.data.AuthRepository
import app.linksnap.features.auth.data.SessionManager
import app.linksnap.models.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val sessionManager: SessionManager,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    init {
        onEvent(AuthEvent.CheckSession)
    }


    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.LoginWithGoogle -> {
                loginWithGoogle(
                    idToken = event.idToken,
                    email = event.email,
                    name = event.name,
                    profilePicUrl = event.profilePicUrl
                )
            }

            is AuthEvent.CheckSession -> {
                checkSession()
            }

            is AuthEvent.Logout -> {
                logout()
            }
        }

    }


    fun loginWithGoogle(
        idToken: String,
        email: String,
        name: String?,
        profilePicUrl: String?
    ) {
        viewModelScope.launch(dispatcher) {

            _state.update {
                it.copy(
                    isLoading = true
                )
            }

            val result = repository.loginWithGoogle(
                idToken = idToken,
                email = email,
                name = name,
                profilePicUrl = profilePicUrl
            )

            when (result) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoggedIn = true,
                            user = result.data.user,
                            error = null
                        )
                    }
                }

                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false, error = result.message,
                            isLoggedIn = false,
                        )
                    }
                }

                else -> {}
            }

        }


    }


    private fun checkSession() {
        viewModelScope.launch(dispatcher) {
            val isLoggedIn = sessionManager.isUserLoggedIn()
            val user = sessionManager.user.value
            _state.update {
                it.copy(
                    isLoggedIn = isLoggedIn,
                    user = user
                )
            }
        }
    }


    private fun logout() {
        viewModelScope.launch {
            repository.logout()
            _state.update {
                it.copy(
                    isLoggedIn = false, user = null
                )
            }
        }
    }


}