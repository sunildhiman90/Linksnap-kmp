package app.linksnap.models

import kotlinx.serialization.Serializable


@Serializable
sealed class NetworkResult<out T> {
    @Serializable
    data class Success<out T>(val data: T): NetworkResult<T>()

    @Serializable
    data class Error(
        val message: String, val code: Int? = null
    ): NetworkResult<Nothing>()

    @Serializable
    object Loading: NetworkResult<Nothing>()

}



