package renard.remi.ping.data.db

import renard.remi.ping.data.db.dbo.UserDbo
import renard.remi.ping.domain.model.User

fun UserDbo.toDomain(): User {
    return User(
        id = remoteId,
        username = username,
        refreshToken = refreshToken
    )
}