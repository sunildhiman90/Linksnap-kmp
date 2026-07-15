package app.linksnap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import app.linksnap.app.navigation.RootContainer
import app.linksnap.app.theme.LinkSnapTheme
import org.jetbrains.compose.resources.painterResource

import linksnap_kmp.app.shared.generated.resources.Res
import linksnap_kmp.app.shared.generated.resources.compose_multiplatform
import linksnap_kmp.app.shared.generated.resources.paste_url_hint
import org.jetbrains.compose.resources.stringResource

@Composable
@Preview
fun App() {
    LinkSnapTheme {

        val navController = rememberNavController()
        RootContainer(navController)
    }
}