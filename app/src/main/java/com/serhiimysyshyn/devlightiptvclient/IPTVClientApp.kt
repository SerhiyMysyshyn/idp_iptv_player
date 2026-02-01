package com.serhiimysyshyn.devlightiptvclient

import android.app.Application
import com.serhiimysyshyn.devlightiptvclient.data.di.dataModule
import com.serhiimysyshyn.devlightiptvclient.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class IPTVClientApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@IPTVClientApp)
            modules(
                presentationModule,
                dataModule
            )
        }
    }
}