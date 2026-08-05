package com.serhiimysyshyn.devlightiptvclient.presentation.core.ui.component.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.serhiimysyshyn.devlightiptvclient.presentation.core.styling.core.Theme

/**
 * Palette for generated avatars. Mid-range saturation so white initials stay legible on every
 * entry, and so a list of them reads as one family rather than a bag of random hues.
 */
private val AVATAR_COLORS = listOf(
    Color(0xFF4F46E5), // indigo
    Color(0xFF0891B2), // cyan
    Color(0xFF059669), // emerald
    Color(0xFFD97706), // amber
    Color(0xFFDC2626), // red
    Color(0xFF7C3AED), // violet
    Color(0xFFDB2777), // pink
    Color(0xFF475569), // slate
)

/**
 * A channel's logo, falling back to a coloured square with the channel's initial.
 *
 * Real IPTV playlists are inconsistent — many channels ship no `tvg-logo` at all and some logo
 * URLs are dead — so the fallback is the common case, not an edge case.
 *
 * @param logoUrl `tvg-logo` from the playlist; blank falls straight through to the initial.
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ChannelAvatar(
    name: String,
    logoUrl: String,
    size: Dp,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(Theme.radius.m)

    Box(
        modifier = modifier
            .size(size)
            .clip(shape)
            .background(avatarColorFor(name)),
        contentAlignment = Alignment.Center,
    ) {
        // The initial sits underneath rather than in Glide's `loading`/`failure` slots: it then
        // covers a blank URL, a slow load and a dead link with one code path.
        Text(
            text = name.initial(),
            style = Theme.typography.title,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )

        if (logoUrl.isNotBlank()) {
            GlideImage(
                model = logoUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(size)
                    .clip(shape),
            )
        }
    }
}

/** Same name always yields the same colour, so a channel keeps its identity across launches. */
private fun avatarColorFor(name: String): Color {
    if (name.isEmpty()) return AVATAR_COLORS.last()

    // Guard against Int.MIN_VALUE, whose absolute value is still negative.
    val index = (name.hashCode().toLong().let(Math::abs) % AVATAR_COLORS.size).toInt()

    return AVATAR_COLORS[index]
}

private fun String.initial(): String =
    firstOrNull { it.isLetterOrDigit() }?.uppercase() ?: "?"
