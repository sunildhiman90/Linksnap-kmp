package app.linksnap.features.auth.data

import org.jetbrains.exposed.v1.core.Table

object UsersTable: Table("users") {
    val id = varchar("id", 255)
    val name = varchar("name", 255).nullable()
    val email = varchar("email", 255).uniqueIndex()
    val profilePicUrl = varchar("profile_pic_url", 500).nullable()
    val googleId = varchar("google_id", 255).nullable()
    val isPro = bool("is_pro").default(false)

    override val primaryKey = PrimaryKey(id)
}

