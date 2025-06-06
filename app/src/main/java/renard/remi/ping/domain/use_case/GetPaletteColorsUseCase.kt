package renard.remi.ping.domain.use_case

import renard.remi.ping.domain.repository.DatastoreRepository

data class GetPaletteColorsUseCase(
    private val datastoreRepository: DatastoreRepository
) {
    suspend fun execute() = datastoreRepository.getAppSettings().palette
}