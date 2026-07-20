package app.linksnap.app.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.linksnap.app.components.AppBottomBar
import app.linksnap.features.home.presentation.HomeScreen
import app.linksnap.features.home.presentation.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun MainContainer(
    onNavigateToDetail: (String) -> Unit,
    onLogout: () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        bottomBar = {

            val currentRoute = navBackStackEntry?.destination?.route

            AppBottomBar(
                currentRoute = currentRoute,
                onNavigate = { screen ->
                    navController.navigate(screen) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = MainScreen.Home,
            modifier = Modifier.fillMaxSize()
        ) {

            composable<MainScreen.Home> {
                val viewModel = koinViewModel<HomeViewModel>()

                val state = viewModel.state.collectAsStateWithLifecycle()

                HomeScreen(
                    state = state.value,
                    onEvent = viewModel::onEvent,
                    bottomPadding = paddingValues.calculateBottomPadding(),
                    onLinkClick = {

                    },
                )

            }

            composable<MainScreen.Favorites> {
                Column {
                    Text("Fav")
                }
            }

            composable<MainScreen.Profile> {
                Column {
                    Text("Profile")
                }
            }

        }


    }



}