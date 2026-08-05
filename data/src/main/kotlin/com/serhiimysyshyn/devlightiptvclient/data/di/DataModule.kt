package com.serhiimysyshyn.devlightiptvclient.data.di

import androidx.room.Room
import com.serhiimysyshyn.devlightiptvclient.data.database.AppDatabase
import com.serhiimysyshyn.devlightiptvclient.data.repository.MainRepositoryImpl
import com.serhiimysyshyn.devlightiptvclient.data.repository.ThemeRepositoryImpl
import com.serhiimysyshyn.devlightiptvclient.domain.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.domain.repository.ThemeRepository
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            DATABASE_NAME,
        ).build()
    }

    // A single OkHttpClient: it owns a connection and thread pool, so creating one per request
    // (as the repository used to) leaks sockets and threads.
    single { OkHttpClient() }

    single { get<AppDatabase>().playlistDao() }
    single { get<AppDatabase>().channelDao() }

    single<MainRepository> { MainRepositoryImpl(get(), get(), get()) }
    single<ThemeRepository> { ThemeRepositoryImpl(get()) }
}

private const val DATABASE_NAME = "app_database"
