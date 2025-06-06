package renard.remi.ping.ui.settings

import android.architecture.app.R
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import renard.remi.ping.domain.model.BaseRoute
import renard.remi.ping.ui.component.AppButton
import renard.remi.ping.ui.settings.component.AppExposedDropdownMenuBox
import renard.remi.ping.ui.settings.component.CardSettingsSwitch
import renard.remi.ping.ui.theme.Palette
import renard.remi.ping.ui.theme.AppTheme
import renard.remi.ping.ui.theme.getPrimaryColorByPalette

@Serializable
object SettingsScreenRoute : BaseRoute

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    state: SettingsState,
    onEvent: (SettingsEventFromUI) -> Unit = {},
    events: Flow<SettingsEventFromVm>? = null
) {
    val activity = LocalActivity.current

    LaunchedEffect(Lifecycle.Event.ON_CREATE) {
        events?.collect { event ->
            when (event) {
                is SettingsEventFromVm.OnLanguageChanged -> {
                    activity?.recreate()
                }
            }
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.settings_title),
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.size(50.dp))
        CardSettingsSwitch(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            isChecked = state.isInDarkMode,
            isForDarkMode = true,
            title = stringResource(
                if (state.isInDarkMode) {
                    R.string.settings_color_switch_dark_mode
                } else {
                    R.string.settings_color_switch_light_mode
                }
            ),
            onCheckedChanged = {
                onEvent(SettingsEventFromUI.ShouldUseDarkMode(it))
            }
        )
        Spacer(modifier = Modifier.size(12.dp))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            CardSettingsSwitch(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                isChecked = state.useDynamicsColors,
                title = stringResource(R.string.settings_color_system),
                onCheckedChanged = {
                    onEvent(SettingsEventFromUI.UseDynamicsColors(it))
                }
            )
            Spacer(modifier = Modifier.size(12.dp))
        }
        AnimatedVisibility(visible = !state.useDynamicsColors) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .alpha(0.8F)
                ) {
                    Spacer(modifier = Modifier.size(12.dp))
                    Text(
                        text = stringResource(R.string.settings_customize_colors),
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 8.dp),
                        columns = GridCells.Fixed(6)
                    ) {
                        items(Palette.entries.toTypedArray()) {
                            Card(
                                modifier = Modifier
                                    .padding(all = 8.dp)
                                    .clip(CircleShape)
                                    .clickable {
                                        onEvent(SettingsEventFromUI.OnPaletteChanged(it))
                                    }
                                    .aspectRatio(1F),
                                colors = CardDefaults.cardColors(
                                    containerColor = getPrimaryColorByPalette(
                                        isDarkTheme = isSystemInDarkTheme(),
                                        palette = it
                                    )
                                )
                            ) {
                                if (state.paletteSelected == it ||
                                    (state.paletteSelected == null && it == Palette.DEFAULT)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Check,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(8.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            contentDescription = "selected",
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.size(12.dp))
            }
        }
        AppExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            label = stringResource(R.string.settings_language_label),
            data = state.languagesAvailable,
            typeToString = { it ?: "" },
            onDataChanged = { language ->
                language?.let {
                    onEvent(SettingsEventFromUI.OnLanguageChanged(it))
                }
            }
        )
        Spacer(modifier = Modifier.size(12.dp))
        AppButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            onClick = {
                onEvent(SettingsEventFromUI.Logout)
            },
            isLoading = state.isLoading,
            isEnabled = !state.isLoading,
            textBtn = stringResource(R.string.settings_logout)
        )
    }
}

@PreviewScreenSizes
@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreen(
            state = SettingsState()
        )
    }
}