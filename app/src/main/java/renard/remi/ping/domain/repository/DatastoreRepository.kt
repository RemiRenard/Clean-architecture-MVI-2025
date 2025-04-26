package renard.remi.ping.domain.repository

import renard.remi.ping.ui.theme.Palette

interface DatastoreRepository {

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