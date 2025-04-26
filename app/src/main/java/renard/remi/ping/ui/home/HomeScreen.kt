package renard.remi.ping.ui.home

import android.Manifest
import android.architecture.app.R
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import renard.remi.ping.domain.model.BaseRoute
import renard.remi.ping.domain.model.User

@Serializable
object HomeScreenRoute : BaseRoute

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    state: HomeState,
    onEvent: (HomeEventFromUI) -> Unit = {},
    events: Flow<HomeEventFromVm>? = null
) {
    val context = LocalContext.current

    val launcherNotificationsPerms = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onEvent(HomeEventFromUI.PermissionsPushGranted)
            }
        }
    )

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            launcherNotificationsPerms.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onEvent(HomeEventFromUI.PermissionsPushGranted)
        }

        events?.collect { event ->
            if (event is HomeEventFromVm.Error) {
                Toast.makeText(
                    context,
                    event.errorMessage.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            text = context.getString(R.string.home_title, state.user?.username ?: ""),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        state = HomeState(
            isLoading = false,
            user = User(username = "My Username")
        )
    )
}
