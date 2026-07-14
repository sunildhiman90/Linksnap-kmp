package app.linksnap.features.link.data

import app.linksnap.features.auth.data.UsersTable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime
import kotlin.time.Clock

object LinksTable: Table("links") {
    val id = varchar("id", 255)
    val userId = varchar("user_id", 255).references(UsersTable.id)
    val originalUrl = text("original_url")
    val aiSummary = text("ai_summary")
    val bodySnippet = text("body_snippet").nullable()
    val title = varchar("title", 500)
    val category = varchar("category", 255)
    val imageUrl = varchar("image_url", 500).nullable()
    val isFavorite = bool("is_favorite").default(false)
    val createdAt = datetime("created_at").clientDefault {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }
    val tags = text("tags")
    val lastReadAt = long("last_read_at").nullable()

    override val primaryKey = PrimaryKey(id)
}