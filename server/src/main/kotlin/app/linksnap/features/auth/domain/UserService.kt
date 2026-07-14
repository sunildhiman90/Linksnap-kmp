package app.linksnap.features.auth.domain

import app.linksnap.features.auth.data.UserDbRepository
import app.linksnap.models.User

interface UserService {

    suspend fun authenticate(
        googleId: String,
        email: String,
        name: String?,
        profilePicUrl: String?,
    ) : User

}

class UserServiceImpl(
    private val userRepo: UserDbRepository
): UserService {
    override suspend fun authenticate(
        googleId: String,
        email: String,
        name: String?,
        profilePicUrl: String?
    ): User {

        return userRepo.getOrCreateUser(
            googleId = googleId,
            email = email,
            name = name,
            profilePicUrl = profilePicUrl
        )

    }


}