package renard.remi.ping.domain.use_case

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import renard.remi.ping.data.datastore.AppSettings
import renard.remi.ping.domain.repository.DatastoreRepository

@ExtendWith(MockKExtension::class)
class ObserveAppSettingsUseCaseTest {

    private lateinit var useCase: ObserveAppSettingsUseCase

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        useCase = ObserveAppSettingsUseCase(datastoreRepository = datastoreRepository)
        coEvery { datastoreRepository.observeAppSettings() } returns flowExpected
    }

    @Test
    fun `Get app settings as flow - Nominal case`() = runTest {
        useCase.execute() shouldBe flowExpected
    }

    companion object {
        val flowExpected = flowOf(AppSettings())
    }
}