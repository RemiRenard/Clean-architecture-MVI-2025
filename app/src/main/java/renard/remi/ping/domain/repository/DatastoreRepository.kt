package renard.remi.ping.domain.repository

import kotlinx.coroutines.flow.Flow
import renard.remi.ping.data.datastore.AppSettings
import renard.remi.ping.ui.theme.Palette

interface DatastoreRepository {

    fun observeAppSettings(): Flow<AppSettings>

    suspend fun getAppSettings(): AppSettings

    suspend fun updateLocalUser(accessToken: String?, userId: String?)

    suspend fun logout()

    suspend fun updateAppColors(palette: Palette)

    suspend fun updateLanguage(language: String)

    suspend fun useDynamicsColors(shouldUse: Boolean)

    suspend fun setDarkMode(isInDarkMode: Boolean)
}