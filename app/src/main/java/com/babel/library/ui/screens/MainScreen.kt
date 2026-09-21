package com.babel.library.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babel.library.core.Address
import com.babel.library.ui.theme.*

@Composable
fun MainScreen(
    lastAddress: Address?,
    onSearchClick: () -> Unit,
    onGenerateRandom: () -> Unit,
    onOpenAddress: (Address) -> Unit,
    onSettingsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BabelBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("BABEL LIBRARY", style = MaterialTheme.typography.headlineMedium, color = BabelGold, letterSpacing = 2.sp)
                Text("БИБЛИОТЕКА ВАВИЛОНА", style = MaterialTheme.typography.labelSmall, color = BabelTextSecondary, letterSpacing = 2.sp)
            }
            IconButton(onClick = onSettingsClick) {
                Icon(Icons.Filled.Settings, contentDescription = "Настройки", tint = BabelTextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Поисковая строка
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(BabelSurface)
                .border(1.dp, BabelGoldDim, RoundedCornerShape(26.dp))
                .clickable { onSearchClick() }
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = BabelGold, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text("Поиск по книгам, авторам, темам...", color = BabelTextMuted, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.weight(1f))

        // Центральная кнопка генерации
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape)
                    .border(1.5.dp, BabelGold, CircleShape)
                    .background(BabelSurface)
                    .clickable { onGenerateRandom() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Filled.MenuBook,
                        contentDescription = null,
                        tint = BabelGold,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Сгенерировать\nслучайную страницу",
                        color = BabelTextPrimary,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Текущие координаты
        if (lastAddress != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BabelSurface)
                    .border(1.dp, BabelBorder, RoundedCornerShape(16.dp))
                    .clickable { onOpenAddress(lastAddress) }
                    .padding(16.dp)
            ) {
                Text(
                    "ТЕКУЩИЕ КООРДИНАТЫ",
                    color = BabelGoldDim,
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                CoordinateRow("Hexagon", lastAddress.hexagon.toString(16).uppercase())
                CoordinateRow("Wall", ('A' + lastAddress.wall).toString())
                CoordinateRow("Shelf", lastAddress.shelf.toString().padStart(2, '0'))
                CoordinateRow("Volume", lastAddress.volume.toString().padStart(2, '0'))
                CoordinateRow("Page", lastAddress.page.toString().padStart(4, '0'))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun CoordinateRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = BabelTextSecondary, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = BabelTextPrimary, style = MaterialTheme.typography.labelMedium)
    }
}
