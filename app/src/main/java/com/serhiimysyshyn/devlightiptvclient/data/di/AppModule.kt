package com.serhiimysyshyn.devlightiptvclient.data.di

import androidx.room.Room
import com.serhiimysyshyn.devlightiptvclient.data.database.AppDatabase
import com.serhiimysyshyn.devlightiptvclient.data.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.data.repository.MainRepositoryImpl
import com.serhiimysyshyn.devlightiptvclient.data.repository.ThemeRepository
import com.serhiimysyshyn.devlightiptvclient.data.repository.ThemeRepositoryImpl
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.ChannelsViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.channels.contract.ChannelsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.MainViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.main.contract.MainScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.PlayerViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.player.contract.PlayerScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.PlaylistsViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.contract.PlaylistsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.SettingsScreenViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.contract.SettingsScreenReducer
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
    single<MainRepository> { MainRepositoryImpl(get(), get()) }
    single<ThemeRepository> { ThemeRepositoryImpl(get()) }

    // ViewModels
    viewModel { MainViewModel(get(), get()) }
    viewModel { PlaylistsViewModel(get(), get()) }
    viewModel { ChannelsViewModel(get(), get()) }
    viewModel { PlayerViewModel(get(), get(), get()) }
    viewModel { SettingsScreenViewModel(get(), get()) }

    // Others
    factory { MainScreenReducer() }
    factory { PlaylistsScreenReducer() }
    factory { ChannelsScreenReducer() }
    factory { PlayerScreenReducer() }
    factory { SettingsScreenReducer() }
}