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
import renard.remi.ping.ui.theme.Palette

@ExtendWith(MockKExtension::class)
class GetPaletteColorsUseCaseTest {

    private lateinit var useCase: GetPaletteColorsUseCase

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        useCase = GetPaletteColorsUseCase(datastoreRepository = datastoreRepository)
        coEvery { datastoreRepository.getAppSettings() } returns AppSettings(palette = Palette.RED)
    }

    @Test
    fun `Get palette colors - Nominal case`() = runTest {
        val palette = useCase.execute()
        palette shouldBe Palette.RED
    }
}