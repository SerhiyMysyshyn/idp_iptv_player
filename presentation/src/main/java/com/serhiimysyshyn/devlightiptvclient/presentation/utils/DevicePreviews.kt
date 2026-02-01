package com.serhiimysyshyn.devlightiptvclient.presentation.utils

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Default Light", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Default Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "4-inch Light", widthDp = 320, heightDp = 533, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "4-inch Dark", widthDp = 320, heightDp = 533, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "5-inch Light", widthDp = 360, heightDp = 640, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "5-inch Dark", widthDp = 360, heightDp = 640, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "7-inch Light", widthDp = 600, heightDp = 960, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "7-inch Dark", widthDp = 600, heightDp = 960, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "10-inch Light", widthDp = 800, heightDp = 1280, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "10-inch Dark", widthDp = 800, heightDp = 1280, uiMode = Configuration.UI_MODE_NIGHT_YES)
annotation class DevicePreviews