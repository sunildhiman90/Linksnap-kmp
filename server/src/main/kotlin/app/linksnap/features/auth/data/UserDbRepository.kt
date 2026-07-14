package app.linksnap.features.auth.data

import app.linksnap.db.DatabaseFactory.dbQuery
import app.linksnap.models.User
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID

interface UserDbRepository {
    suspend fun getOrCreateUser(
        googleId: String,
        email: String,
        name: String?,
        profilePicUrl: String?
    ): User
}

class UserDbRepositoryImpl : UserDbRepository {

    override suspend fun getOrCreateUser(
        googleId: String,
        email: String,
        name: String?,
        profilePicUrl: String?
    ): User = dbQuery {

        val existing = UsersTable.selectAll().where {
            UsersTable.googleId eq googleId
        }.singleOrNull()

        if (existing != null) {
            UsersTable.update({
                UsersTable.id eq existing[UsersTable.id]
            }) {
                it[UsersTable.name] = name ?: existing[UsersTable.name]
                it[UsersTable.profilePicUrl] = profilePicUrl ?: existing[UsersTable.profilePicUrl]
            }

            User(
                id = existing[UsersTable.id],
                googleId = existing[UsersTable.googleId],
                email = existing[UsersTable.email],
                name = existing[UsersTable.name],
                profilePicUrl = existing[UsersTable.profilePicUrl],
                isPro = existing[UsersTable.isPro]
            )
        } else {
            val newId = UUID.randomUUID().toString()
            UsersTable.insert {
                it[id] = newId
                it[UsersTable.name] = name
                it[UsersTable.email] = email
                it[UsersTable.profilePicUrl] = profilePicUrl
                it[UsersTable.isPro] = isPro
                it[UsersTable.googleId] = googleId
            }

            User(
                newId, name, email, profilePicUrl, googleId
            )
        }
    }
}
