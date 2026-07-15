package app.linksnap.di

import app.linksnap.features.auth.data.AuthRepository
import app.linksnap.features.auth.data.AuthRepositoryImpl
import app.linksnap.features.auth.data.SessionManager
import app.linksnap.features.home.data.LinkRepository
import app.linksnap.features.home.data.LinkRepositoryImpl
import app.linksnapkmp.BuildKonfig
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

fun commonModule() = module {

    single {
        Json {
            ignoreUnknownKeys =  true
            isLenient = true
            prettyPrint = true
        }
    }

    single {
        HttpClient(get()) {
            install(ContentNegotiation) {
                json(get())
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP Client: $message")
                    }
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 600000
                connectTimeoutMillis = 600000
                socketTimeoutMillis = 600000
            }

            defaultRequest {
                url(BuildKonfig.BASE_URL)
            }

        }
    }

    single<Settings> { Settings() }
    single { SessionManager(get(), get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    single<LinkRepository> { LinkRepositoryImpl(get(), get()) }

    

}