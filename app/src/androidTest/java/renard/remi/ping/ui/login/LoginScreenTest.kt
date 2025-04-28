package renard.remi.ping.ui.login

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.rememberNavController
import androidx.test.platform.app.InstrumentationRegistry
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import renard.remi.ping.di.DatabaseModule
import renard.remi.ping.di.NetworkModule
import renard.remi.ping.ui.component.AppNavHost
import renard.remi.ping.ui.main.MainActivity
import renard.remi.ping.ui.theme.AppTheme
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_APP_NAME
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_BUTTON
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_TITLE
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_WELCOME

@HiltAndroidTest
@UninstallModules(NetworkModule::class, DatabaseModule::class)
class LoginScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().context
        composeRule.activity.setContent {
            AppTheme {
                AppNavHost(
                    navController = rememberNavController(),
                    startDestination = LoginScreenRoute,
                    scaffoldPadding = PaddingValues()
                )
            }
        }
    }

    @Test
    fun testTextScreen() {
        composeRule.onNodeWithTag(LOGIN_SCREEN_WELCOME).assertTextEquals("Welcome to")
        composeRule.onNodeWithTag(LOGIN_SCREEN_APP_NAME).assertTextEquals("Ping")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_TITLE).assertTextEquals("Login")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_BUTTON).assertHasClickAction()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_BUTTON).assertTextEquals("Login")
    }
}