package renard.remi.ping.data.network.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val username: String? = null,
    val password: String? = null
)