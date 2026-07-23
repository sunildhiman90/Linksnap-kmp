package app.linksnap.features.home.data

import app.linksnap.features.auth.data.SessionManager
import app.linksnap.models.LinkSummary
import app.linksnap.models.NetworkResponse
import app.linksnap.models.NetworkResult
import app.linksnap.models.SummarizeRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext

interface LinkRepository {

    val refreshEvents: SharedFlow<Unit>

    suspend fun summarizeLink(url: String): NetworkResult<LinkSummary>
    suspend fun getLinks(): NetworkResult<List<LinkSummary>>

    suspend fun getLinkDetail(id: String): NetworkResult<LinkSummary>
    suspend fun getFavorites(): NetworkResult<List<LinkSummary>>
    suspend fun toggleFavorite(linkId: String): NetworkResult<Boolean>
    suspend fun markAsRead(linkId: String): NetworkResult<Boolean>
    suspend fun getUserStats(): NetworkResult<Int>

}

class LinkRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) : LinkRepository {

    private val _refreshEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val refreshEvents = _refreshEvents.asSharedFlow()

    private fun HttpRequestBuilder.authHeader() {
        sessionManager.token.value?.let {
            header(HttpHeaders.Authorization, "Bearer $it")
        }
    }

    override suspend fun summarizeLink(url: String): NetworkResult<LinkSummary> {
        return try {
            withContext(Dispatchers.Default) {
                val apiRes = client.post("api/summarize") {
                    authHeader()
                    contentType(ContentType.Application.Json)
                    setBody(SummarizeRequest(url))
                }.body<NetworkResponse<LinkSummary>>()
                if (apiRes.data != null) {
                    _refreshEvents.tryEmit(Unit)
                    NetworkResult.Success(apiRes.data!!)
                } else {
                    NetworkResult.Error(apiRes.message ?: "some error in summarizeLink")
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "some error in summarizeLink")
        }
    }

    override suspend fun getLinks(): NetworkResult<List<LinkSummary>> {
        return try {
            withContext(Dispatchers.Default) {
                val apiRes = client.get("api/links") {
                    authHeader()
                }.body<NetworkResponse<List<LinkSummary>>>()

                if (apiRes.data != null) {
                    NetworkResult.Success(apiRes.data!!)
                } else {
                    NetworkResult.Error(apiRes.message ?: "some error in getLinks")
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "some error in getLinks")
        }
    }

    override suspend fun getFavorites(): NetworkResult<List<LinkSummary>> {
        return try {
            withContext(Dispatchers.Default) {

                val apiRes = client.get("api/links/favorite") {
                    authHeader()
                }.body<NetworkResponse<List<LinkSummary>>>()

                if (apiRes.data != null) {
                    NetworkResult.Success(apiRes.data!!)
                } else {
                    NetworkResult.Error(apiRes.message ?: "some error in getFavorites")
                }

            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "some error in getFavorites")
        }
    }


    override suspend fun getLinkDetail(id: String): NetworkResult<LinkSummary> {
        return try {
            withContext(Dispatchers.Default) {
                val apiRes = client.get("api/links/$id") {
                    authHeader()
                }.body<NetworkResponse<LinkSummary>>()

                if (apiRes.data != null) {
                    NetworkResult.Success(apiRes.data!!)
                } else {
                    NetworkResult.Error(apiRes.message ?: "some error in getLink detail")
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "some error in getLink detail")
        }
    }

    override suspend fun toggleFavorite(linkId: String): NetworkResult<Boolean> {
        return try {
            withContext(Dispatchers.Default) {

            val apiRes = client.post("api/links/$linkId/favorite") {
                authHeader()
            }.body<NetworkResponse<Boolean>>()
            if (apiRes.data != null) {
                _refreshEvents.tryEmit(Unit)
                NetworkResult.Success(apiRes.data!!)
            } else {
                NetworkResult.Error(apiRes.message ?: "some error in toggleFavorite")
            }
                }

        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "some error in toggleFavorite")
        }
    }

    override suspend fun markAsRead(linkId: String): NetworkResult<Boolean> {
        return try {
            withContext(Dispatchers.Default) {

                val apiRes = client.post("api/links/$linkId/read") {
                    authHeader()
                }.body<NetworkResponse<Boolean>>()
                if (apiRes.data != null) {
                    _refreshEvents.tryEmit(Unit)
                    NetworkResult.Success(apiRes.data!!)
                } else {
                    NetworkResult.Error(apiRes.message ?: "some error in markAsRead")
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "some error in markAsRead")
        }
    }

    override suspend fun getUserStats(): NetworkResult<Int> {
        return try {
            withContext(Dispatchers.Default) {

                val apiRes = client.get("api/links/stats") {
                    authHeader()
                }.body<NetworkResponse<Int>>()
                if (apiRes.data != null) {
                    NetworkResult.Success(apiRes.data!!)
                } else {
                    NetworkResult.Error(apiRes.message ?: "some error in getUserStats")
                }
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "some error in getUserStats")
        }
    }

}