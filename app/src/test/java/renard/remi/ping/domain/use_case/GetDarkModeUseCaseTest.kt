package renard.remi.ping.domain.use_case

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import renard.remi.ping.data.datastore.AppSettings
import renard.remi.ping.domain.repository.DatastoreRepository

@ExtendWith(MockKExtension::class)
class GetDarkModeUseCaseTest {

    private lateinit var useCase: GetDarkModeUseCase

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        useCase = GetDarkModeUseCase(datastoreRepository = datastoreRepository)
        coEvery { datastoreRepository.getAppSettings() } returns AppSettings(isInDarkMode = true)
    }

    @Test
    fun `Get dark mode - Nominal case`() = runTest {
        val isInDarkMode = useCase.execute()
        isInDarkMode shouldBe true
    }
}