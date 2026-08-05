package com.serhiimysyshyn.devlightiptvclient

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.serhiimysyshyn.devlightiptvclient.domain.model.AppThemeType
import com.serhiimysyshyn.devlightiptvclient.domain.repository.ThemeRepository
import com.serhiimysyshyn.devlightiptvclient.navigation.AppNavHost
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.core.provider.LocalAppNavController
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.LocalShortcutDestinationBus
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.ShortcutDestination
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.ShortcutDestinationBus
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.pip.LocalUserLeaveHintOwner
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.pip.UserLeaveHintDispatcher
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme.AppTheme
import org.koin.android.ext.android.inject

/**
 * The single activity. Applies the persisted theme and hosts the root navigation graph.
 */
class MainActivity : ComponentActivity() {

    private val themeRepository: ThemeRepository by inject()

    /**
     * `ComponentActivity` does not implement `OnUserLeaveHintProvider`, so the callback is
     * forwarded manually to whichever screen cares (currently the player, for Picture-in-Picture).
     */
    private val userLeaveHintDispatcher = UserLeaveHintDispatcher()

    private val shortcutDestinationBus = ShortcutDestinationBus()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        publishShortcutDestination(intent)

        setContent {
            val themeType by themeRepository.getTheme()
                .collectAsStateWithLifecycle(initialValue = AppThemeType.SYSTEM)

            AppTheme(useDarkTheme = themeType.isDarkTheme()) {
                val navController = rememberNavController()

                CompositionLocalProvider(
                    LocalAppNavController provides navController,
                    LocalUserLeaveHintOwner provides userLeaveHintDispatcher,
                    LocalShortcutDestinationBus provides shortcutDestinationBus,
                ) {
                    AppNavHost(navController = navController)
                }
            }
        }
    }

    /** The activity is `singleTask`, so a shortcut tap on a running app lands here, not in onCreate. */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        publishShortcutDestination(intent)
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        userLeaveHintDispatcher.dispatchUserLeaveHint()
    }

    private fun publishShortcutDestination(intent: Intent?) {
        val extra = intent?.getStringExtra(ShortcutDestination.INTENT_EXTRA)

        ShortcutDestination.fromValue(extra)?.let(shortcutDestinationBus::emit)
    }
}

/**
 * Resolves the stored preference to a concrete light/dark decision.
 *
 * `SYSTEM` has to be resolved inside composition so that the app follows a system theme change
 * without a restart.
 */
@Composable
private fun AppThemeType.isDarkTheme(): Boolean = when (this) {
    AppThemeType.LIGHT -> false
    AppThemeType.DARK -> true
    AppThemeType.SYSTEM -> isSystemInDarkTheme()
}
