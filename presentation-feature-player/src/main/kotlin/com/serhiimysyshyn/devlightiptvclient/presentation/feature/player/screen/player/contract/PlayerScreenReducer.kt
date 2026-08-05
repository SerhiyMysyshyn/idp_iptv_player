package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.contract

import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.base.reducer.BaseScreenReducer

class PlayerScreenReducer : BaseScreenReducer<PlayerScreenState, PlayerScreenEvent>() {

    override fun reduce(
        currentState: PlayerScreenState,
        event: PlayerScreenEvent,
    ): PlayerScreenState = when (event) {
        is PlayerScreenEvent.ChannelLoaded -> currentState.copy(
            isLoading = false,
            isError = false,
            currentChannel = event.channel,
        )

        is PlayerScreenEvent.FavouriteChannelsLoaded -> currentState.copy(
            likedChannels = event.favouriteChannels,
            // The favourites stream is the source of truth for the favourite flag, so refresh the
            // currently playing channel from it — otherwise the heart icon goes stale after a tap.
            currentChannel = currentState.currentChannel?.let { channel ->
                channel.copy(isFavorite = event.favouriteChannels.any { it.id == channel.id })
            },
        )

        is PlayerScreenEvent.FullscreenChanged -> currentState.copy(isFullscreen = event.isFullscreen)

        is PlayerScreenEvent.PlaybackErrorChanged -> currentState.copy(
            hasPlaybackError = event.hasError,
        )

        is PlayerScreenEvent.TracksChanged -> currentState.copy(tracks = event.tracks)

        is PlayerScreenEvent.ShowTrackSelector -> currentState.copy(showTrackSelector = true)

        is PlayerScreenEvent.HideTrackSelector -> currentState.copy(showTrackSelector = false)

        is PlayerScreenEvent.PictureInPictureEnabledLoaded -> currentState.copy(
            isPictureInPictureEnabled = event.isEnabled,
        )

        is PlayerScreenEvent.Error -> currentState.copy(
            isLoading = false,
            isError = true,
        )

        is AudioEffectEvent -> reduceAudioEffect(currentState, event)
    }

    /** Split out so the main `when` stays under detekt's complexity ceiling. */
    private fun reduceAudioEffect(
        currentState: PlayerScreenState,
        event: AudioEffectEvent,
    ): PlayerScreenState = when (event) {
        // The sheet stays open — the point of the panel is to hear a preset take effect and then
        // fine-tune it with the band sliders.
        is PlayerScreenEvent.PresetApplied -> currentState.copy(currentPreset = event.preset)

        is PlayerScreenEvent.ShowAudioSettings -> currentState.copy(showAudioSettings = true)

        is PlayerScreenEvent.HideAudioSettings -> currentState.copy(showAudioSettings = false)

        is PlayerScreenEvent.EqualizerBandsChanged -> currentState.copy(equalizerBands = event.bands)

        is PlayerScreenEvent.BassBoostChanged -> currentState.copy(bassBoostStrength = event.strength)

        is PlayerScreenEvent.VirtualizerChanged -> currentState.copy(
            virtualizerStrength = event.strength,
        )
    }
}
