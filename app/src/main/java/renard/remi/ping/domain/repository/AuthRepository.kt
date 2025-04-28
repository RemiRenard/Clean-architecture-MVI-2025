package renard.remi.ping.domain.repository

import renard.remi.ping.domain.model.AuthResult
import renard.remi.ping.domain.model.DataError
import renard.remi.ping.domain.model.Result

interface AuthRepository {

    suspend fun login(
        username: String,
        password: String
    ): Result<AuthResult, DataError.Network>

    suspend fun createAccount(
        username: String,
        password: String
    ): Result<AuthResult, DataError.Network>
}