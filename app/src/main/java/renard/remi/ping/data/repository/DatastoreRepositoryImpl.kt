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

    override suspend fun getAppSettings() = context.dataStore.data.first()

    override suspend fun updateLocalUser(accessToken: String?, userId: String?) {
        context.dataStore.updateData {
            it.copy(
                accessToken = accessToken,
                userId = userId
            )
        }
    }

    override suspend fun logout() {
        context.dataStore.updateData {
            it.copy(
                accessToken = null,
                userId = null
            )
        }
    }

    override suspend fun updateAppColors(palette: Palette) {
        context.dataStore.updateData {
            it.copy(palette = palette)
        }
    }

    override suspend fun updateLanguage(language: String) {
        context.dataStore.updateData {
            it.copy(currentLanguage = language)
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
}