package com.serhiimysyshyn.devlightiptvclient.presentation.feature.home.screen.playlists.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.R as CoreUiR

/**
 * Asks for an `.m3u` URL.
 *
 * The confirm button is disabled until the input looks like a playlist URL — the previous version
 * silently did nothing when the text failed its `.endsWith(".m3u")` check, which read as a broken
 * button.
 */
@Composable
internal fun AddNewPlaylistDialog(
    onConfirmClicked: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var url by rememberSaveable { mutableStateOf(DEV_PLAYLIST_URL) }
    val isValid = remember(url) { url.isValidPlaylistUrl() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(CoreUiR.string.add_playlist_title),
                style = Theme.typography.title,
            )
        },
        text = {
            Column {
                TextField(
                    value = url,
                    onValueChange = { url = it },
                    placeholder = { Text(stringResource(CoreUiR.string.add_playlist_hint)) },
                    singleLine = true,
                    isError = url.isNotBlank() && !isValid,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Done,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Theme.spacing.s),
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = isValid,
                onClick = {
                    onConfirmClicked(url)
                    onDismiss()
                },
            ) {
                Text(stringResource(CoreUiR.string.action_done))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(CoreUiR.string.action_close))
            }
        },
    )
}

private fun String.isValidPlaylistUrl(): Boolean =
    (startsWith("http://") || startsWith("https://")) && endsWith(".m3u", ignoreCase = true)

/** Prefilled so adding a playlist during development is one tap. Clear the field to enter another. */
private const val DEV_PLAYLIST_URL = "https://iptv.org.ua/iptv/kino-plus.m3u"
