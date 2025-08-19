package com.serhiimysyshyn.devlightiptvclient.data.di

import androidx.room.Room
import com.serhiimysyshyn.devlightiptvclient.data.database.AppDatabase
import com.serhiimysyshyn.devlightiptvclient.data.repository.IMainRepository
import com.serhiimysyshyn.devlightiptvclient.data.repository.MainRepositoryImpl
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.ChannelsViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.intent.ChannelsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.MainViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.intent.MainScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.PlaylistsViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.intent.PlaylistsScreenReducer
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Room DB instance
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
    single<IMainRepository> { MainRepositoryImpl(get(), get()) }

    // ViewModels
    viewModel { MainViewModel(get(), get()) }
    viewModel { PlaylistsViewModel(get(), get()) }
    viewModel { ChannelsViewModel(get(), get()) }

    // Others
    factory { MainScreenReducer() }
    factory { PlaylistsScreenReducer() }
    factory { ChannelsScreenReducer() }
}