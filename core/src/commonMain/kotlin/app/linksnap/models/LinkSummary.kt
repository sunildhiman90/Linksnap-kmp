package app.linksnap.models

import kotlinx.serialization.Serializable

@Serializable
data class LinkSummary(
    val id: String,
    val userId: String,
    val originalUrl: String,
    val title: String,
    val imageUrl: String?,
    val aiSummary: String,
    val category: String,
    val bodySnippet: String? = null,
    val isFavorite: Boolean = false,
    val createdAt: Long = 0L,
    val tags: String = "",
    val lastReadAt: Long? = null
)