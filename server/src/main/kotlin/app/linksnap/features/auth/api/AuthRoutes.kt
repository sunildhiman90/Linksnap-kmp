package app.linksnap.features.auth.api

import app.linksnap.features.auth.domain.UserService
import app.linksnap.features.auth.jwt.JwtConfig
import app.linksnap.models.AuthRequest
import app.linksnap.models.AuthResponse
import app.linksnap.models.NetworkResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.netty.handler.codec.http.HttpServerCodec
import org.koin.mp.KoinPlatform.getKoin


fun io.ktor.server.routing.Route.authRoutes() {

    val userService = getKoin().get<UserService>()


    post("/api/auth/google") {

        try {

            val request = call.receive<AuthRequest>()

            //TODO, implement google verification for tokenId
            val mockGoogleId = "google_${request.idToken.take(10)}"

            val user = userService.authenticate(mockGoogleId, request.email, request.name, request.profilePicUrl)

            val token = JwtConfig.makeToken(userId = user.id)

            call.respond(NetworkResponse(
                data = AuthResponse(token, user)
            ))

        } catch (e: Exception) {

            call.respond(HttpStatusCode.Unauthorized, NetworkResponse<AuthResponse>(
                message = "Auth failed: ${e.message}"
            ))

        }
    }


}