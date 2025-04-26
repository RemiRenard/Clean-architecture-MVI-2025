package renard.remi.ping.domain.repository

import kotlinx.coroutines.flow.Flow
import renard.remi.ping.data.datastore.AppSettings
import renard.remi.ping.ui.theme.Palette

interface DatastoreRepository {

    fun observeAppSettings(): Flow<AppSettings>

    suspend fun updateAppColors(palette: Palette)

    suspend fun useDynamicsColors(shouldUse: Boolean)

    suspend fun setDarkMode(isInDarkMode: Boolean)

    suspend fun getDynamicsColors(): Boolean

    suspend fun getIsInDarkMode(): Boolean

    suspend fun getPaletteColors(): Palette?

    suspend fun logout()

    suspend fun getAccessToken(): String?

    suspend fun getCurrentUserId(): String?
}