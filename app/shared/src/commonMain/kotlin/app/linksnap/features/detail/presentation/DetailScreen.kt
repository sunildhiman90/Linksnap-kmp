package app.linksnap.features.detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.linksnap.app.theme.AppDimensions
import app.linksnap.models.LinkSummary
import coil3.compose.AsyncImage
import kotlin.math.sign


@Composable
fun DetailScreen(
    state: DetailState,
    onEvent: (DetailEvent) -> Unit,
    bottomPadding: Dp = 0.dp,
    onBack: () -> Unit,
) {
    val scrollState = rememberScrollState()

    val urlLauncher = LocalUriHandler.current

    LaunchedEffect(state.link) {
        if (state.link != null) {
            onEvent(
                DetailEvent.MarkAsRead
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            DetailTopBar(
                onBack = onBack,
                isFavorite = state.link?.isFavorite ?: false,
                onToggleFavorite = {
                    onEvent(DetailEvent.ToggleFavorite)
                }
            )
        },
        bottomBar = {
            DetailBottomBar {
                urlLauncher.openUri(state.link?.originalUrl ?: "")
            }
        }
    ) { paddingValues ->

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (state.link != null) {
            Column(
                modifier = Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                ).padding(
                    bottom = 100.dp
                )
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {


                DetailHeader(
                    state.link
                )

                state.link.imageUrl?.let {
                    HeroImage(
                        it
                    )
                }

                AiSummary(
                    state.link
                )


                Column(
                    modifier = Modifier.padding(horizontal = AppDimensions.paddingLarge)
                ) {

                    state.link.bodySnippet?.let {
                        Text(
                            it,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                }

            }


        } else {


        }


    }

}


@Composable
private fun DetailHeader(link: LinkSummary) {
    val wordCount = link.bodySnippet?.split("\\s+".toRegex())?.size ?: 0
    val readTime = (wordCount / 200.0).coerceAtLeast(1.0).toInt()


    Column(
        modifier = Modifier.padding(
            horizontal = AppDimensions.paddingLarge,
            vertical = AppDimensions.paddingMagazine
        )
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(AppDimensions.cornerRadiusFull)
            ) {

                Text(
                    text = link.category.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    ),
                    fontWeight = FontWeight.Bold
                )

            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(AppDimensions.cornerRadiusFull)
            ) {

                Text(
                    text = "$readTime MIN READ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 4.dp
                    ),
                    fontWeight = FontWeight.Bold
                )

            }

        }

        Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))

        Text(
            text = link.title,
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.ExtraBold,
            lineHeight = 42.sp
        )
    }

}


@Composable
private fun HeroImage(url: String) {
    Box(
        modifier = Modifier.padding(horizontal = AppDimensions.paddingLarge).fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(AppDimensions.cornerRadiusLarge))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun AiSummary(link: LinkSummary) {
    Box(
        modifier = Modifier.padding(
            vertical = AppDimensions.paddingMagazine,
            horizontal = AppDimensions.paddingLarge
        )
    ) {

        Box(
            modifier = Modifier.matchParentSize()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape)
        )

        Column(
            modifier = Modifier.padding(start = AppDimensions.paddingLarge)
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Ai Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))

            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                shape = RoundedCornerShape(AppDimensions.cornerRadiusLarge)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = AppDimensions.paddingExtraLarge)
                        .padding(start = AppDimensions.paddingExtraLarge)
                ) {

                    Text(
                        link.aiSummary,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }

    }
}


@Composable
fun DetailTopBar(onBack: () -> Unit, isFavorite: Boolean, onToggleFavorite: () -> Unit) {

    TopAppBar(
        title = {
            Text(
                "LinkSnap",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        actions = {
            IconButton(
                onClick = onToggleFavorite
            ) {
                Icon(
                    if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )

}

@Composable
fun DetailBottomBar(onOpen: () -> Unit) {
    Surface(
        modifier = Modifier.navigationBarsPadding().fillMaxWidth().height(100.dp),
        color = MaterialTheme.colorScheme.background.copy(
            alpha = 0.7f
        ),
        tonalElevation = 4.dp
    ) {

        Row(
            modifier = Modifier.padding(horizontal = AppDimensions.paddingLarge),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = onOpen,
                modifier = Modifier.weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(AppDimensions.cornerRadiusFull)),
                shape = RoundedCornerShape(AppDimensions.cornerRadiusFull),
                contentPadding = PaddingValues(0.dp)
            ) {

                Box(
                    modifier = Modifier.fillMaxSize().background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                        .padding(horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Open In Browser",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }

}
