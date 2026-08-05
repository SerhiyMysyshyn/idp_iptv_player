package com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.model.PlayerTrack
import com.serhiimysyshyn.devlightiptvclient.presentation.feature.player.screen.player.model.PlayerTrackType
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

/** Lets the viewer switch audio language or subtitles — many IPTV streams ship several. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TrackSelectorBottomSheet(
    tracks: List<PlayerTrack>,
    onTrackSelected: (PlayerTrack) -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(),
        containerColor = Theme.colors.semantic.background.primaryMain,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Theme.spacing.m)
                .padding(bottom = Theme.spacing.xl),
        ) {
            Text(
                text = stringResource(CoreUiR.string.player_tracks),
                style = Theme.typography.h2,
                color = Theme.colors.semantic.text.primary,
            )

            if (tracks.isEmpty()) {
                Text(
                    text = stringResource(CoreUiR.string.player_tracks_empty),
                    style = Theme.typography.body,
                    color = Theme.colors.semantic.text.secondary,
                    modifier = Modifier.padding(top = Theme.spacing.m),
                )
                return@Column
            }

            TrackSection(
                titleRes = CoreUiR.string.player_tracks_audio,
                tracks = tracks.filter { it.type == PlayerTrackType.AUDIO },
                onTrackSelected = onTrackSelected,
            )

            TrackSection(
                titleRes = CoreUiR.string.player_tracks_subtitles,
                tracks = tracks.filter { it.type == PlayerTrackType.SUBTITLE },
                onTrackSelected = onTrackSelected,
            )
        }
    }
}

@Composable
private fun TrackSection(
    titleRes: Int,
    tracks: List<PlayerTrack>,
    onTrackSelected: (PlayerTrack) -> Unit,
) {
    if (tracks.isEmpty()) return

    Text(
        text = stringResource(titleRes),
        style = Theme.typography.body,
        color = Theme.colors.semantic.text.secondary,
        modifier = Modifier.padding(top = Theme.spacing.m, bottom = Theme.spacing.xs),
    )

    tracks.forEach { track ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectable(
                    selected = track.isSelected,
                    role = Role.RadioButton,
                    onClick = { onTrackSelected(track) },
                )
                .padding(vertical = Theme.spacing.s),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(selected = track.isSelected, onClick = null)

            Text(
                text = track.label,
                style = Theme.typography.body,
                color = Theme.colors.semantic.text.primary,
                modifier = Modifier.padding(start = Theme.spacing.s),
            )
        }
    }
}
