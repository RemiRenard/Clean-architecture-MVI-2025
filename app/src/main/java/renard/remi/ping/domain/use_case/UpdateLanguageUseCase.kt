package renard.remi.ping.domain.use_case

import renard.remi.ping.domain.repository.DatastoreRepository

data class UpdateLanguageUseCase(
    private val datastoreRepository: DatastoreRepository
) {
    suspend fun execute(language: String) = datastoreRepository.updateLanguage(language = language)
}