package app.linksnap.features.auth.api

import app.linksnap.features.auth.domain.UserService
import app.linksnap.features.auth.jwt.JwtConfig
import app.linksnap.features.link.domain.LinkService
import app.linksnap.models.AuthRequest
import app.linksnap.models.AuthResponse
import app.linksnap.models.NetworkResponse
import app.linksnap.models.SummarizeRequest
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.netty.handler.codec.http.HttpServerCodec
import org.koin.mp.KoinPlatform.getKoin


fun Route.linkRoutes() {

    val linkService = getKoin().get<LinkService>()

    authenticate("auth-jwt") {

        post("/api/summarize") {

            val principle = call.principal<JwtConfig.JWTPrincipal>()!!
            val request = call.receive<SummarizeRequest>()

            try {

                val linkSummary = linkService.processAndAddLink(principle.userId, request.url)

                call.respond(
                    NetworkResponse(
                        linkSummary
                    )
                )

            } catch (e: Exception) {

                call.respond(
                    HttpStatusCode.InternalServerError,
                    NetworkResponse(
                        e.message
                    )
                )

            }
        }

        get("/api/links") {

            val principle = call.principal<JwtConfig.JWTPrincipal>()!!

            try {
                call.respond(
                    NetworkResponse(
                        linkService.getUserLinks(principle.userId)
                    )
                )

            } catch (e: Exception) {

                call.respond(
                    HttpStatusCode.InternalServerError,
                    NetworkResponse(
                        e.message
                    )
                )

            }


        }

        get("/api/links/favorite") {

            val principle = call.principal<JwtConfig.JWTPrincipal>()!!

            try {
                call.respond(
                    NetworkResponse(
                        linkService.getUserFavorites(principle.userId)
                    )
                )

            } catch (e: Exception) {

                call.respond(
                    HttpStatusCode.InternalServerError,
                    NetworkResponse(
                        e.message
                    )
                )

            }

        }

        post("/api/links/{id}/favorite") {

            val principle = call.principal<JwtConfig.JWTPrincipal>()!!
            val linkId = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)


            try {

                val updated = linkService.toggleFavorite(principle.userId, linkId)

                if (updated != null) {
                    call.respond(
                        NetworkResponse(
                            updated
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        NetworkResponse(
                            "Link not found"
                        )
                    )
                }

                call.respond(
                    NetworkResponse(
                        updated
                    )
                )

            } catch (e: Exception) {

                call.respond(
                    HttpStatusCode.InternalServerError,
                    NetworkResponse(
                        e.message
                    )
                )

            }

        }

        post("/api/links/{id}/read") {

            val principle = call.principal<JwtConfig.JWTPrincipal>()!!
            val linkId = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)


            try {

                val updated = linkService.markAsRead(principle.userId, linkId)

                if (updated != null) {
                    call.respond(
                        NetworkResponse(
                            updated
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.NotFound,
                        NetworkResponse(
                            "Link not found"
                        )
                    )
                }

                call.respond(
                    NetworkResponse(
                        updated
                    )
                )

            } catch (e: Exception) {

                call.respond(
                    HttpStatusCode.InternalServerError,
                    NetworkResponse(
                        e.message
                    )
                )

            }

        }


        get("/api/links/stats") {

            val principle = call.principal<JwtConfig.JWTPrincipal>()!!

            try {
                call.respond(
                    NetworkResponse(
                        linkService.getLinksCount(principle.userId)
                    )
                )

            } catch (e: Exception) {

                call.respond(
                    HttpStatusCode.InternalServerError,
                    NetworkResponse(
                        e.message
                    )
                )

            }

        }

    }


}