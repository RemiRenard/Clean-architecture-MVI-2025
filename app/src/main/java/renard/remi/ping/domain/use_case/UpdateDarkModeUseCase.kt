package renard.remi.ping.domain.use_case

import renard.remi.ping.domain.repository.DatastoreRepository

data class UpdateDarkModeUseCase(
    private val datastoreRepository: DatastoreRepository
) {
    suspend fun execute(isInDarkMode: Boolean) =
        datastoreRepository.setDarkMode(isInDarkMode = isInDarkMode)
}