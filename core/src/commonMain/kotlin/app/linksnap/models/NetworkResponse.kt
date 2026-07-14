package app.linksnap.models

import kotlinx.serialization.Serializable


@Serializable
data class NetworkResponse<T>(
    val data: T? = null,
    val message: String? = null,
    val code: Int? = null,
)
