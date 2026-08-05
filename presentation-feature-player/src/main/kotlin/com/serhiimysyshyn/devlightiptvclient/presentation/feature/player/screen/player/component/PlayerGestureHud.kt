package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import kotlin.math.roundToInt

private const val HUD_BACKGROUND_ALPHA = 0.6f
private val HUD_WIDTH = 140.dp

/** Transient readout of the value a drag is changing; the caller shows it only while dragging. */
@Composable
internal fun PlayerGestureHud(
    state: PlayerGestureState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(HUD_WIDTH)
            .background(
                color = Color.Black.copy(alpha = HUD_BACKGROUND_ALPHA),
                shape = RoundedCornerShape(Theme.radius.m),
            )
            .padding(Theme.spacing.m),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = when (state.gesture) {
                PlayerGesture.BRIGHTNESS -> Icons.Default.LightMode
                PlayerGesture.VOLUME -> Icons.AutoMirrored.Filled.VolumeUp
            },
            contentDescription = null,
            tint = Color.White,
        )

        Text(
            text = "${(state.progress * 100).roundToInt()}%",
            style = Theme.typography.body,
            color = Color.White,
            modifier = Modifier.padding(vertical = Theme.spacing.xs),
        )

        LinearProgressIndicator(
            progress = { state.progress },
            color = Color.White,
            trackColor = Color.White.copy(alpha = HUD_BACKGROUND_ALPHA),
        )
    }
}
