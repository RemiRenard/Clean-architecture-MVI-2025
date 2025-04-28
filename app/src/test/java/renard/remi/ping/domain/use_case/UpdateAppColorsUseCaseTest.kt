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
import renard.remi.ping.ui.theme.Palette

@ExtendWith(MockKExtension::class)
class UpdateAppColorsUseCaseTest {

    private lateinit var useCase: UpdateAppColorsUseCase

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        useCase = UpdateAppColorsUseCase(datastoreRepository = datastoreRepository)
        coEvery { datastoreRepository.updateAppColors(any()) } returns Unit
    }

    @Test
    fun `Update app colors - Nominal case`() = runTest {
        useCase.execute(Palette.DEFAULT)
        coVerify(exactly = 1) { datastoreRepository.updateAppColors(Palette.DEFAULT) }

        useCase.execute(Palette.VIOLET)
        coVerify(exactly = 1) { datastoreRepository.updateAppColors(Palette.VIOLET) }

        useCase.execute(Palette.RED)
        coVerify(exactly = 1) { datastoreRepository.updateAppColors(Palette.RED) }
    }
}