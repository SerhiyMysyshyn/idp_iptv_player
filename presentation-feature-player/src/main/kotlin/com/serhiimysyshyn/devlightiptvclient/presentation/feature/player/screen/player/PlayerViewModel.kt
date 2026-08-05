package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player

import androidx.lifecycle.SavedStateHandle
import com.serhiimysyshyn.devlightiptvclient.domain.repository.MainRepository
import com.serhiimysyshyn.devlightiptvclient.domain.repository.PlayerPreferencesRepository
import com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation.source.NavigationRoute
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.viewmodel.BaseViewModel
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.ext.safeLaunch
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.AudioEffectIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenEvent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenIntent
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenReducer
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract.PlayerScreenState
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.equalizer.EqualizerPreset
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlayerViewModel(
    savedStateHandle: SavedStateHandle,
    private val mainRepository: MainRepository,
    private val playerPreferencesRepository: PlayerPreferencesRepository,
    private val reducer: PlayerScreenReducer,
) : BaseViewModel<PlayerScreenIntent>() {

    private val _state = MutableStateFlow(PlayerScreenState())
    val state: StateFlow<PlayerScreenState> = _state.asStateFlow()

    private val channelId: Long =
        savedStateHandle[NavigationRoute.Player.ARG_CHANNEL_ID] ?: NO_CHANNEL_ID

    init {
        if (channelId != NO_CHANNEL_ID) {
            loadChannel(channelId)
        }
        observeFavouriteChannels()
        observePictureInPicturePreference()
    }

    override fun processIntent(intent: PlayerScreenIntent) {
        when (intent) {
            is PlayerScreenIntent.LoadChannel -> loadChannel(intent.channelId)
            is PlayerScreenIntent.ToggleChannelFavourite -> toggleFavourite(intent.channelId)
            is PlayerScreenIntent.LoadFavouriteChannels -> observeFavouriteChannels()
            is PlayerScreenIntent.ToggleFullscreen -> toggleFullscreen()
            is PlayerScreenIntent.PlaybackErrorOccurred ->
                emit(PlayerScreenEvent.PlaybackErrorChanged(intent.isError))
            // Clearing the flag is all the VM does — re-preparing the stream is the Screen's job,
            // since it owns the ExoPlayer instance.
            is PlayerScreenIntent.RetryPlayback ->
                emit(PlayerScreenEvent.PlaybackErrorChanged(false))
            is PlayerScreenIntent.TracksAvailable ->
                emit(PlayerScreenEvent.TracksChanged(intent.tracks))
            is PlayerScreenIntent.ShowTrackSelector -> emit(PlayerScreenEvent.ShowTrackSelector)
            is PlayerScreenIntent.HideTrackSelector -> emit(PlayerScreenEvent.HideTrackSelector)
            // Applying the override belongs to the Screen (it owns the player); closing the
            // sheet is the only state change.
            is PlayerScreenIntent.SelectTrack -> emit(PlayerScreenEvent.HideTrackSelector)
            is AudioEffectIntent -> processAudioEffectIntent(intent)
        }
    }

    /** Split out so [processIntent] stays under detekt's complexity ceiling. */
    private fun processAudioEffectIntent(intent: AudioEffectIntent) {
        when (intent) {
            is PlayerScreenIntent.ApplyPreset -> emit(PlayerScreenEvent.PresetApplied(intent.preset))
            is PlayerScreenIntent.ShowAudioSettings -> emit(PlayerScreenEvent.ShowAudioSettings)
            is PlayerScreenIntent.HideAudioSettings -> emit(PlayerScreenEvent.HideAudioSettings)
            is PlayerScreenIntent.AudioEffectsReady ->
                emit(PlayerScreenEvent.EqualizerBandsChanged(intent.bands))
            is PlayerScreenIntent.ChangeBandLevel -> changeBandLevel(intent.index, intent.millibel)
            is PlayerScreenIntent.ChangeBassBoost ->
                emit(PlayerScreenEvent.BassBoostChanged(intent.strength))
            is PlayerScreenIntent.ChangeVirtualizer ->
                emit(PlayerScreenEvent.VirtualizerChanged(intent.strength))
            is PlayerScreenIntent.ResetAudioEffects -> resetAudioEffects()
        }
    }

    /**
     * Dragging a band means the sound no longer matches whichever named preset was selected, so
     * the preset label falls back to CUSTOM.
     */
    private fun changeBandLevel(index: Short, millibel: Short) {
        val updated = _state.value.equalizerBands.map { band ->
            if (band.index == index) band.copy(levelMillibel = millibel) else band
        }

        emit(PlayerScreenEvent.EqualizerBandsChanged(updated))
        emit(PlayerScreenEvent.PresetApplied(EqualizerPreset.CUSTOM))
    }

    private fun resetAudioEffects() {
        emit(PlayerScreenEvent.PresetApplied(EqualizerPreset.NORMAL))
        emit(PlayerScreenEvent.BassBoostChanged(NO_STRENGTH))
        emit(PlayerScreenEvent.VirtualizerChanged(NO_STRENGTH))
    }

    private fun toggleFullscreen() {
        emit(PlayerScreenEvent.FullscreenChanged(!_state.value.isFullscreen))
    }

    private fun loadChannel(channelId: Long) {
        safeLaunch(onError = { emit(PlayerScreenEvent.Error) }) {
            emit(PlayerScreenEvent.ChannelLoaded(mainRepository.getChannelInfoById(channelId)))
        }
    }

    private fun observePictureInPicturePreference() {
        safeLaunch(onError = { emit(PlayerScreenEvent.Error) }) {
            playerPreferencesRepository.isPictureInPictureEnabled().collect { isEnabled ->
                emit(PlayerScreenEvent.PictureInPictureEnabledLoaded(isEnabled))
            }
        }
    }

    private fun observeFavouriteChannels() {
        safeLaunch(onError = { emit(PlayerScreenEvent.Error) }) {
            mainRepository.getFavouriteChannels().collect { channels ->
                emit(PlayerScreenEvent.FavouriteChannelsLoaded(channels))
            }
        }
    }

    /**
     * Toggles rather than only adding — the button showed a filled heart for an already-favourite
     * channel but tapping it re-added the channel instead of removing it.
     */
    private fun toggleFavourite(channelId: Long) {
        val isFavourite = _state.value.currentChannel?.isFavorite == true

        safeLaunch(onError = { emit(PlayerScreenEvent.Error) }) {
            if (isFavourite) {
                mainRepository.removeChannelFromFavourite(channelId)
            } else {
                mainRepository.addChannelToFavourite(channelId)
            }
        }
    }

    private fun emit(event: PlayerScreenEvent) {
        _state.value = reducer.reduce(_state.value, event)
    }

    private companion object {
        const val NO_CHANNEL_ID = -1L
        const val NO_STRENGTH: Short = 0
    }
}
