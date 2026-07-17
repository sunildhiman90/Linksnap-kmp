package app.linksnap.di

import app.linksnapkmp.BuildKonfig
import com.sunildhiman90.kmauth.core.KMAuthConfig
import com.sunildhiman90.kmauth.core.KMAuthInitializer
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

//Next we need to initialize it form platform specific code
fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {

    KMAuthInitializer.initialize(
        KMAuthConfig.forGoogle(
            webClientId = BuildKonfig.WEB_CLIENT_ID,
            clientSecret = BuildKonfig.WEB_CLIENT_SECRET,
            googleClientRedirectHost = "localhost:8081"
        )
    )

    appDeclaration()
    modules(commonModule(),viewModelModule(),platformModule())
}
