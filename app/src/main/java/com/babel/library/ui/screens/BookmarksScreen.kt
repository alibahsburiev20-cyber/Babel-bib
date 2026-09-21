package com.babel.library.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.babel.library.core.Address
import com.babel.library.ui.theme.*

@Composable
fun BookmarksScreen(
    bookmarks: List<Address>,
    onOpen: (Address) -> Unit,
    onRemove: (Address) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BabelBackground)
    ) {
        Text(
            "Закладки",
            style = MaterialTheme.typography.titleLarge,
            color = BabelTextPrimary,
            modifier = Modifier.padding(20.dp)
        )

        if (bookmarks.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Bookmark, contentDescription = null, tint = BabelTextMuted, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Пока нет сохранённых страниц.\nНайдите или сгенерируйте что-нибудь интересное.",
                        color = BabelTextSecondary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(bookmarks) { address ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(BabelSurface)
                            .border(1.dp, BabelBorder, RoundedCornerShape(12.dp))
                            .clickable { onOpen(address) }
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(address.toDisplayString(), color = BabelTextPrimary, style = AddressTextStyle)
                        Row {
                            IconButton(onClick = { onRemove(address) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Удалить", tint = BabelTextMuted)
                            }
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = BabelTextMuted)
                        }
                    }
                }
            }
        }
    }
}
