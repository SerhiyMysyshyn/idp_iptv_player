package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.di

import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.ChannelsViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.channels.contract.ChannelsScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.MainViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.main.contract.MainScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.PlaylistsViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.contract.PlaylistsScreenReducer
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Each feature owns its own Koin module; `:app` merges them.
 *
 * `viewModelOf` / `factoryOf` wire constructor parameters from the graph, so adding a dependency
 * to a ViewModel no longer means editing a `get(), get(), get()` list here.
 */
val presentationFeatureHomeModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::PlaylistsViewModel)
    viewModelOf(::ChannelsViewModel)

    factoryOf(::MainScreenReducer)
    factoryOf(::PlaylistsScreenReducer)
    factoryOf(::ChannelsScreenReducer)
}
