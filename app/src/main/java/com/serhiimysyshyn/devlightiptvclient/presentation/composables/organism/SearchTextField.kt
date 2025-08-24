package com.serhiimysyshyn.devlightiptvclient.presentation.composables.organism

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.serhiimysyshyn.devlightiptvclient.presentation.theme.IPTVClientTheme
import com.serhiimysyshyn.devlightiptvclient.presentation.utils.DevicePreviews

@Composable
internal fun SearchTextField(
    value: String,
    placeholder: String = "",
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = { query ->
            onValueChange.invoke(query)
        },
        textStyle = LocalTextStyle.current.copy(
            fontSize = 18.sp
        ),
        placeholder = { Text(text = placeholder) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp
            ),
        singleLine = true,
        shape = ShapeDefaults.Medium,
        colors = TextFieldDefaults.colors().copy(
            focusedTextColor = IPTVClientTheme.colors.primary,
            unfocusedTextColor = IPTVClientTheme.colors.primary,
            disabledTextColor = IPTVClientTheme.colors.primary,
            errorTextColor = IPTVClientTheme.colors.surface,
            focusedContainerColor = IPTVClientTheme.colors.onBackground,
            unfocusedContainerColor = IPTVClientTheme.colors.onBackground,
            unfocusedIndicatorColor = IPTVClientTheme.colors.onBackground,
            focusedIndicatorColor = IPTVClientTheme.colors.onBackground,
        )
    )
}

@Composable
@DevicePreviews
fun SearchTextFieldPreview() {
    IPTVClientTheme {
        SearchTextField(
            value = "Test title",
            onValueChange = {}
        )
    }
}