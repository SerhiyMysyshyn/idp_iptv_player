package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component

import android.app.Activity
import android.app.PictureInPictureParams
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import android.util.Rational
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.pip.LocalUserLeaveHintOwner

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
 * Driven by `onUserLeaveHint`, which fires only on home/recents. An earlier version listened for
 * `ON_PAUSE` instead — but pause also fires when the player is popped off the back stack, so
 * pressing back entered PiP instead of closing the screen.
 */
@Composable
internal fun PictureInPictureEffect(
    enabled: Boolean,
    videoAspectRatio: Rational?,
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findActivityOrNull() }
    val userLeaveHintOwner = LocalUserLeaveHintOwner.current

    if (activity == null || userLeaveHintOwner == null) return
    // Inline rather than folded into supportsPictureInPicture(): lint only narrows the API level
    // for a version check it can see at the call site.
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    if (!activity.supportsPictureInPicture()) return

    DisposableEffect(userLeaveHintOwner, enabled, videoAspectRatio) {
        val listener: () -> Unit = {
            // isFinishing guards the case where the user hits back and home in quick succession:
            // the screen is already going away, so a PiP window would outlive its own player.
            val canEnter = enabled &&
                !activity.isInPictureInPictureMode &&
                !activity.isFinishing

            if (canEnter) {
                activity.enterPictureInPicture(videoAspectRatio)
            }
        }

        userLeaveHintOwner.addOnUserLeaveHintListener(listener)
        onDispose { userLeaveHintOwner.removeOnUserLeaveHintListener(listener) }
    }
}

/**
 * The API-26 entry point, isolated behind [RequiresApi] so the version check is one lint can
 * follow — an `SDK_INT` test hidden inside a helper predicate is not.
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun Activity.enterPictureInPicture(videoAspectRatio: Rational?) {
    val params = PictureInPictureParams.Builder()
        .apply { videoAspectRatio?.clampToPipRange()?.let(::setAspectRatio) }
        .build()

    // The system rejects the request when PiP is disabled device-wide; playback simply continues
    // in the background in that case.
    runCatching { enterPictureInPictureMode(params) }
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

/** Tablets and TV boxes without the PiP feature exist even on new Android versions. */
private fun Activity.supportsPictureInPicture(): Boolean =
    packageManager.hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE)

private tailrec fun Context.findActivityOrNull(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivityOrNull()
    else -> null
}
