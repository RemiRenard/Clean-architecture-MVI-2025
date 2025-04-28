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
class UpdateDarkModeUseCaseTest {

    private lateinit var useCase: UpdateDarkModeUseCase

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        useCase = UpdateDarkModeUseCase(datastoreRepository = datastoreRepository)
        coEvery { datastoreRepository.setDarkMode(any()) } returns Unit
    }

    @Test
    fun `Set dark mode - Nominal case`() = runTest {
        useCase.execute(true)
        coVerify(exactly = 1) { datastoreRepository.setDarkMode(true) }

        useCase.execute(false)
        coVerify(exactly = 1) { datastoreRepository.setDarkMode(false) }
    }
}