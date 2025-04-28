package renard.remi.ping.domain.use_case

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import renard.remi.ping.domain.model.Result
import renard.remi.ping.domain.repository.UserRepository

@ExtendWith(MockKExtension::class)
class UpdateFcmTokenUseCaseTest {

    private lateinit var useCase: UpdateFcmTokenUseCase

    @MockK
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        useCase = UpdateFcmTokenUseCase(userRepository = userRepository)
        coEvery { userRepository.updateRemoteFcmToken(any()) } returns Result.Success(Any())
    }

    @Test
    fun `Update FCM token - Nominal case`() = runTest {
        useCase.execute()
        coVerify(exactly = 1) { userRepository.updateRemoteFcmToken(null) }

        useCase.execute("anotherToken")
        coVerify(exactly = 1) { userRepository.updateRemoteFcmToken("anotherToken") }
    }
}