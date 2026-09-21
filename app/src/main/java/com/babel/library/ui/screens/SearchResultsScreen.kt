package com.babel.library.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babel.library.core.SearchEncoder
import com.babel.library.ui.theme.*

@Composable
fun SearchResultsScreen(
    query: String,
    results: List<SearchEncoder.SearchResult>,
    onBack: () -> Unit,
    onOpenResult: (SearchEncoder.SearchResult) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BabelBackground)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = BabelTextPrimary)
            }
            Column {
                Text("Результаты", style = MaterialTheme.typography.titleLarge, color = BabelTextPrimary)
                Text("«$query»", style = MaterialTheme.typography.bodyMedium, color = BabelGold)
            }
        }

        if (results.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Text(
                    "Ничего не сконструировано. Попробуйте другой текст.",
                    color = BabelTextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results) { result ->
                    ResultCard(query = query, result = result, onClick = { onOpenResult(result) })
                }
            }
        }
    }
}

@Composable
private fun ResultCard(
    query: String,
    result: SearchEncoder.SearchResult,
    onClick: () -> Unit
) {
    val contextRadius = 40
    val start = (result.matchStart - contextRadius).coerceAtLeast(0)
    val end = (result.matchEnd + contextRadius).coerceAtMost(result.page.length)
    val before = result.page.substring(start, result.matchStart)
    val match = result.page.substring(result.matchStart, result.matchEnd)
    val after = result.page.substring(result.matchEnd, end)

    val previewText = buildAnnotatedString {
        append("…")
        append(before)
        withStyle(SpanStyle(color = BabelGold, fontWeight = FontWeight.Bold)) {
            append(match)
        }
        append(after)
        append("…")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BabelSurface)
            .border(1.dp, BabelBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                result.address.toDisplayString(),
                style = MaterialTheme.typography.labelMedium,
                color = BabelGoldDim
            )
            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = BabelTextMuted)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            previewText,
            style = MaterialTheme.typography.bodyMedium,
            color = BabelTextSecondary,
            maxLines = 3
        )
    }
}
