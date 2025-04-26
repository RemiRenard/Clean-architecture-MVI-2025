package renard.remi.ping.domain.use_case

import renard.remi.ping.domain.repository.DatastoreRepository

data class ObserveAppSettingsUseCase(
    private val datastoreRepository: DatastoreRepository
) {
    fun execute() = datastoreRepository.observeAppSettings()
}