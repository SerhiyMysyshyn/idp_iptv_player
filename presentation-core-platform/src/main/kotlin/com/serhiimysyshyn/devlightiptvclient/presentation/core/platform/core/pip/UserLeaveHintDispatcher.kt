package com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.pip

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Provided by the hosting activity. Null when no activity supplies one, so a screen can fall back
 * to doing nothing rather than crashing in a preview.
 */
val LocalUserLeaveHintOwner = staticCompositionLocalOf<UserLeaveHintOwner?> { null }

/**
 * Lets a screen react to `Activity.onUserLeaveHint` without depending on the activity class.
 *
 * `onUserLeaveHint` fires only when the user leaves the app (home, recents) — never on in-app
 * navigation. That distinction is the whole point: `ON_PAUSE` also fires when a screen is simply
 * popped off the back stack, which made the player enter Picture-in-Picture on its own back button.
 *
 * `ComponentActivity` does not implement `OnUserLeaveHintProvider` (as of activity 1.8.2), so the
 * hosting activity overrides the callback and forwards it here.
 */
interface UserLeaveHintOwner {

    fun addOnUserLeaveHintListener(listener: () -> Unit)

    fun removeOnUserLeaveHintListener(listener: () -> Unit)
}

/** Straightforward listener list an activity can delegate to. */
class UserLeaveHintDispatcher : UserLeaveHintOwner {

    private val listeners = mutableSetOf<() -> Unit>()

    override fun addOnUserLeaveHintListener(listener: () -> Unit) {
        listeners += listener
    }

    override fun removeOnUserLeaveHintListener(listener: () -> Unit) {
        listeners -= listener
    }

    /** Called by the activity from its `onUserLeaveHint` override. */
    fun dispatchUserLeaveHint() {
        // Copy first: a listener may remove itself while being notified.
        listeners.toList().forEach { it() }
    }
}
