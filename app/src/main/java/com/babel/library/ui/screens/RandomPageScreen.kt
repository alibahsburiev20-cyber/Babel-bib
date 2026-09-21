package com.babel.library.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.babel.library.core.Address
import com.babel.library.ui.theme.*
import kotlinx.coroutines.delay

private data class GenStep(val label: String, val value: String)

@Composable
fun RandomPageScreen(
    address: Address,
    onBack: () -> Unit,
    onGenerationComplete: () -> Unit
) {
    var generating by remember { mutableStateOf(true) }
    val steps = remember(address) {
        listOf(
            GenStep("Выбор hexagon...", address.hexagon.toString(16).uppercase()),
            GenStep("Выбор wall...", ('A' + address.wall).toString()),
            GenStep("Выбор shelf...", address.shelf.toString().padStart(2, '0')),
            GenStep("Выбор volume...", address.volume.toString().padStart(2, '0')),
            GenStep("Выбор page...", address.page.toString().padStart(4, '0'))
        )
    }
    var visibleSteps by remember { mutableStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "spin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "rotation"
    )

    LaunchedEffect(address) {
        visibleSteps = 0
        for (i in steps.indices) {
            delay(280)
            visibleSteps = i + 1
        }
        delay(400)
        generating = false
        delay(500)
        onGenerationComplete()
    }

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
            Text("Случайная страница", style = MaterialTheme.typography.titleLarge, color = BabelTextPrimary)
        }

        Column(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .border(1.5.dp, BabelGold, CircleShape)
                    .rotate(if (generating) rotation else 0f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.MenuBook,
                    contentDescription = null,
                    tint = BabelGold,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                if (generating) "Генерация страницы..." else "Готово",
                style = MaterialTheme.typography.titleMedium,
                color = BabelTextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Случайная книга. Случайная эпоха.\nСлучайная жизнь.",
                style = MaterialTheme.typography.bodyMedium,
                color = BabelTextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Column(horizontalAlignment = Alignment.Start) {
                steps.forEachIndexed { index, step ->
                    if (index < visibleSteps) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text("> ", color = BabelGoldDim, style = MaterialTheme.typography.labelMedium)
                            Text(
                                step.label,
                                color = BabelTextSecondary,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.width(140.dp)
                            )
                            Text(step.value, color = BabelTextPrimary, style = MaterialTheme.typography.labelMedium)
                            Spacer(modifier = Modifier.width(8.dp))
                            if (index < visibleSteps - 1 || !generating) {
                                Icon(Icons.Filled.Check, contentDescription = null, tint = BabelSuccess, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}
