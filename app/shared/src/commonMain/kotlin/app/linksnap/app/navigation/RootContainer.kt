package app.linksnap.app.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import app.linksnap.app.components.AppBottomBar
import app.linksnap.features.auth.presentation.AuthViewModel
import app.linksnap.features.auth.presentation.LoginScreen
import app.linksnap.features.detail.presentation.DetailEvent
import app.linksnap.features.detail.presentation.DetailScreen
import app.linksnap.features.detail.presentation.DetailViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun RootContainer(
    navController: NavHostController,
    authViewModel: AuthViewModel = koinViewModel()
) {

    val authState = authViewModel.state.collectAsStateWithLifecycle()

    val startDestination = if (authState.value.isLoggedIn) RootScreen.Main else RootScreen.Auth

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.fillMaxSize()
    ) {

        composable<RootScreen.Auth> {

            LoginScreen(
                state = authState.value,
                onEvent = authViewModel::onEvent
            )

        }

        composable<RootScreen.Main> {
            MainContainer(
                onNavigateToDetail = {
                    navController.navigate(RootScreen.Detail(it))
                },
                onLogout = {
                    navController.navigate(RootScreen.Auth) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<RootScreen.Detail> { backStackEntry ->

            val route = backStackEntry.toRoute<RootScreen.Detail>()

            val viewModel = koinViewModel<DetailViewModel>()

            val state = viewModel.state.collectAsStateWithLifecycle()


            LaunchedEffect(route.linkId) {
                viewModel.onEvent(DetailEvent.LoadDetail(route.linkId))
            }

            DetailScreen(
                state = state.value,
                onEvent = viewModel::onEvent,
                onBack = {
                    navController.popBackStack()
                }
            )


        }

    }
}