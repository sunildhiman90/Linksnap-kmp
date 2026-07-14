package app.linksnap.features.link.domain

import app.linksnap.features.link.data.LinkDbRepository
import app.linksnap.features.link.data.LinkSummarizer
import app.linksnap.models.LinkSummary

interface LinkService {

    suspend fun processAndAddLink(
        userId: String,
        url: String
    ): LinkSummary

    suspend fun getUserLinks(
        userId: String
    ): List<LinkSummary>


    suspend fun getUserFavorites(
        userId: String
    ): List<LinkSummary>

    suspend fun toggleFavorite(
        userId: String,
        linkId: String
    ): Boolean


    suspend fun markAsRead(
        userId: String,
        linkId: String
    ): Boolean


    suspend fun getLinksCount(
        userId: String
    ): Int

}

class LinkServiceImpl(
    private val repo: LinkDbRepository
) : LinkService {

    override suspend fun processAndAddLink(
        userId: String,
        url: String
    ): LinkSummary {

        val summaryData = LinkSummarizer.summarize(url)

        return repo.addLink(
            userId = userId,
            url = url,
            summaryTitle = summaryData.title,
            bodySnippetText = summaryData.bodySnippet,
            summaryImageUrl = summaryData.imageUrl,
            aiSummaryText = summaryData.aiSummary,
            categoryName = summaryData.category,
            tagsText = summaryData.tags,
        )
    }

    override suspend fun getUserLinks(userId: String): List<LinkSummary> {
        return repo.getLinks(userId)
    }

    override suspend fun getUserFavorites(userId: String): List<LinkSummary> {
        return repo.getFavorites(userId)
    }

    override suspend fun toggleFavorite(
        userId: String,
        linkId: String
    ): Boolean {
        return repo.toggleFavorite(userId, linkId)
    }

    override suspend fun markAsRead(userId: String, linkId: String): Boolean {
        return repo.markAsRead(userId, linkId)
    }

    override suspend fun getLinksCount(userId: String): Int {
        return repo.getLinksCount(userId)
    }


}