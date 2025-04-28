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
class GetAccessTokenUseCaseTest {

    private lateinit var useCase: GetAccessTokenUseCase

    @MockK
    private lateinit var datastoreRepository: DatastoreRepository

    @BeforeEach
    fun setUp() {
        useCase = GetAccessTokenUseCase(datastoreRepository = datastoreRepository)
        coEvery { datastoreRepository.getAppSettings() } returns AppSettings(accessToken = "MyAccessToken")
    }

    @Test
    fun `Get access token - Nominal case`() = runTest {
        val accessToken = useCase.execute()
        accessToken shouldBe "MyAccessToken"
    }
}