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
class UpdateLanguageUseCaseTest {

    private lateinit var useCase: UpdateLanguageUseCase

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        useCase = UpdateLanguageUseCase(datastoreRepository = datastoreRepository)
        coEvery { datastoreRepository.updateLanguage(any()) } returns Unit
    }

    @Test
    fun `Update language - Nominal case`() = runTest {
        useCase.execute("French")
        coVerify(exactly = 1) { datastoreRepository.updateLanguage("French") }

        useCase.execute("English")
        coVerify(exactly = 1) { datastoreRepository.updateLanguage("English") }
    }
}