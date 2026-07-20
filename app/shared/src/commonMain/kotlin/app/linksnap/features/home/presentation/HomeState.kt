package app.linksnap.features.home.presentation

import app.linksnap.models.LinkSummary

data class HomeState(
    val isLoading: Boolean = false,
    val links: List<LinkSummary> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val isSummarizing: Boolean = false
)