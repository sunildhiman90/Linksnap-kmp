package app.linksnap.app.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class RootScreen {
    @Serializable
    object Auth: RootScreen()

    @Serializable
    object Main: RootScreen()

    @Serializable
    data class Detail(
        val linkId: String
    ): RootScreen()

}


@Serializable
sealed class MainScreen {
    @Serializable
    object Home: MainScreen()

    @Serializable
    object Favorites: MainScreen()

    @Serializable
    object Profile: MainScreen()

}


