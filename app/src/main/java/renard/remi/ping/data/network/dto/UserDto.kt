package renard.remi.ping.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: String? = null,
    val username: String? = null,
    val refreshToken: String? = null,
)