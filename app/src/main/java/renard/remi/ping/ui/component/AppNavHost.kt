package renard.remi.ping.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import renard.remi.ping.domain.model.BaseRoute
import renard.remi.ping.ui.create_account.CreateAccountScreen
import renard.remi.ping.ui.create_account.CreateAccountScreenRoute
import renard.remi.ping.ui.create_account.CreateAccountViewModel
import renard.remi.ping.ui.home.HomeScreen
import renard.remi.ping.ui.home.HomeScreenRoute
import renard.remi.ping.ui.home.HomeViewModel
import renard.remi.ping.ui.login.LoginScreen
import renard.remi.ping.ui.login.LoginScreenRoute
import renard.remi.ping.ui.login.LoginViewModel
import renard.remi.ping.ui.settings.SettingsScreen
import renard.remi.ping.ui.settings.SettingsScreenRoute
import renard.remi.ping.ui.settings.SettingsViewModel

@Composable
fun AppNavHost(navController : NavHostController, startDestination: BaseRoute, scaffoldPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<CreateAccountScreenRoute> {
            val viewModel = hiltViewModel<CreateAccountViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            CreateAccountScreen(
                onLoginClicked = { navController.navigate(LoginScreenRoute) },
                events = viewModel.events,
                onEvent = viewModel::onEvent,
                state = state
            )
        }
        composable<LoginScreenRoute> {
            val viewModel = hiltViewModel<LoginViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LoginScreen(
                state = state,
                events = viewModel.events,
                onEvent = viewModel::onEvent,
                onRegisterClicked = {
                    navController.navigate(
                        CreateAccountScreenRoute
                    )
                }
            )
        }
        composable<HomeScreenRoute> {
            val viewModel = hiltViewModel<HomeViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            HomeScreen(
                modifier = Modifier.padding(scaffoldPadding),
                onEvent = viewModel::onEvent,
                events = viewModel.events,
                state = state
            )
        }
        composable<WebViewRoute> {
            WebView(
                modifier = Modifier.padding(scaffoldPadding),
                url = it.toRoute<WebViewRoute>().url
            )
        }
        composable<SettingsScreenRoute> {
            val viewModel = hiltViewModel<SettingsViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            SettingsScreen(
                modifier = Modifier.padding(scaffoldPadding),
                onEvent = viewModel::onEvent,
                events = viewModel.events,
                state = state
            )
        }
    }
}