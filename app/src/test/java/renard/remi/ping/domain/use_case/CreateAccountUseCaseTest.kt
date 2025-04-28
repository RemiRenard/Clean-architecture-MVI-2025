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
class CreateAccountUseCaseTest {

    private lateinit var createAccountUseCase: CreateAccountUseCase

    @MockK
    private lateinit var authRepository: AuthRepository

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        createAccountUseCase = CreateAccountUseCase(
            authRepository = authRepository,
            datastoreRepository = datastoreRepository
        )
    }

    @Test
    fun `Create an account - Nominal case`() = runTest {
        val resultExpected = Result.Success<AuthResult, DataError.Network>(
            AuthResult(
                accessToken = "accessToken",
                user = User()
            )
        )

        coEvery { authRepository.createAccount(any(), any()) } returns resultExpected
        coEvery { datastoreRepository.updateLocalUser(any(), any()) } returns Unit

        val useCaseResult = createAccountUseCase.execute("username", "password")

        useCaseResult shouldBe resultExpected
    }

    @Test
    fun `Create an account - Error case Error Network`() = runTest {
        val errorExpected =
            Result.Error<AuthResult, DataError.Network>(DataError.Network.NO_INTERNET)

        coEvery { authRepository.createAccount(any(), any()) } returns errorExpected

        val useCaseResult = createAccountUseCase.execute("username", "password")

        useCaseResult shouldBe errorExpected
    }

    @Test
    fun `Create an account - Error case throws Exception`() = runTest {
        coEvery { authRepository.login(any(), any()) } throws Exception()

        shouldThrow<Exception> {
            createAccountUseCase.execute("username", "password")
        }
    }
}