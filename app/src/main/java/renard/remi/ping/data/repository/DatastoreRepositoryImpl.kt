package renard.remi.ping.data.repository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import renard.remi.ping.domain.repository.DatastoreRepository
import renard.remi.ping.extension.dataStore
import renard.remi.ping.ui.theme.Palette
import javax.inject.Inject

class DatastoreRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : DatastoreRepository {

    override fun observeAppSettings() = context.dataStore.data

    override suspend fun updateAppColors(palette: Palette) {
        context.dataStore.updateData {
            it.copy(palette = palette)
        }
    }

    override suspend fun useDynamicsColors(shouldUse: Boolean) {
        context.dataStore.updateData {
            it.copy(useDynamicColors = shouldUse)
        }
    }

    override suspend fun setDarkMode(isInDarkMode: Boolean) {
        context.dataStore.updateData {
            it.copy(isInDarkMode = isInDarkMode)
        }
    }

    override suspend fun getDynamicsColors() = context.dataStore.data.first().useDynamicColors

    override suspend fun getIsInDarkMode() = context.dataStore.data.first().isInDarkMode

    override suspend fun getPaletteColors() = context.dataStore.data.first().palette

    override suspend fun logout() {
        context.dataStore.updateData {
            it.copy(
                accessToken = null,
                userId = null
            )
        }
    }

    override suspend fun getAccessToken() = context.dataStore.data.first().accessToken

    override suspend fun getCurrentUserId() = context.dataStore.data.first().userId
}