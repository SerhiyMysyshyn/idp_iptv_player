package com.serhiimysyshyn.devlightiptvclient.presentation.screens.playlists.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun AddNewPlaylistDialog(
    onConfirmClicked: (String) -> Unit,
    onDismiss: () -> Unit
) {
    // FOR TEST ONLY
    var text by remember { mutableStateOf("https://iptv.org.ua/iptv/kino-plus.m3u") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 8.dp,
            modifier = Modifier.padding(16.dp)
        ) {
            Column {
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Введіть посилання для завантаження .M3U плейлиста",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = text,
                    onValueChange = { newText -> text = newText },
                    placeholder = { Text("URL for downloading") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onClick = {
                        onConfirmClicked.invoke(text)
                        onDismiss()
                    }
                ) {
                    Text(
                        text = "Готово")
                }
            }
        }
    }
}