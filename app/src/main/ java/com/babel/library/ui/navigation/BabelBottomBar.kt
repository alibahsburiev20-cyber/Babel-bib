package com.babel.library.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.babel.library.ui.theme.BabelGold
import com.babel.library.ui.theme.BabelSurface
import com.babel.library.ui.theme.BabelTextSecondary

enum class BabelDestination(val route: String, val label: String) {
    SEARCH("search", "Поиск"),
    RANDOM("random", "Случайно"),
    BOOKMARKS("bookmarks", "Закладки"),
    ADDRESS("address", "Адрес")
}

@Composable
fun BabelBottomBar(
    current: BabelDestination,
    onSelect: (BabelDestination) -> Unit
) {
    NavigationBar(
        containerColor = BabelSurface,
        tonalElevation = 0.dp
    ) {
        BabelDestination.entries.forEach { dest ->
            val selected = dest == current
            NavigationBarItem(
                selected = selected,
                onClick = { onSelect(dest) },
                icon = { Icon(iconFor(dest, selected), contentDescription = dest.label) },
                label = { Text(dest.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BabelGold,
                    selectedTextColor = BabelGold,
                    unselectedIconColor = BabelTextSecondary,
                    unselectedTextColor = BabelTextSecondary,
                    indicatorColor = BabelSurface
                )
            )
        }
    }
}

private fun iconFor(dest: BabelDestination, selected: Boolean): ImageVector = when (dest) {
    BabelDestination.SEARCH -> if (selected) Icons.Filled.Search else Icons.Outlined.Search
    BabelDestination.RANDOM -> if (selected) Icons.Filled.Description else Icons.Outlined.Description
    BabelDestination.BOOKMARKS -> if (selected) Icons.Filled.Bookmark else Icons.Outlined.Bookmark
    BabelDestination.ADDRESS -> if (selected) Icons.Filled.Description else Icons.Outlined.Description
}
