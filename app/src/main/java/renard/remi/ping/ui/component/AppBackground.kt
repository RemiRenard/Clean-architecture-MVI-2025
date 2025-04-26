package renard.remi.ping.ui.component

import android.architecture.app.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun AppBackground(isVisible: Boolean) {
    AnimatedVisibility(
        isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .blur(36.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Image(
                painter = painterResource(R.drawable.bg),
                modifier = Modifier,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }
    }
}