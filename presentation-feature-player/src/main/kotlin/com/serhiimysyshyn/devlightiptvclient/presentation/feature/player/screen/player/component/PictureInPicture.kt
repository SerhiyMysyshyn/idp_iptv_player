package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import android.util.Rational
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/** Widest and narrowest aspect ratios Android accepts for a PiP window. */
private val MIN_ASPECT_RATIO = Rational(100, 239)
private val MAX_ASPECT_RATIO = Rational(239, 100)

/**
 * True while the hosting activity is in Picture-in-Picture.
 *
 * The PiP window is just a small activity window, so without this the app bar, buttons and
 * favourites list would all try to render inside it.
 */
@Composable
internal fun isInPictureInPictureMode(): Boolean {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivityOrNull() } ?: return false
    val lifecycleOwner = LocalLifecycleOwner.current
    var inPipMode by remember { mutableStateOf(activity.isInPictureInPictureMode) }

    DisposableEffect(lifecycleOwner) {
        // Entering and leaving PiP both surface as lifecycle events on the activity, so re-read
        // the flag on each one rather than polling.
        val observer = LifecycleEventObserver { _, _ ->
            inPipMode = activity.isInPictureInPictureMode
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    return inPipMode
}

/**
 * Enters Picture-in-Picture when the user leaves the app while a stream is playing.
 *
 * `ON_PAUSE` stands in for `onUserLeaveHint`: a Composable can't override the activity callback,
 * and pause fires on the same home/recents gesture. The [enabled] guard is what keeps it from
 * triggering on an in-app navigation away from the player.
 */
@Composable
internal fun PictureInPictureEffect(
    enabled: Boolean,
    videoAspectRatio: Rational?,
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivityOrNull() }
    val lifecycleOwner = LocalLifecycleOwner.current

    if (activity == null || !activity.supportsPictureInPicture()) return

    DisposableEffect(lifecycleOwner, enabled, videoAspectRatio) {
        val observer = LifecycleEventObserver { _, event ->
            if (event != Lifecycle.Event.ON_PAUSE || !enabled) return@LifecycleEventObserver
            if (activity.isInPictureInPictureMode) return@LifecycleEventObserver

            val params = PictureInPictureParams.Builder()
                .apply { videoAspectRatio?.clampToPipRange()?.let(::setAspectRatio) }
                .build()

            // The system rejects the request when PiP is disabled device-wide or the activity is
            // already finishing; playback simply continues in the background in that case.
            runCatching { activity.enterPictureInPictureMode(params) }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

/** Android throws if the ratio falls outside its supported band, so clamp instead of trusting it. */
private fun Rational.clampToPipRange(): Rational? {
    if (numerator <= 0 || denominator <= 0) return null

    return when {
        toFloat() < MIN_ASPECT_RATIO.toFloat() -> MIN_ASPECT_RATIO
        toFloat() > MAX_ASPECT_RATIO.toFloat() -> MAX_ASPECT_RATIO
        else -> this
    }
}

private fun Activity.supportsPictureInPicture(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
        packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)

private tailrec fun Context.findActivityOrNull(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivityOrNull()
    else -> null
}
