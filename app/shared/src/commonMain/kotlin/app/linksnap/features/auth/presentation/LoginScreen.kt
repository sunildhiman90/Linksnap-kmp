package app.linksnap.features.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.linksnap.app.theme.AppDimensions
import com.sunildhiman90.kmauth.google.compose.GoogleSignInButton
import linksnap_kmp.app.shared.generated.resources.Res
import linksnap_kmp.app.shared.generated.resources.login_subtitle
import linksnap_kmp.app.shared.generated.resources.login_title
import linksnap_kmp.app.shared.generated.resources.login_welcome_back
import org.jetbrains.compose.resources.stringResource


@Composable
fun LoginScreen(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {

    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {

        //Decorations
        BgDecorations()


        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
                .padding(horizontal = AppDimensions.paddingLarge)
                .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    "LinkSnap",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingMagazine))


            Text(
                stringResource(Res.string.login_title),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = AppDimensions.paddingSmall)
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))


            Text(
                stringResource(Res.string.login_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingHero))

            AuthCard(
                state, onEvent
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingHero))

            Footer()

        }


    }


}

@Composable
fun BgDecorations() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.size(400.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = -(50).dp)
                .background(
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.05f
                    ),
                    CircleShape,
                ).blur(100.dp),
        )
    }
}

@Composable
fun AuthCard(
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxWidth().clip(
            RoundedCornerShape(AppDimensions.cornerRadiusLarge)
        ),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
        tonalElevation = 2.dp
    ) {

        Column(
            modifier = Modifier.padding(AppDimensions.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                stringResource(Res.string.login_welcome_back),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingExtraLarge))

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                GoogleSignInButton(
                    modifier = Modifier.clip(
                        RoundedCornerShape(AppDimensions.cornerRadiusLarge)
                    )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(AppDimensions.cornerRadiusLarge)
                        ),
                    onSignInResult = { user, throwable ->

                        if (user != null) {
                            if (user.idToken != null && user.email != null) {
                                onEvent(
                                    AuthEvent.LoginWithGoogle(
                                        idToken = user.idToken!!,
                                        email = user.email!!,
                                        name = user.name,
                                        profilePicUrl = user.profilePicUrl,
                                    )
                                )
                            }
                        }
                    }
                )
            }

            state.error?.let {
                Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

    }

}


@Composable
fun Footer() {


    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = AppDimensions.paddingLarge)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                "LinkSnap",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                "Privacy Policy",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                "Terms of Service",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                "Contact",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingMedium))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                "@2024 Copyright LinkSnap",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.5f
                )
            )
        }

    }


}