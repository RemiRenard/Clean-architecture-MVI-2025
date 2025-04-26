package renard.remi.ping.domain.use_case

import renard.remi.ping.domain.repository.DatastoreRepository
import renard.remi.ping.ui.theme.Palette

data class UpdateAppColorsUseCase(
    private val datastoreRepository: DatastoreRepository
) {
    suspend fun execute(palette: Palette) = datastoreRepository.updateAppColors(palette = palette)
}