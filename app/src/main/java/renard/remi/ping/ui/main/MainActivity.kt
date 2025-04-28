package renard.remi.ping.ui.main

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import renard.remi.ping.extension.dataStore
import renard.remi.ping.extension.removeAllAfterSlash
import renard.remi.ping.ui.component.AppBackground
import renard.remi.ping.ui.component.AppNavHost
import renard.remi.ping.ui.component.CustomBottomBar
import renard.remi.ping.ui.component.WebViewRoute
import renard.remi.ping.ui.create_account.CreateAccountScreenRoute
import renard.remi.ping.ui.home.HomeScreenRoute
import renard.remi.ping.ui.login.LoginScreenRoute
import renard.remi.ping.ui.settings.SettingsScreenRoute
import renard.remi.ping.ui.theme.AppTheme
import java.util.Locale

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val mainViewModel = hiltViewModel<MainViewModel>()
                val mainState by mainViewModel.state.collectAsStateWithLifecycle()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route.toString()
                val routeWithBottomBar = listOf(
                    HomeScreenRoute.javaClass.name,
                    WebViewRoute::class.java.name,
                )
                val routeWithBackground = listOf(
                    LoginScreenRoute.javaClass.name,
                    CreateAccountScreenRoute.javaClass.name,
                    HomeScreenRoute.javaClass.name,
                    SettingsScreenRoute.javaClass.name
                )

                Scaffold { paddingValues ->

                    AppBackground(isVisible = routeWithBackground.contains(currentRoute.removeAllAfterSlash()))

                    AppNavHost(
                        navController = navController,
                        startDestination = if (mainState.isConnected == true) HomeScreenRoute else LoginScreenRoute,
                        scaffoldPadding = paddingValues
                    )

                    CustomBottomBar(
                        modifier = Modifier.padding(paddingValues),
                        isVisible = routeWithBottomBar.contains(currentRoute.removeAllAfterSlash()),
                        currentRoute = currentRoute,
                        onTabClicked = {
                            navController.navigate(it.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onSettingsClicked = {
                            navController.navigate(SettingsScreenRoute)
                        }
                    )
                }
            }
        }
    }

    override fun attachBaseContext(context: Context) {
        val newConfig = context.resources.configuration
        val currentLanguage = runBlocking { context.dataStore.data.first().currentLanguage }
        currentLanguage?.let {
            try {
                // TODO improve this
                // FOR NOW I HAVE DECIDED TO TAKE 2 FIRST CHARS OF A HARDCODED STRING
                // IT'S ONLY FOR DEV !
                newConfig?.setLocale(Locale(it.take(2).lowercase()))
                applyOverrideConfiguration(newConfig)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        super.attachBaseContext(context)
    }
}

