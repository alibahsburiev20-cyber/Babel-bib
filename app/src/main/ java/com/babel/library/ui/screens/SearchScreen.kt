package com.babel.library.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.babel.library.ui.theme.*

@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onSearch: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BabelBackground)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = BabelTextPrimary)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text("Поиск", style = MaterialTheme.typography.titleLarge, color = BabelTextPrimary)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(BabelSurface)
                .border(1.dp, BabelGold, RoundedCornerShape(26.dp))
                .padding(horizontal = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Search, contentDescription = null, tint = BabelGold, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            BasicSearchField(
                value = query,
                onValueChange = { query = it },
                onSearch = { if (query.isNotBlank()) onSearch(query) },
                modifier = Modifier.weight(1f)
            )
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = "" }, modifier = Modifier.size(20.dp)) {
                    Icon(Icons.Filled.Close, contentDescription = "Очистить", tint = BabelTextMuted)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Введите любое слово, имя или фразу — библиотека найдёт (точнее, сгенерирует) книгу, где этот текст встречается.",
            style = MaterialTheme.typography.bodyMedium,
            color = BabelTextSecondary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { if (query.isNotBlank()) onSearch(query) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BabelGold, contentColor = BabelBackground),
            enabled = query.isNotBlank()
        ) {
            Text("Найти", fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
        }
    }
}

@Composable
private fun BasicSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = BabelTextPrimary),
        cursorBrush = androidx.compose.ui.graphics.SolidColor(BabelGold),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch() }),
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text("Поиск по книгам, авторам, темам...", color = BabelTextMuted, style = MaterialTheme.typography.bodyMedium)
            }
            innerTextField()
        }
    )
}
