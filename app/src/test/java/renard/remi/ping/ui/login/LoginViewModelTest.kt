package renard.remi.ping.ui.login

import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import renard.remi.ping.data.network.dto.UserDto
import renard.remi.ping.data.network.dto.response.AuthResponse
import renard.remi.ping.domain.model.DataError
import renard.remi.ping.domain.model.Result
import renard.remi.ping.domain.use_case.LoginUseCase
import renard.remi.ping.domain.use_case.ValidatePasswordUseCase
import renard.remi.ping.domain.use_case.ValidateUsernameUseCase
import renard.remi.ping.extension.UiText

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    @MockK
    private lateinit var validateUsernameUseCase: ValidateUsernameUseCase

    @MockK
    private lateinit var validatePasswordUseCase: ValidatePasswordUseCase

    @MockK
    private lateinit var loginUseCase: LoginUseCase

    @BeforeEach
    fun setUp() {
        createViewModel()
    }

    private fun createViewModel() {
        loginViewModel = LoginViewModel(
            loginUseCase = loginUseCase,
            validateUsernameUseCase = validateUsernameUseCase,
            validatePasswordUseCase = validatePasswordUseCase
        )
    }

    @Test
    fun `Test initial state`() = runTest {
        val states = mutableListOf<LoginState>()

        val job = launch {
            loginViewModel.state.toList(states)
        }

        advanceUntilIdle()

        states[0].username shouldBe ""
        states[0].password shouldBe ""
        states[0].usernameError shouldBe null
        states[0].passwordError shouldBe null
        states[0].isLoading shouldBe false
        states[0].isPasswordVisible shouldBe false

        job.cancel()
    }

    @Test
    fun `Test to change username`() = runTest {
        val usernameExpected = "My username"
        loginViewModel.onEvent(LoginEventFromUI.ChangeUsername(usernameExpected))

        advanceUntilIdle()

        loginViewModel.state.value.username shouldBe usernameExpected
        loginViewModel.state.value.usernameError shouldBe null
    }

    @Test
    fun `Test to change password`() = runTest {
        val passwordExpected = "P@ssw0rd"
        loginViewModel.onEvent(LoginEventFromUI.ChangePassword(passwordExpected))

        advanceUntilIdle()

        loginViewModel.state.value.password shouldBe passwordExpected
        loginViewModel.state.value.passwordError shouldBe null
    }

    @Test
    fun `Test to change password visibility`() = runTest {
        loginViewModel.onEvent(LoginEventFromUI.ShowPasswordClicked)
        advanceUntilIdle()
        loginViewModel.state.value.isPasswordVisible shouldBe true

        loginViewModel.onEvent(LoginEventFromUI.ShowPasswordClicked)
        advanceUntilIdle()
        loginViewModel.state.value.isPasswordVisible shouldBe false
    }

    @Test
    fun `Test to submit login - Nominal case`() = runTest {
        val resultExpected = AuthResponse("accessToken", UserDto())

        every { validateUsernameUseCase.execute(any()) } returns true
        every { validatePasswordUseCase.execute(any()) } returns true
        coEvery {
            loginUseCase.execute(any(), any())
        } returns Result.Success(resultExpected)

        loginViewModel.onEvent(LoginEventFromUI.SubmitLogin)

        advanceUntilIdle()

        // TODO
    }

    @Test
    fun `Test to submit login - Network error case`() = runTest {
        val resultExpected = DataError.Network.UNKNOWN

        every { validateUsernameUseCase.execute(any()) } returns true
        every { validatePasswordUseCase.execute(any()) } returns true
        coEvery {
            loginUseCase.execute(any(), any())
        } returns Result.Error(resultExpected)

        loginViewModel.onEvent(LoginEventFromUI.SubmitLogin)

        advanceUntilIdle()

        // TODO
    }

    @Test
    fun `Test to submit login - Username error case`() = runTest {
        val resultExpected = AuthResponse("accessToken", UserDto())

        every { validateUsernameUseCase.execute(any()) } returns false
        every { validatePasswordUseCase.execute(any()) } returns true
        coEvery {
            loginUseCase.execute(any(), any())
        } returns Result.Success(resultExpected)

        loginViewModel.onEvent(LoginEventFromUI.SubmitLogin)

        advanceUntilIdle()

        loginViewModel.state.value.usernameError.shouldBeInstanceOf<UiText.StringResource>()
        loginViewModel.state.value.passwordError shouldBe null
    }

    @Test
    fun `Test to submit login - Password error case`() = runTest {
        val resultExpected = AuthResponse("accessToken", UserDto())

        every { validateUsernameUseCase.execute(any()) } returns true
        every { validatePasswordUseCase.execute(any()) } returns false
        coEvery {
            loginUseCase.execute(any(), any())
        } returns Result.Success(resultExpected)

        loginViewModel.onEvent(LoginEventFromUI.SubmitLogin)

        advanceUntilIdle()

        loginViewModel.state.value.passwordError.shouldBeInstanceOf<UiText.StringResource>()
        loginViewModel.state.value.usernameError shouldBe null
    }
}