package renard.remi.ping.data.repository

import renard.remi.ping.data.db.dao.UserDao
import renard.remi.ping.data.db.dbo.UserDbo
import renard.remi.ping.data.network.ApiService
import renard.remi.ping.data.network.dto.request.AuthRequest
import renard.remi.ping.data.network.toDomain
import renard.remi.ping.domain.model.AuthResult
import renard.remi.ping.domain.model.DataError
import renard.remi.ping.domain.model.Result
import renard.remi.ping.domain.repository.AuthRepository
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Result<AuthResult, DataError.Network> {
        return try {
            val authResponse =
                apiService.login(AuthRequest(username = username, password = password))
            userDao.insertUser(
                UserDbo(
                    remoteId = authResponse.user.id ?: "",
                    username = authResponse.user.username ?: "",
                    refreshToken = authResponse.user.refreshToken ?: ""
                )
            )
            Result.Success(authResponse.toDomain())
        } catch (error: Exception) {
            if (error is HttpException) {
                when (error.code()) {
                    400 -> Result.Error(DataError.Network.BAD_REQUEST)
                    500 -> Result.Error(DataError.Network.SERVER_ERROR)
                    else -> Result.Error(DataError.Network.UNKNOWN)
                }
            } else {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }
    }

    override suspend fun createAccount(
        username: String,
        password: String
    ): Result<AuthResult, DataError.Network> {
        return try {
            val authResponse = apiService.createAccount(
                authRequest = AuthRequest(
                    username = username,
                    password = password
                )
            )
            userDao.insertUser(
                UserDbo(
                    remoteId = authResponse.user.id ?: "",
                    username = authResponse.user.username ?: "",
                    refreshToken = authResponse.user.refreshToken ?: ""
                )
            )
            Result.Success(authResponse.toDomain())
        } catch (error: Exception) {
            if (error is HttpException) {
                when (error.code()) {
                    400 -> Result.Error(DataError.Network.BAD_REQUEST)
                    409 -> Result.Error(DataError.Network.CONFLICT)
                    422 -> Result.Error(DataError.Network.UNPROCESSABLE_ENTITY)
                    500 -> Result.Error(DataError.Network.SERVER_ERROR)
                    503 -> Result.Error(DataError.Network.UNAVAILABLE_SERVICE)
                    else -> Result.Error(DataError.Network.UNKNOWN)
                }
            } else {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }
    }
}