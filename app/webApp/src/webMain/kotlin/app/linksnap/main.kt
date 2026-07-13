package app.linksnap

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import app.linksnap.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin {  }
    
    ComposeViewport {
        App()
    }
}