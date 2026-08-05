package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.di

import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.PlayerViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenReducer
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val presentationFeaturePlayerModule = module {
    viewModelOf(::PlayerViewModel)
    factoryOf(::PlayerScreenReducer)
}
