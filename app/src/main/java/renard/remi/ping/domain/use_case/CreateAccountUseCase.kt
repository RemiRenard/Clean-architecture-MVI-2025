package renard.remi.ping.domain.use_case

import renard.remi.ping.domain.model.AuthResult
import renard.remi.ping.domain.model.DataError
import renard.remi.ping.domain.model.Result
import renard.remi.ping.domain.repository.AuthRepository
import renard.remi.ping.domain.repository.DatastoreRepository

data class CreateAccountUseCase(
    private val authRepository: AuthRepository,
    private val datastoreRepository: DatastoreRepository
) {
    suspend fun execute(
        username: String,
        password: String
    ): Result<AuthResult, DataError.Network> {
        val result = authRepository.createAccount(username = username, password = password)
        if (result is Result.Success) {
            datastoreRepository.updateLocalUser(result.data.accessToken, result.data.user.id)
        }
        return result
    }
}