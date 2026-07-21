package app.linksnap.features.detail.presentation

import app.linksnap.models.LinkSummary

data class DetailState(
    val isLoading: Boolean = false,
    val link: LinkSummary? = null,
    val error: String? = null,
)
