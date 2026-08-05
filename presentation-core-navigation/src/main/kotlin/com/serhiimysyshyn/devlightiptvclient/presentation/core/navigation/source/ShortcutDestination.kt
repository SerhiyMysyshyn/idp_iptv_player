package com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/** Destinations reachable from a launcher long-press shortcut. */
enum class ShortcutDestination(val value: String) {
    FAVOURITES("favourites"),
    ;

    companion object {
        const val INTENT_EXTRA = "destination"

        fun fromValue(value: String?): ShortcutDestination? =
            entries.find { it.value == value }
    }
}

/**
 * Carries a shortcut launch from the activity to whichever screen owns the matching nav graph.
 *
 * A flow rather than a plain value because the app is `singleTask`: tapping the shortcut while the
 * app is already running delivers `onNewIntent` instead of recreating the activity, and a
 * replayless flow re-emits for that second tap where a `State` holding the same value would not.
 */
class ShortcutDestinationBus {

    private val _destinations = MutableSharedFlow<ShortcutDestination>(extraBufferCapacity = 1)
    val destinations: SharedFlow<ShortcutDestination> = _destinations.asSharedFlow()

    fun emit(destination: ShortcutDestination) {
        _destinations.tryEmit(destination)
    }
}

val LocalShortcutDestinationBus = staticCompositionLocalOf<ShortcutDestinationBus?> { null }
