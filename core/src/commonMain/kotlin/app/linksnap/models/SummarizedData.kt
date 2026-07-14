package app.linksnap.models

import kotlinx.serialization.Serializable

@Serializable
data class SummarizedData(
    val title: String,
    val imageUrl: String?,
    val aiSummary: String,
    val category: String,
    val tags: String,
    val bodySnippet: String?,
)