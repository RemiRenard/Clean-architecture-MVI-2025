package renard.remi.ping.domain.use_case

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import renard.remi.ping.data.network.dto.UserDto
import renard.remi.ping.data.network.dto.response.AuthResponse
import renard.remi.ping.domain.model.DataError
import renard.remi.ping.domain.model.Result
import renard.remi.ping.domain.repository.AuthRepository

@ExtendWith(MockKExtension::class)
class LoginUseCaseTest {

    @InjectMockKs
    private lateinit var loginUseCase: LoginUseCase

    @MockK
    private lateinit var loginRepository: AuthRepository

    @BeforeEach
    fun setUp() {
        loginUseCase = LoginUseCase(loginRepository)
    }

    @Test
    fun `Login - Nominal case`() = runTest {
        val resultExpected = Result.Success<AuthResponse, DataError.Network>(
            AuthResponse(
                accessToken = "accessToken",
                user = UserDto()
            )
        )

        coEvery { loginRepository.login(any(), any()) } returns resultExpected

        val useCaseResult = loginUseCase.execute("username", "password")

        useCaseResult shouldBe resultExpected
    }

    @Test
    fun `Login - Error case Error Network`() = runTest {
        val errorExpected =
            Result.Error<AuthResponse, DataError.Network>(DataError.Network.NO_INTERNET)

        coEvery { loginRepository.login(any(), any()) } returns errorExpected

        val useCaseResult = loginUseCase.execute("username", "password")

        useCaseResult shouldBe errorExpected
    }

    @Test
    fun `Login - Error case throws Exception`() = runTest {
        coEvery { loginRepository.login(any(), any()) } throws Exception()

        shouldThrow<Exception> {
            loginUseCase.execute("username", "password")
        }
    }
}