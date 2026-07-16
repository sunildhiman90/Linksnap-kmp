package app.linksnap.di

import io.ktor.client.engine.HttpClientEngine
import okhttp3.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { io.ktor.client.engine.okhttp.OkHttp.create() }
}