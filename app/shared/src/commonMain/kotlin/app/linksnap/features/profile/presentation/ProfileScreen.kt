package app.linksnap.features.profile.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import app.linksnap.app.theme.AppDimensions
import coil3.compose.AsyncImage
import io.ktor.util.hex
import org.koin.dsl.module


@Composable
fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    onNavigateToDetail: (String) -> Unit,
    bottomPadding: Dp = 0.dp,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier.padding(
                top = paddingValues.calculateTopPadding()
            )
                .padding(bottom = bottomPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = AppDimensions.paddingLarge),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppDimensions.cornerRadiusExtraLarge),
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                tonalElevation = 2.dp
            ) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = AppDimensions.paddingLarge,
                        vertical = AppDimensions.paddingMagazine
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {

                    Box(
                        modifier = Modifier.size(130.dp).background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape
                        ),
                        contentAlignment = Alignment.Center
                    ) {

                        Surface(
                            modifier = Modifier.size(110.dp),
                            shape = CircleShape,
                            border = BorderStroke(
                                3.dp,
                                MaterialTheme.colorScheme.primary
                            ),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                        ) {

                            if (state.user?.profilePicUrl != null) {
                                AsyncImage(
                                    model = state.user.profilePicUrl,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                            } else {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(30.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        state.user?.name ?: "User",
                        style = MaterialTheme.typography.displaySmall.copy(fontSize = 28.sp),
                        fontWeight = FontWeight.ExtraBold,
                    )

                    Text(
                        state.user?.email ?: "User",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (state.user?.isPro == true) {
                        BadgeChip(
                            text = "Pro Member",
                            isPrimary = true
                        )
                    } else {
                        BadgeChip(
                            text = "Free Member",
                            isPrimary = false
                        )
                    }
                }

            }


            //stats card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(AppDimensions.cornerRadiusExtraLarge),
                color = MaterialTheme.colorScheme.primary,
                tonalElevation = 2.dp
            ) {

                Column(
                    modifier = Modifier.padding(
                        AppDimensions.paddingMagazine
                    ),
                ) {
                    Text(
                        "Total Savings",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimary.copy(
                            alpha = 0.7f
                        )
                    )

                    Text(
                        state.linksCount.toString(),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        "Links organized across multiple smart categories curated by AI",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(
                            alpha = 0.7f
                        )
                    )

                }

            }


            //recent reads

            if (state.recentLinks.isNotEmpty()) {
                //heading
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Recent reads",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        TextButton(onClick = {

                        }) {
                            Text(
                                "See all",
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    //surface
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(AppDimensions.cornerRadiusExtraLarge),
                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                        tonalElevation = 2.dp
                    ) {

                        val uriLauncher = LocalUriHandler.current

                        Column(
                            modifier = Modifier.padding(AppDimensions.paddingLarge)
                        ) {

                            state.recentLinks.forEachIndexed { index, link ->
                                RecentItem(
                                    title = link.title,
                                    subtitle = link.category,
                                    onLinkClick = {
                                        uriLauncher.openUri(link.originalUrl)
                                    }
                                )

                                if (index < state.recentLinks.size - 1) {

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 12.dp),
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                    )

                                }
                            }
                        }
                    }

                }

            }


            Spacer(modifier = Modifier.height(16.dp))

            //Logout button

            Button(
                onClick = {
                    onEvent(ProfileEvent.Logout)
                },
                modifier = Modifier.fillMaxSize(0.7f).height(56.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.AutoMirrored.Default.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        "Logout",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                "Version 2.4.1",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                    alpha = 0.5f
                )
            )
            Spacer(modifier = Modifier.width(32.dp))

        }
    }

}

@Composable
fun BadgeChip(text: String, isPrimary: Boolean = true) {
    Surface(
        shape = RoundedCornerShape(AppDimensions.cornerRadiusMedium),
        color = if (isPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {

        Text(
            text = text,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = if (isPrimary) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )

    }

}

//RecentItem
@Composable
fun RecentItem(
    title: String,
    subtitle: String,
    onLinkClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onLinkClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        //square thumbnail surface with border
        Surface(
            modifier = Modifier.size(56.dp),
            shape = RoundedCornerShape(AppDimensions.cornerRadiusMedium),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
        ) {
            Column(modifier = Modifier.padding(8.dp)) {

                Box(
                    modifier = Modifier.fillMaxWidth().height(4.dp).background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.3f
                        ),
                        CircleShape
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(0.7f).height(4.dp).background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.3f
                        ),
                        CircleShape
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(0.9f).height(4.dp).background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.3f
                        ),
                        CircleShape
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))


                Box(
                    modifier = Modifier.fillMaxWidth(0.5f).height(4.dp).background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.3f
                        ),
                        CircleShape
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                subtitle,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                    alpha = 0.6f
                )
            )
        }

        Icon(
            Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.5f
            ),
            modifier = Modifier.size(20.dp)
        )


    }


}
