package renard.remi.ping.domain.use_case

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import renard.remi.ping.domain.model.AuthResult
import renard.remi.ping.domain.model.DataError
import renard.remi.ping.domain.model.Result
import renard.remi.ping.domain.model.User
import renard.remi.ping.domain.repository.AuthRepository
import renard.remi.ping.domain.repository.DatastoreRepository

@ExtendWith(MockKExtension::class)
class LoginUseCaseTest {

    private lateinit var loginUseCase: LoginUseCase

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        loginUseCase = LoginUseCase(
            authRepository = authRepository,
            datastoreRepository = datastoreRepository
        )
    }

    @Test
    fun `Login - Nominal case`() = runTest {
        val resultExpected = Result.Success<AuthResult, DataError.Network>(
            AuthResult(
                accessToken = "accessToken",
                user = User()
            )
        )

        coEvery { authRepository.login(any(), any()) } returns resultExpected
        coEvery { datastoreRepository.updateLocalUser(any(), any()) } returns Unit

        val useCaseResult = loginUseCase.execute("username", "password")

        useCaseResult shouldBe resultExpected
    }

    @Test
    fun `Login - Error case Error Network`() = runTest {
        val errorExpected =
            Result.Error<AuthResult, DataError.Network>(DataError.Network.NO_INTERNET)

        coEvery { authRepository.login(any(), any()) } returns errorExpected

        val useCaseResult = loginUseCase.execute("username", "password")

        useCaseResult shouldBe errorExpected
    }

    @Test
    fun `Login - Error case throws Exception`() = runTest {
        coEvery { authRepository.login(any(), any()) } throws Exception()

        shouldThrow<Exception> {
            loginUseCase.execute("username", "password")
        }
    }
}