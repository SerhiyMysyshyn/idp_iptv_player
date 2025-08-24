package com.serhiimysyshyn.devlightiptvclient.presentation.screens.settings.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import kotlin.reflect.full.memberProperties

@Composable
fun AppColorsDialog(
    onDismiss: () -> Unit
) {
    val appColors = IPTVClientTheme.colors
    val colorList = remember { extractColors(appColors) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Кольорова палітра додатку")
        },
        text = {
            LazyColumn {
                items(colorList) { (name, color) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color, shape = CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(name)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрити")
            }
        }
    )
}

fun extractColors(appColors: Any): List<Pair<String, Color>> {
    return appColors::class.memberProperties
        .filter { it.returnType.classifier == Color::class }
        .mapNotNull { prop ->
            val value = prop.getter.call(appColors) as? Color
            value?.let { prop.name to it }
        }
}