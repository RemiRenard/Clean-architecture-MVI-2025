package renard.remi.ping.ui.login

import io.kotest.matchers.shouldBe
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
import renard.remi.ping.domain.use_case.LoginUseCase
import renard.remi.ping.domain.use_case.ValidatePasswordUseCase
import renard.remi.ping.domain.use_case.ValidateUsernameUseCase

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
    fun `Check initial state`() = runTest {
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
}