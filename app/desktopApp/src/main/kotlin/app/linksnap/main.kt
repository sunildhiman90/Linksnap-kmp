package app.linksnap

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import app.linksnap.di.initKoin

fun main() = application {

    initKoin {  }
    Window(
        onCloseRequest = ::exitApplication,
        title = "Linksnap-kmp",
    ) {
        App()
    }
}