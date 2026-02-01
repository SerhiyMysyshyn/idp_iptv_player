package com.serhiimysyshyn.devlightiptvclient.presentation.di

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
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { PlaylistsViewModel(get(), get()) }
    viewModel { ChannelsViewModel(get(), get()) }
    viewModel { PlayerViewModel(get(), get(), get()) }
    viewModel { SettingsScreenViewModel(get(), get()) }

    factory { MainScreenReducer() }
    factory { PlaylistsScreenReducer() }
    factory { ChannelsScreenReducer() }
    factory { PlayerScreenReducer() }
    factory { SettingsScreenReducer() }
}