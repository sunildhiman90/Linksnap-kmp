package app.linksnap.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.linksnap.app.components.LinkCard
import app.linksnap.app.theme.AppDimensions
import linksnap_kmp.app.shared.generated.resources.Res
import linksnap_kmp.app.shared.generated.resources.empty_feed
import linksnap_kmp.app.shared.generated.resources.paste_url_hint
import linksnap_kmp.app.shared.generated.resources.search_knowledge_hint
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onLinkClick: (String) -> Unit,
    bottomPadding: Dp = 0.dp
) {

    var urlToSummarize by remember {
        mutableStateOf("")
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredLinks = remember(state.links, state.searchQuery) {
        state.links.filter {
            it.title.contains(state.searchQuery, ignoreCase = true) ||
                    it.aiSummary.contains(state.searchQuery, ignoreCase = true) ||
                    it.category.contains(state.searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal),
        floatingActionButton = {

            Button(
                onClick = {
                    showBottomSheet = true
                },
                modifier = Modifier
                    .padding(bottom = bottomPadding)
                    .height(56.dp),
                shape = RoundedCornerShape(
                    AppDimensions.cornerRadiusLarge
                ),
                contentPadding = PaddingValues(0.dp)
            ) {

                Box(
                    modifier = Modifier.fillMaxHeight().background(
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

                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Save Link",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }


                }

            }

        }
    ) { padding ->

        Column(
            modifier = Modifier.padding(
                bottom = bottomPadding
            )
        ) {

            HomeAppBar()

            HomeSearchBar(
                query = state.searchQuery,
                onQueryChange = {
                    onEvent(HomeEvent.UpdateSearchQuery(it))
                }
            )

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else if (filteredLinks.isEmpty()) {
                EmptyState(state.searchQuery)
            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = AppDimensions.paddingLarge,
                        end = AppDimensions.paddingLarge,
                        top = AppDimensions.paddingMedium,
                        bottom = 100.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(AppDimensions.paddingLarge)
                ) {

                    items(filteredLinks, key = {
                        it.id
                    }) { link ->

                        val uriHandler = LocalUriHandler.current

                        LinkCard(
                            link = link,
                            onClick = {
                                onLinkClick(link.id)
                            },
                            onOpenLink = {
                                uriHandler.openUri(link.originalUrl)
                                onEvent(HomeEvent.MarkAsRead(link.id))
                            },
                            onFavoriteToggle = {
                                onEvent(HomeEvent.ToggleFavorite(link.id))
                            },
                        )

                    }
                }
            }

        }

        if (showBottomSheet) {

            AddLinkBottomSheet(
                sheetState = sheetState,
                url = urlToSummarize,
                onUrlChange = {
                    urlToSummarize = it
                },
                onSummarize = {
                    if (urlToSummarize.isEmpty()) {
                        return@AddLinkBottomSheet
                    }
                    onEvent(HomeEvent.Summarize(urlToSummarize))
                },
                onDismiss = {
                    if (!state.isSummarizing) {
                        showBottomSheet = false
                    }
                },
                isSummarizing = state.isSummarizing,
            )

        }

        LaunchedEffect(state.links.size) {

            if (showBottomSheet) {
                sheetState.hide()
                showBottomSheet = false
                urlToSummarize = ""
            }
        }

    }

}

@Composable
fun HomeAppBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().statusBarsPadding()
            .padding(top = 16.dp, bottom = AppDimensions.paddingMedium)
    ) {
        Icon(
            Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "LinkSnap",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@Composable
fun HomeSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxWidth().padding(
            horizontal = AppDimensions.paddingLarge,
            vertical = AppDimensions.paddingMedium
        )
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    stringResource(Res.string.search_knowledge_hint),
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(20.dp)
                )
            },
            shape = RoundedCornerShape(
                AppDimensions.cornerRadiusLarge
            ),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                focusedBorderColor = MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.2f
                ),
                unfocusedBorderColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.bodyMedium
        )

    }

}

@Composable
fun EmptyState(query: String) {
    Box(
        modifier = Modifier.fillMaxSize().padding(top = AppDimensions.paddingHero),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline.copy(
                    alpha = 0.3f
                ),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = if (query.isEmpty()) stringResource(Res.string.empty_feed) else "No saved links found for $query",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLinkBottomSheet(
    url: String,
    onUrlChange: (String) -> Unit,
    onSummarize: () -> Unit,
    onDismiss: () -> Unit,
    isSummarizing: Boolean,
    sheetState: SheetState
) {

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .width(48.dp)
                    .height(4.dp)
                    .background(
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                        RoundedCornerShape(16.dp)
                    ),
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = AppDimensions.paddingLarge,
                )
                .padding(bottom = AppDimensions.paddingHero),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Add New Link",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Paste any article url and our AI will curate brief for you",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingMagazine))

            OutlinedTextField(
                value = url,
                onValueChange = onUrlChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isSummarizing,
                placeholder = {
                    Text(
                        stringResource(Res.string.paste_url_hint),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                shape = RoundedCornerShape(
                    AppDimensions.cornerRadiusLarge
                ),
                colors = OutlinedTextFieldDefaults.colors(

                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    focusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(
                        alpha = 0.2f
                    ),
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.height(AppDimensions.paddingLarge))

            Button(
                onClick = onSummarize,
                enabled = !isSummarizing,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(
                    AppDimensions.cornerRadiusLarge
                ),
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
                    if (isSummarizing) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Summarize Now",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                }

            }


            Spacer(modifier = Modifier.height(AppDimensions.paddingMagazine))

            Text(
                "Private & secure * Instant results",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )

        }
    }
}