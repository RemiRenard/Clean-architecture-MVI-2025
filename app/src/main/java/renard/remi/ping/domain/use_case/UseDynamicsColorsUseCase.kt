package renard.remi.ping.domain.use_case

import renard.remi.ping.domain.repository.DatastoreRepository

data class UseDynamicsColorsUseCase(
    private val datastoreRepository: DatastoreRepository
) {
    suspend fun execute(shouldUseDynamicsColors: Boolean) =
        datastoreRepository.useDynamicsColors(shouldUse = shouldUseDynamicsColors)
}