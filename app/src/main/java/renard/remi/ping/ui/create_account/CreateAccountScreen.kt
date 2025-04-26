package renard.remi.ping.ui.create_account

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
import renard.remi.ping.extension.UiText
import renard.remi.ping.extension.dataStore
import renard.remi.ping.ui.component.AppButton
import renard.remi.ping.ui.component.AppTextField

@Serializable
object CreateAccountScreenRoute

@Composable
fun CreateAccountScreen(
    state: CreateAccountState,
    onEvent: (CreateAccountEventFromUI) -> Unit = {},
    events: Flow<CreateAccountEventFromVm>? = null,
    onLoginClicked: (() -> Unit)? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(Lifecycle.Event.ON_CREATE) {
        events?.collect { event ->
            when (event) {
                is CreateAccountEventFromVm.Error -> {
                    Toast.makeText(
                        context,
                        event.errorMessage.asString(context),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is CreateAccountEventFromVm.CreateAccountSuccess -> {
                    // Updating accessToken via datastore update the startDestination of the NavHost
                    context.dataStore.updateData {
                        it.copy(
                            accessToken = event.accessToken,
                            userId = event.userId
                        )
                    }
                }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.fillMaxHeight(0.1F))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = stringResource(R.string.auth_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
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
                        .padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraLight,
                    text = stringResource(R.string.create_account_title),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.size(24.dp))
                AppTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = state.username,
                    label = stringResource(R.string.create_account_field_username),
                    isError = state.usernameError?.asString()?.isNotBlank() == true,
                    errorMessage = state.usernameError?.asString(),
                    maxLines = 1,
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = stringResource(R.string.create_account_field_username),
                        )
                    },
                    onValueChange = { username ->
                        onEvent(CreateAccountEventFromUI.ChangeUsername(username))
                    }
                )
                Spacer(modifier = Modifier.size(12.dp))
                AppTextField(
                    modifier = Modifier
                        .focusRequester(FocusRequester())
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = state.password,
                    label = stringResource(R.string.create_account_field_password),
                    isPassword = true,
                    maxLines = 1,
                    onNext = {
                        localFocusManager.moveFocus(FocusDirection.Down)
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            modifier = Modifier.clickable {
                                onEvent(CreateAccountEventFromUI.ShowPasswordClicked)
                            },
                            contentDescription = stringResource(R.string.create_account_field_password)
                        )
                    },
                    isError = state.passwordError?.asString()?.isNotBlank() == true,
                    isTextVisible = state.isPasswordVisible,
                    errorMessage = state.passwordError?.asString(),
                    onValueChange = { password ->
                        onEvent(CreateAccountEventFromUI.ChangePassword(password))
                    }
                )
                Spacer(modifier = Modifier.size(12.dp))
                AppTextField(
                    modifier = Modifier
                        .focusRequester(FocusRequester())
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = state.confirmPassword,
                    label = stringResource(R.string.create_account_field_confirm_password),
                    isPassword = true,
                    maxLines = 1,
                    onDone = {
                        keyboardController?.hide()
                        localFocusManager.clearFocus()
                        onEvent(CreateAccountEventFromUI.SubmitAccountCreation)
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            modifier = Modifier.clickable {
                                onEvent(CreateAccountEventFromUI.ShowConfirmPasswordClicked)
                            },
                            contentDescription = stringResource(R.string.create_account_field_confirm_password)
                        )
                    },
                    isError = state.confirmPasswordError?.asString()
                        ?.isNotBlank() == true,
                    isTextVisible = state.isConfirmPasswordVisible,
                    errorMessage = state.confirmPasswordError?.asString(),
                    onValueChange = { password ->
                        onEvent(CreateAccountEventFromUI.ChangeConfirmPassword(password))
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
                            .fillMaxWidth(),
                        onClick = {
                            onEvent(CreateAccountEventFromUI.SubmitAccountCreation)
                        },
                        isLoading = state.isLoading,
                        isEnabled = !state.isLoading,
                        textBtn = stringResource(R.string.create_account_btn_text)
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
                        modifier = Modifier.clickable {
                            onLoginClicked?.invoke()
                        },
                        text = buildAnnotatedString {
                            append(stringResource(R.string.create_account_go_to_login_title) + " ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.create_account_go_to_login_subtitle))
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
    showBackground = false,
    showSystemUi = true
)
@Composable
fun CreateAccountScreenEmptyStatePreview() {
    CreateAccountScreen(state = CreateAccountState())
}

@PreviewScreenSizes
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CreateAccountScreenLoadingStatePreview() {
    CreateAccountScreen(
        state = CreateAccountState(
            isLoading = true
        )
    )
}

@PreviewScreenSizes
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CreateAccountScreenErrorStatePreview() {
    CreateAccountScreen(
        state = CreateAccountState(
            usernameError = UiText.StringResource(R.string.create_account_field_username_error_msg),
            passwordError = UiText.StringResource(R.string.create_account_field_password_error_msg),
            confirmPasswordError = UiText.StringResource(R.string.create_account_field_confirm_field_password_error_msg),
        )
    )
}


