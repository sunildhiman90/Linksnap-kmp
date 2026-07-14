package app.linksnap.models

import kotlinx.serialization.Serializable

@Serializable
data class SummarizeRequest(
    val url: String,
)