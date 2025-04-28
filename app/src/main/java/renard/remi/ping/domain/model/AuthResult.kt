package renard.remi.ping.domain.model

data class AuthResult(
    val accessToken: String? = null,
    val user: User
)
