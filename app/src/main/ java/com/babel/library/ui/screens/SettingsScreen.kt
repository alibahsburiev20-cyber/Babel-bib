package com.babel.library.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.babel.library.ui.theme.*

@Composable
fun SettingsScreen(
    guaranteeWord: Boolean,
    onGuaranteeWordChange: (Boolean) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BabelBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = BabelTextPrimary)
            }
            Text("Настройки", style = MaterialTheme.typography.titleLarge, color = BabelTextPrimary)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(BabelSurface)
                .border(1.dp, BabelBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Гарантировать слово", color = BabelTextPrimary, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "При случайной генерации всегда добавлять хотя бы одно настоящее слово вместо чистого шума",
                        color = BabelTextSecondary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Switch(
                    checked = guaranteeWord,
                    onCheckedChange = onGuaranteeWordChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = BabelGold,
                        checkedTrackColor = BabelGoldDim
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("О библиотеке", color = BabelGoldDim, style = MaterialTheme.typography.labelSmall)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Библиотека Вавилона — реализация концепции Хорхе Луиса Борхеса. " +
                "Каждая страница вычисляется детерминированно по адресу: один и тот же адрес " +
                "всегда возвращает одно и то же содержимое. Библиотека не хранит книги — она " +
                "их порождает по требованию из огромного, но конечного пространства комбинаций.",
            color = BabelTextSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
