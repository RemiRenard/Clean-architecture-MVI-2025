package renard.remi.ping.data.repository

import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import renard.remi.ping.data.db.dao.UserDao
import renard.remi.ping.data.network.ApiService
import renard.remi.ping.data.network.dto.UserDto
import renard.remi.ping.data.network.dto.response.AuthResponse
import renard.remi.ping.data.network.toDomain
import renard.remi.ping.domain.model.DataError
import renard.remi.ping.domain.model.Result
import retrofit2.HttpException
import retrofit2.Response

@ExtendWith(MockKExtension::class)
class AuthRepositoryImplTest {

    @InjectMockKs
    private lateinit var authRepository: AuthRepositoryImpl

    @MockK
    private lateinit var apiService: ApiService

    @MockK
    private lateinit var userDao: UserDao

    @BeforeEach
    fun setUp() {
        authRepository = AuthRepositoryImpl(apiService, userDao)
    }

    @Test
    fun `Login - Nominal case`() = runTest {
        val expectedAuthResponse =
            AuthResponse(accessToken = "accessToken", user = UserDto(id = "1"))
        coEvery { apiService.login(any()) } returns expectedAuthResponse
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.login("username", "password")

        actualResult shouldBe Result.Success(expectedAuthResponse.toDomain())
    }

    @Test
    fun `Login - Error case HttpException 500`() = runTest {
        coEvery { apiService.login(any()) } throws HttpException(
            Response.error<Unit>(
                500,
                "".toResponseBody()
            )
        )
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.login("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.SERVER_ERROR)
    }

    @Test
    fun `Login - Error case HttpException 400`() = runTest {
        coEvery { apiService.login(any()) } throws HttpException(
            Response.error<Unit>(
                400,
                "".toResponseBody()
            )
        )
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.login("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.BAD_REQUEST)
    }

    @Test
    fun `Login - Error case HttpException unknown`() = runTest {
        coEvery { apiService.login(any()) } throws HttpException(
            Response.error<Unit>(
                508,
                "".toResponseBody()
            )
        )
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.login("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.UNKNOWN)
    }

    @Test
    fun `Login - Error case Exception`() = runTest {
        coEvery { apiService.login(any()) } throws Exception()
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.login("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.NO_INTERNET)
    }

    @Test
    fun `Create an account - Nominal case`() = runTest {
        val expectedAuthResponse =
            AuthResponse(accessToken = "accessToken", user = UserDto(id = "1"))
        coEvery { apiService.createAccount(any()) } returns expectedAuthResponse
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.createAccount("username", "password")

        actualResult shouldBe Result.Success(expectedAuthResponse.toDomain())
    }

    @Test
    fun `Create an account - Error case HttpException 400`() = runTest {
        coEvery { apiService.createAccount(any()) } throws HttpException(
            Response.error<Unit>(
                400,
                "".toResponseBody()
            )
        )
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.createAccount("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.BAD_REQUEST)
    }

    @Test
    fun `Create an account - Error case HttpException 409`() = runTest {
        coEvery { apiService.createAccount(any()) } throws HttpException(
            Response.error<Unit>(
                409,
                "".toResponseBody()
            )
        )
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.createAccount("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.CONFLICT)
    }

    @Test
    fun `Create an account - Error case HttpException 422`() = runTest {
        coEvery { apiService.createAccount(any()) } throws HttpException(
            Response.error<Unit>(
                422,
                "".toResponseBody()
            )
        )
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.createAccount("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.UNPROCESSABLE_ENTITY)
    }

    @Test
    fun `Create an account - Error case HttpException 500`() = runTest {
        coEvery { apiService.createAccount(any()) } throws HttpException(
            Response.error<Unit>(
                500,
                "".toResponseBody()
            )
        )
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.createAccount("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.SERVER_ERROR)
    }

    @Test
    fun `Create an account - Error case HttpException 503`() = runTest {
        coEvery { apiService.createAccount(any()) } throws HttpException(
            Response.error<Unit>(
                503,
                "".toResponseBody()
            )
        )
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.createAccount("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.UNAVAILABLE_SERVICE)
    }

    @Test
    fun `Create an account - Error case HttpException unknown`() = runTest {
        coEvery { apiService.createAccount(any()) } throws HttpException(
            Response.error<Unit>(
                508,
                "".toResponseBody()
            )
        )
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.createAccount("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.UNKNOWN)
    }

    @Test
    fun `Create an account - Error case Exception`() = runTest {
        coEvery { apiService.createAccount(any()) } throws Exception()
        coEvery { userDao.insertUser(any()) } returns Unit

        val actualResult = authRepository.createAccount("username", "password")

        actualResult shouldBe Result.Error(DataError.Network.NO_INTERNET)
    }
}