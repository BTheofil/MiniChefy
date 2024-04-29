package hu.tb.minichefy.presentation.screens.settings.settings_main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.tb.minichefy.R
import hu.tb.minichefy.domain.model.settings.SettingItem
import hu.tb.minichefy.presentation.screens.settings.navigation.TAG_SCREEN
import hu.tb.minichefy.presentation.screens.settings.navigation.THEME_SCREEN
import hu.tb.minichefy.presentation.ui.theme.SCREEN_VERTICAL_PADDING
import hu.tb.minichefy.presentation.util.icons.iconVectorResource

@Composable
fun SettingsScreen(
    onSettingsClick: (route: String) -> Unit,
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = SCREEN_VERTICAL_PADDING,
                )
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White),
            ) {
                settingsMenuList.forEach { item ->
                    ListItem(
                        modifier = Modifier
                            .clickable { onSettingsClick(item.screenTag) },
                        leadingContent = {
                            Icon(
                                modifier = Modifier
                                    .size(24.dp),
                                imageVector = iconVectorResource(iconResource = item.icon),
                                contentDescription = "tag icon",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        headlineContent = {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        },
                        colors = ListItemDefaults.colors(
                            containerColor = Color.White
                        )
                    )
                    if (settingsMenuList.last() != item) {
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            thickness = 1.dp,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

private val settingsMenuList: List<SettingItem> = listOf(
    SettingItem(
        "Tag",
        R.drawable.tag_icon,
        TAG_SCREEN
    ),
    SettingItem(
        "Theme",
        R.drawable.theme_icon,
        THEME_SCREEN
    )
)

@Preview
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen {}
}