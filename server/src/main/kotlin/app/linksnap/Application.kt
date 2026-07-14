package app.linksnap

import app.linksnap.db.DatabaseFactory
import app.linksnap.di.serverModule
import app.linksnap.features.auth.api.authRoutes
import app.linksnap.features.auth.api.linkRoutes
import app.linksnap.features.auth.jwt.JwtConfig
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.core.context.startKoin

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()

    startKoin {
        modules(serverModule)
    }

    install(CORS) {
        allowHost("localhost:8080")
        allowHost("localhost:8081")
        allowHost("127.0.0.1:8080")
        allowHost("127.0.0.1:8081")

        allowHeader(io.ktor.http.HttpHeaders.ContentType)
        allowHeader(io.ktor.http.HttpHeaders.Authorization)

        allowMethod(io.ktor.http.HttpMethod.Get)
        allowMethod(io.ktor.http.HttpMethod.Post)
        allowMethod(io.ktor.http.HttpMethod.Put)
        allowMethod(io.ktor.http.HttpMethod.Delete)

        allowCredentials = true
    }


    install(ContentNegotiation) {
        json()
    }


    install(Authentication) {
        jwt("auth-jwt") {
            JwtConfig.configureKtor(this)
        }
    }

    routing {

        get("/") {
            call.respondText("Server is up and running!", ContentType.Text.Plain)
        }

        authRoutes()
        linkRoutes()
    }
}