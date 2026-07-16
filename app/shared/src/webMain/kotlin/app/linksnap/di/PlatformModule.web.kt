package app.linksnap.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.JsClient
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { JsClient().create() }
}