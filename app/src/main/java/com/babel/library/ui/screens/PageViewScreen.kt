package com.babel.library.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.babel.library.core.Address
import com.babel.library.core.Library
import com.babel.library.ui.theme.*

@Composable
fun PageViewScreen(
    address: Address,
    highlightRange: IntRange? = null,
    isBookmarked: Boolean,
    onBack: () -> Unit,
    onToggleBookmark: () -> Unit,
    onPrevPage: () -> Unit,
    onNextPage: () -> Unit
) {
    val title = remember(address) { Library.generateTitle(address) }
    val author = remember(address) { Library.generateAuthor(address) }
    val pageText = remember(address) { Library.generatePage(address) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BabelBackground)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = BabelTextPrimary)
            }
            Text("Страница книги", style = MaterialTheme.typography.titleLarge, color = BabelTextPrimary)
            Row {
                IconButton(onClick = { /* share stub */ }) {
                    Icon(Icons.Filled.Share, contentDescription = "Поделиться", tint = BabelTextSecondary)
                }
                IconButton(onClick = onToggleBookmark) {
                    Icon(
                        if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                        contentDescription = "Закладка",
                        tint = if (isBookmarked) BabelGold else BabelTextSecondary
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(BabelPaperBackground)
                .padding(24.dp)
        ) {
            Text(author, color = BabelPaperMuted, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                title.replaceFirstChar { it.uppercase() },
                color = BabelPaperText,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Том · Стр. ${address.page + 1}",
                color = BabelPaperMuted,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Текст страницы прокручивается отдельно от заголовка,
            // чтобы все 3200 символов были доступны, а шапка книги оставалась на месте.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                HighlightedPageText(text = pageText, highlightRange = highlightRange)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                address.toDisplayString(),
                color = BabelPaperMuted,
                style = AddressTextStyle
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrevPage) {
                Icon(Icons.Filled.ChevronLeft, contentDescription = "Предыдущая", tint = BabelTextPrimary)
            }
            Text(
                "Страница ${address.page + 1} из 410",
                color = BabelTextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
            IconButton(onClick = onNextPage) {
                Icon(Icons.Filled.ChevronRight, contentDescription = "Следующая", tint = BabelTextPrimary)
            }
        }
    }
}

@Composable
private fun HighlightedPageText(text: String, highlightRange: IntRange?) {
    if (highlightRange == null) {
        Text(text, style = PageTextStyle, color = BabelPaperText)
        return
    }
    val safeEnd = highlightRange.last.coerceAtMost(text.length)
    val safeStart = highlightRange.first.coerceIn(0, safeEnd)
    val annotated = buildAnnotatedString {
        append(text.substring(0, safeStart))
        withStyle(SpanStyle(color = BabelGoldDim, fontWeight = FontWeight.Bold)) {
            append(text.substring(safeStart, safeEnd))
        }
        append(text.substring(safeEnd))
    }
    Text(annotated, style = PageTextStyle, color = BabelPaperText)
}
