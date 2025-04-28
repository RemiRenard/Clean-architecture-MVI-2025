package renard.remi.ping.ui.login

import android.architecture.app.R
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import renard.remi.ping.domain.model.BaseRoute
import renard.remi.ping.extension.UiText
import renard.remi.ping.ui.component.AppButton
import renard.remi.ping.ui.component.AppTextField
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_APP_NAME
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_BUTTON
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_PASSWORD
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_PASSWORD_ERROR
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_TITLE
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_USERNAME
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_FORM_USERNAME_ERROR
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_GO_TO_REGISTER
import renard.remi.ping.ui.utils.TestTags.LOGIN_SCREEN_WELCOME

@Serializable
object LoginScreenRoute : BaseRoute

@Composable
fun LoginScreen(
    state: LoginState,
    onEvent: (LoginEventFromUI) -> Unit = {},
    events: Flow<LoginEventFromVm>? = null,
    onRegisterClicked: (() -> Unit)? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(Lifecycle.Event.ON_CREATE) {
        events?.collect { event ->
            when (event) {
                is LoginEventFromVm.Error -> {
                    Toast.makeText(
                        context,
                        event.errorMessage.asString(context),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is LoginEventFromVm.LoginSuccess -> {
                    // Do nothing here, redirection is managed by the navHost which collect
                    // information from datastore to handle startDestination
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.1F))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .testTag(LOGIN_SCREEN_WELCOME),
            text = stringResource(R.string.auth_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .testTag(LOGIN_SCREEN_APP_NAME),
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(R.string.app_name),
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.1F))
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .alpha(0.8F)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                shape = RoundedCornerShape(
                    topEnd = 50.dp,
                    topStart = 50.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                ),
            ) {
                Spacer(modifier = Modifier.size(50.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .testTag(LOGIN_SCREEN_FORM_TITLE),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraLight,
                    text = stringResource(R.string.login_title),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(24.dp))
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    textFieldTestTag = LOGIN_SCREEN_FORM_USERNAME,
                    errorMessageTestTag = LOGIN_SCREEN_FORM_USERNAME_ERROR,
                    value = state.username,
                    label = stringResource(R.string.login_field_username),
                    isError = state.usernameError?.asString()?.isNotBlank() == true,
                    errorMessage = state.usernameError?.asString(),
                    maxLines = 1,
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = stringResource(R.string.login_field_username),
                        )
                    },
                    onValueChange = { username ->
                        onEvent(LoginEventFromUI.ChangeUsername(username))
                    }
                )
                Spacer(modifier = Modifier.size(12.dp))
                AppTextField(
                    modifier = Modifier
                        .focusRequester(FocusRequester())
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    textFieldTestTag = LOGIN_SCREEN_FORM_PASSWORD,
                    errorMessageTestTag = LOGIN_SCREEN_FORM_PASSWORD_ERROR,
                    value = state.password,
                    label = stringResource(R.string.login_field_password),
                    isPassword = true,
                    maxLines = 1,
                    onDone = {
                        keyboardController?.hide()
                        localFocusManager.clearFocus()
                        onEvent(LoginEventFromUI.SubmitLogin)
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            modifier = Modifier.clickable {
                                onEvent(LoginEventFromUI.ShowPasswordClicked)
                            },
                            contentDescription = stringResource(R.string.login_field_password)
                        )
                    },
                    isError = state.passwordError?.asString()?.isNotBlank() == true,
                    isTextVisible = state.isPasswordVisible,
                    errorMessage = state.passwordError?.asString(),
                    onValueChange = { password ->
                        onEvent(LoginEventFromUI.ChangePassword(password))
                    }
                )
                Spacer(modifier = Modifier.size(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    AppButton(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .fillMaxWidth()
                            .testTag(LOGIN_SCREEN_FORM_BUTTON),
                        onClick = {
                            onEvent(LoginEventFromUI.SubmitLogin)
                        },
                        isLoading = state.isLoading,
                        isEnabled = !state.isLoading,
                        textBtn = stringResource(R.string.login_btn_text)
                    )
                }
                Spacer(modifier = Modifier.size(30.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        modifier = Modifier
                            .testTag(LOGIN_SCREEN_GO_TO_REGISTER)
                            .clickable {
                                onRegisterClicked?.invoke()
                            },
                        text = buildAnnotatedString {
                            append(stringResource(R.string.login_go_to_create_account_title) + " ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.login_go_to_create_account_subtitle))
                            }
                        },
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.size(30.dp))
            }
        }
    }
}

@PreviewScreenSizes
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenEmptyStatePreview() {
    LoginScreen(state = LoginState())
}

@PreviewScreenSizes
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenLoadingStatePreview() {
    LoginScreen(state = LoginState(isLoading = true))
}

@PreviewScreenSizes
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun LoginScreenErrorStatePreview() {
    LoginScreen(
        state = LoginState(
            usernameError = UiText.StringResource(R.string.login_field_username_error_msg),
            passwordError = UiText.StringResource(R.string.login_field_password_error_msg)
        )
    )
}



