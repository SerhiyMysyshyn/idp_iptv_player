package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.media.AudioManager
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.content.getSystemService
import kotlin.math.roundToInt

/** What a vertical drag is currently adjusting. */
internal enum class PlayerGesture {
    BRIGHTNESS,
    VOLUME,
}

/** Live value of the gesture in progress, `null` when no drag is active. */
internal data class PlayerGestureState(
    val gesture: PlayerGesture,
    /** 0f..1f, ready to render as a percentage. */
    val progress: Float,
)

/**
 * A full-screen drag distance maps to the full range of the property, so the same swipe feels the
 * same on any screen height.
 */
private const val DRAG_RANGE_FRACTION = 0.7f

/**
 * VLC-style vertical drags: left half controls screen brightness, right half controls media
 * volume.
 *
 * Brightness is set on the window (not the system setting) so it reverts automatically when the
 * activity goes away — the player has no business permanently changing device brightness.
 *
 * @param onGestureChange receives the live value while dragging and `null` when the drag ends,
 *   which the caller uses to show and hide the HUD.
 */
@Composable
internal fun rememberPlayerGestureModifier(
    enabled: Boolean,
    onGestureChange: (PlayerGestureState?) -> Unit,
): Modifier {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivityOrNull() }
    val audioManager = remember(context) { context.getSystemService<AudioManager>() }
    val screenHeightPx = with(LocalDensity.current) {
        LocalConfiguration.current.screenHeightDp.dp.toPx()
    }

    if (!enabled || activity == null || audioManager == null) return Modifier

    val maxVolume = remember(audioManager) {
        audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }

    return Modifier.pointerInput(enabled, screenHeightPx) {
        var gesture: PlayerGesture? = null
        var value = 0f

        detectVerticalDragGestures(
            onDragStart = { offset ->
                gesture = if (offset.x < size.width / 2) {
                    PlayerGesture.BRIGHTNESS
                } else {
                    PlayerGesture.VOLUME
                }

                value = when (gesture) {
                    PlayerGesture.BRIGHTNESS -> activity.currentBrightness()
                    else -> audioManager.currentVolumeFraction(maxVolume)
                }
            },
            onDragEnd = {
                gesture = null
                onGestureChange(null)
            },
            onDragCancel = {
                gesture = null
                onGestureChange(null)
            },
        ) { change, dragAmount ->
            val activeGesture = gesture ?: return@detectVerticalDragGestures
            change.consume()

            // Dragging up must increase the value, hence the negated delta.
            val delta = -dragAmount / (screenHeightPx * DRAG_RANGE_FRACTION)
            value = (value + delta).coerceIn(0f, 1f)

            when (activeGesture) {
                PlayerGesture.BRIGHTNESS -> activity.setBrightness(value)
                PlayerGesture.VOLUME -> audioManager.setVolumeFraction(value, maxVolume)
            }

            onGestureChange(PlayerGestureState(activeGesture, value))
        }
    }
}

private fun Activity.currentBrightness(): Float {
    val current = window.attributes.screenBrightness

    // BRIGHTNESS_OVERRIDE_NONE (-1f) means "follow the system"; start such a drag from mid-scale
    // rather than snapping the screen to black.
    return if (current < 0f) DEFAULT_BRIGHTNESS else current.coerceIn(0f, 1f)
}

private fun Activity.setBrightness(value: Float) {
    window.attributes = window.attributes.apply {
        // A literal 0f can render the screen unreadable on some devices, so keep a floor.
        screenBrightness = value.coerceIn(MIN_BRIGHTNESS, 1f)
    }
}

private fun AudioManager.currentVolumeFraction(maxVolume: Int): Float =
    if (maxVolume <= 0) 0f else getStreamVolume(AudioManager.STREAM_MUSIC).toFloat() / maxVolume

private fun AudioManager.setVolumeFraction(value: Float, maxVolume: Int) {
    setStreamVolume(AudioManager.STREAM_MUSIC, (value * maxVolume).roundToInt(), 0)
}

private tailrec fun Context.findActivityOrNull(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivityOrNull()
    else -> null
}

private const val DEFAULT_BRIGHTNESS = 0.5f
private const val MIN_BRIGHTNESS = 0.01f
