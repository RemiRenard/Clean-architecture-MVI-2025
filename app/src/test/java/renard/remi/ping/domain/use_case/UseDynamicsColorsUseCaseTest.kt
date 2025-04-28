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
class UseDynamicsColorsUseCaseTest {

    private lateinit var useCase: UseDynamicsColorsUseCase

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        useCase = UseDynamicsColorsUseCase(datastoreRepository = datastoreRepository)
        coEvery { datastoreRepository.useDynamicsColors(any()) } returns Unit
    }

    @Test
    fun `Use dynamics colors - Nominal case`() = runTest {
        useCase.execute(shouldUseDynamicsColors = true)
        coVerify(exactly = 1) { datastoreRepository.useDynamicsColors(true) }

        useCase.execute(shouldUseDynamicsColors = false)
        coVerify(exactly = 1) { datastoreRepository.useDynamicsColors(false) }
    }
}