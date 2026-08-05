package com.serhiimysyshyn.devlightiptvclient

import android.app.Application
import com.serhiimysyshyn.devlightiptvclient.data.di.dataModule
import com.serhiimysyshyn.devlightiptvclient.data.preference.di.dataPreferenceModule
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.di.presentationFeatureHomeModule
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.di.presentationFeaturePlayerModule
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.settings.di.presentationFeatureSettingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Composition root.
 *
 * Every module that declares a `val xxxModule = module { … }` must be registered here — Koin
 * resolves nothing that is not in this list.
 */
class IPTVClientApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@IPTVClientApp)
            modules(
                // Data
                dataModule,
                dataPreferenceModule,
                // Presentation features
                presentationFeatureHomeModule,
                presentationFeaturePlayerModule,
                presentationFeatureSettingsModule,
            )
        }
    }
}
