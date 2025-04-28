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
import renard.remi.ping.domain.model.User
import renard.remi.ping.domain.repository.UserRepository

@ExtendWith(MockKExtension::class)
class GetMeUseCaseTest {

    private lateinit var useCase: GetMeUseCase

    @MockK
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        useCase = GetMeUseCase(userRepository = userRepository)
        coEvery { userRepository.getMe(any()) } returns Result.Success(User())
    }

    @Test
    fun `Get me - Nominal case`() = runTest {
        useCase.execute(false)
        coVerify(exactly = 1) { userRepository.getMe(false) }

        useCase.execute(true)
        coVerify(exactly = 1) { userRepository.getMe(true) }
    }
}