package renard.remi.ping.ui.login

import android.content.Context
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
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
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_PASSWORD
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_PASSWORD_ERROR
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_TITLE
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_USERNAME
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_USERNAME_ERROR
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_GO_TO_REGISTER
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
    fun testScreenText() {
        composeRule.onNodeWithTag(LOGIN_SCREEN_WELCOME).assertTextEquals("Welcome to")
        composeRule.onNodeWithTag(LOGIN_SCREEN_APP_NAME).assertTextEquals("Ping")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_TITLE).assertTextEquals("Login")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_BUTTON).assertHasClickAction()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_BUTTON).assertTextEquals("Login")
    }

    @Test
    fun testLoginNominalCase() {
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME).performTextInput("Username")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD).performTextInput("P@ssw0rd")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_BUTTON).performClick()
    }

    @Test
    fun testLoginErrorCase() {
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME).performTextInput("A")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD).performTextInput("weak")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_BUTTON).performClick()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME_ERROR).assertIsDisplayed()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD_ERROR).assertIsDisplayed()

        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME).performTextClearance()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD).performTextClearance()

        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME).performTextInput("Username")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD).performTextInput("weak")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_BUTTON).performClick()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME_ERROR).assertIsNotDisplayed()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD_ERROR).assertIsDisplayed()

        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME).performTextClearance()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD).performTextClearance()

        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME).performTextInput("A")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD).performTextInput("P@ssw0rd")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_BUTTON).performClick()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME_ERROR).assertIsDisplayed()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD_ERROR).assertIsNotDisplayed()

        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME).performTextClearance()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD).performTextClearance()

        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME).performTextInput("Username")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD).performTextInput("P@ssw0rd")
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_BUTTON).performClick()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_USERNAME_ERROR).assertIsNotDisplayed()
        composeRule.onNodeWithTag(LOGIN_SCREEN_FORM_PASSWORD_ERROR).assertIsNotDisplayed()
    }

    @Test
    fun testNavigationToRegisterScreen() {
        composeRule.onNodeWithTag(LOGIN_SCREEN_GO_TO_REGISTER).performClick()
    }
}