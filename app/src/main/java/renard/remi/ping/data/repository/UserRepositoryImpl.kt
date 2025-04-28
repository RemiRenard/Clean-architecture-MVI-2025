package renard.remi.ping.data.repository

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import renard.remi.ping.data.db.dao.UserDao
import renard.remi.ping.data.db.dbo.UserDbo
import renard.remi.ping.data.db.toDomain
import renard.remi.ping.data.network.ApiService
import renard.remi.ping.data.network.dto.request.UpdateFcmTokenRequest
import renard.remi.ping.data.network.toDomain
import renard.remi.ping.domain.model.DataError
import renard.remi.ping.domain.model.Result
import renard.remi.ping.domain.model.User
import renard.remi.ping.domain.repository.DatastoreRepository
import renard.remi.ping.domain.repository.UserRepository
import retrofit2.HttpException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val datastoreRepository: DatastoreRepository,
    private val apiService: ApiService,
    private val userDao: UserDao,
) : UserRepository {

    override suspend fun getMe(forceRefresh: Boolean): Result<User?, DataError> {
        return if (forceRefresh) {
            getRemoteUser()
        } else {
            val currentUser =
                getLocalUserById(userId = datastoreRepository.getCurrentUserId() ?: "")
            return if (currentUser is Result.Success && currentUser.data != null) {
                Result.Success(currentUser.data.toDomain())
            } else if (currentUser is Result.Error) {
                Result.Error(DataError.Local.REQUEST_ERROR)
            } else {
                getRemoteUser()
            }
        }
    }

    override suspend fun updateRemoteFcmToken(fcmToken: String?): Result<Any, DataError.Network> {
        return try {
            Result.Success(
                apiService.updateFcmToken(
                    accessToken = "Bearer " + datastoreRepository.getAccessToken(),
                    updateFcmTokenRequest = UpdateFcmTokenRequest(
                        fcmToken = fcmToken ?: Firebase.messaging.token.await()
                    )
                )
            )
        } catch (error: Exception) {
            if (error is HttpException) {
                when (error.code()) {
                    401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                    500 -> Result.Error(DataError.Network.SERVER_ERROR)
                    else -> Result.Error(DataError.Network.UNKNOWN)
                }
            } else {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }
    }

    private suspend fun getRemoteUser(): Result<User?, DataError.Network> {
        return try {
            val user = apiService.getMe(
                accessToken = "Bearer " + datastoreRepository.getAccessToken()
            )

            user?.let {
                userDao.insertUser(
                    UserDbo(
                        remoteId = it.id ?: "",
                        username = it.username ?: "",
                        refreshToken = it.refreshToken ?: ""
                    )
                )
            }
            Result.Success(user?.toDomain())
        } catch (error: Exception) {
            if (error is HttpException) {
                when (error.code()) {
                    401 -> Result.Error(DataError.Network.UNAUTHORIZED)
                    500 -> Result.Error(DataError.Network.SERVER_ERROR)
                    else -> Result.Error(DataError.Network.UNKNOWN)
                }
            } else {
                Result.Error(DataError.Network.NO_INTERNET)
            }
        }
    }

    private suspend fun getLocalUserById(userId: String): Result<UserDbo?, DataError.Local> {
        return try {
            val user = userDao.findUserById(userId = userId).firstOrNull()
                ?.firstOrNull()
            Result.Success(user)
        } catch (error: Exception) {
            Result.Error(DataError.Local.REQUEST_ERROR)
        }
    }
}