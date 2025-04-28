package renard.remi.ping.domain.use_case

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import renard.remi.ping.domain.repository.DatastoreRepository

@ExtendWith(MockKExtension::class)
class LogoutUseCaseTest {

    private lateinit var useCase: LogoutUseCase

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        useCase = LogoutUseCase(datastoreRepository = datastoreRepository)
        coEvery { datastoreRepository.logout() } returns Unit
    }

    @Test
    fun `Logout - Nominal case`() = runTest {
        useCase.execute()
        coVerify(exactly = 1) { datastoreRepository.logout() }
    }
}