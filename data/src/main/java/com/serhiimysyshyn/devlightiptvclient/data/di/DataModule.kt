package com.serhiimysyshyn.devlightiptvclient.data.di

import androidx.room.Room
import com.serhiimysyshyn.devlightiptvclient.data.database.AppDatabase
import com.serhiimysyshyn.devlightiptvclient.data.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.data.repository.MainRepositoryImpl
import com.serhiimysyshyn.devlightiptvclient.data.repository.ThemeRepository
import com.serhiimysyshyn.devlightiptvclient.data.repository.ThemeRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    // DAO
    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().channelDao() }

    // Repos
    single<MainRepository> { MainRepositoryImpl(get(), get()) }
    single<ThemeRepository> { ThemeRepositoryImpl(get()) }
}