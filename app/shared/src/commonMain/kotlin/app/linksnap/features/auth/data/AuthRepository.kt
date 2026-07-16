package app.linksnap.features.auth.data

import app.linksnap.models.AuthRequest
import app.linksnap.models.AuthResponse
import app.linksnap.models.NetworkResponse
import app.linksnap.models.NetworkResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AuthRepository {

    suspend fun loginWithGoogle(
        idToken: String,
        email: String,
        name: String?,
        profilePicUrl: String?,
    ): NetworkResult<AuthResponse>

    fun logout()
}

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager
) : AuthRepository {
    override suspend fun loginWithGoogle(
        idToken: String,
        email: String,
        name: String?,
        profilePicUrl: String?
    ): NetworkResult<AuthResponse> {

        return withContext(Dispatchers.Default) {
            try {
                val apiResponse: NetworkResponse<AuthResponse> =
                    httpClient.post("api/auth/google") {
                        contentType(ContentType.Application.Json)
                        setBody(
                            AuthRequest(
                                idToken = idToken,
                                email = email,
                                name = name,
                                profilePicUrl = profilePicUrl
                            )
                        )
                    }.body()

                val data = apiResponse.data
                val result = if (data != null) {
                    NetworkResult.Success(data)
                } else {
                    NetworkResult.Error(apiResponse.message ?: "Auth failed")
                }

                if (result is NetworkResult.Success) {
                    sessionManager.saveSession(result.data.token, result.data.user)
                }
                result
            } catch (e: Exception) {

                NetworkResult.Error(e.message ?: "Something went wrong")
            }
        }
    }

    override fun logout() {
        sessionManager.clearSession()
    }

}