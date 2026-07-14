package app.linksnap.features.link.data

import app.linksnap.db.DatabaseFactory.dbQuery
import app.linksnap.models.LinkSummary
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

interface LinkDbRepository {
    suspend fun addLink(
        userId: String,
        url: String,
        summaryTitle: String,
        summaryImageUrl: String?,
        aiSummaryText: String,
        categoryName: String,
        bodySnippetText: String?,
        tagsText: String
    ): LinkSummary

    suspend fun getLinks(userId: String): List<LinkSummary>
    suspend fun getFavorites(userId: String): List<LinkSummary>
    suspend fun toggleFavorite(userId: String, linkId: String): Boolean
    suspend fun markAsRead(userId: String, linkId: String): Boolean
    suspend fun getLinksCount(userId: String): Int

}

class LinkDbRepositoryImpl : LinkDbRepository {
    override suspend fun addLink(
        userId: String,
        url: String,
        summaryTitle: String,
        summaryImageUrl: String?,
        aiSummaryText: String,
        categoryName: String,
        bodySnippetText: String?,
        tagsText: String
    ): LinkSummary = dbQuery {
        val newId = UUID.randomUUID().toString()
        LinksTable.insert {
            it[id] = newId
            it[LinksTable.userId] = userId
            it[originalUrl] = url
            it[title] = title
            it[imageUrl] = summaryImageUrl
            it[LinksTable.aiSummary] = aiSummaryText
            it[category] = categoryName
            it[bodySnippet] = bodySnippetText
            it[LinksTable.tags] = tagsText
        }

        LinkSummary(
            id = newId,
            userId = userId,
            originalUrl = url,
            title = summaryTitle,
            imageUrl = summaryImageUrl,
            aiSummary = aiSummaryText,
            category = categoryName,
            bodySnippet = bodySnippetText,
            tags = tagsText,
            lastReadAt = null,
            createdAt = System.currentTimeMillis()
        )
    }

    override suspend fun getLinks(userId: String): List<LinkSummary> = dbQuery {
        LinksTable.selectAll().where {
            LinksTable.userId eq userId
        }
            .orderBy(LinksTable.createdAt to SortOrder.DESC)
            .map {
                it.toLinkSummary()
            }
    }

    override suspend fun getFavorites(userId: String): List<LinkSummary> = dbQuery {

        LinksTable.selectAll().where {
            (LinksTable.userId eq userId) and (LinksTable.isFavorite eq true)
        }
            .orderBy(LinksTable.createdAt to SortOrder.DESC)
            .map {
                it.toLinkSummary()
            }
    }

    override suspend fun toggleFavorite(
        userId: String,
        linkId: String
    ): Boolean = dbQuery {
        val link = LinksTable.selectAll().where {
            (LinksTable.userId eq userId) and (LinksTable.id eq linkId)
        }.singleOrNull()

        if (link != null) {
            LinksTable.update({ (LinksTable.userId eq userId) and (LinksTable.id eq linkId) }) {
                it[isFavorite] = !link[isFavorite]
            }
            true
        } else {
            false
        }
    }

    override suspend fun markAsRead(userId: String, linkId: String): Boolean = dbQuery {
        val link = LinksTable.selectAll().where {
            (LinksTable.userId eq userId) and (LinksTable.id eq linkId)
        }.singleOrNull()

        if (link != null) {
            LinksTable.update({ (LinksTable.userId eq userId) and (LinksTable.id eq linkId) }) {
                it[lastReadAt] = System.currentTimeMillis()
            }
            true
        } else {
            false
        }
    }

    override suspend fun getLinksCount(userId: String): Int {

        return dbQuery {
            LinksTable.selectAll().where {
                LinksTable.userId eq userId
            }.count().toInt()
        }

    }
}


fun ResultRow.toLinkSummary(): LinkSummary {
    return LinkSummary(
        id = this[LinksTable.id],
        userId = this[LinksTable.userId],
        originalUrl = this[LinksTable.originalUrl],
        title = this[LinksTable.title],
        imageUrl = this[LinksTable.imageUrl],
        aiSummary = this[LinksTable.aiSummary],
        category = this[LinksTable.category],
        bodySnippet = this[LinksTable.bodySnippet],
        tags = this[LinksTable.tags],
        lastReadAt = this[LinksTable.lastReadAt],
        createdAt = this[LinksTable.createdAt].toInstant(TimeZone.currentSystemDefault())
            .toEpochMilliseconds()
    )
}
