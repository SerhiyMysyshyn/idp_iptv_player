package com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.serhiimysyshyn.devlightiptvclient.presentation.core.platform.core.preview.DevicePreviews
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.source.theme.AppTheme

/** Single-line search input. */
@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = Theme.typography.title,
        placeholder = {
            Text(
                text = placeholder,
                style = Theme.typography.title,
                color = Theme.colors.semantic.text.placeholder,
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = Theme.spacing.m,
                top = Theme.spacing.m,
                end = Theme.spacing.m,
            ),
        singleLine = true,
        shape = RoundedCornerShape(Theme.radius.m),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Theme.colors.semantic.text.primary,
            unfocusedTextColor = Theme.colors.semantic.text.primary,
            disabledTextColor = Theme.colors.semantic.text.placeholder,
            errorTextColor = Theme.colors.semantic.text.error,
            focusedContainerColor = Theme.colors.semantic.background.primaryContent,
            unfocusedContainerColor = Theme.colors.semantic.background.primaryContent,
            disabledContainerColor = Theme.colors.semantic.background.secondaryMain,
            focusedIndicatorColor = Theme.colors.semantic.border.primary,
            unfocusedIndicatorColor = Theme.colors.static.transparent,
            cursorColor = Theme.colors.semantic.foreground.accent,
        ),
    )
}

@DevicePreviews
@Composable
private fun SearchTextFieldPreview() {
    AppTheme {
        SearchTextField(
            value = "",
            placeholder = "Пошук",
            onValueChange = {},
        )
    }
}
