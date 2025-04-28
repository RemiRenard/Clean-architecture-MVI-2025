package renard.remi.ping.data.network

import renard.remi.ping.data.network.dto.UserDto
import renard.remi.ping.data.network.dto.response.AuthResponse
import renard.remi.ping.domain.model.AuthResult
import renard.remi.ping.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        username = username,
        refreshToken = refreshToken
    )
}

fun AuthResponse.toDomain(): AuthResult {
    return AuthResult(
        accessToken = accessToken,
        user = user.toDomain()
    )
}