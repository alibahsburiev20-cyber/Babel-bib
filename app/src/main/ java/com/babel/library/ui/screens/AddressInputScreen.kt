package com.babel.library.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.babel.library.core.Address
import com.babel.library.ui.theme.*

@Composable
fun AddressInputScreen(
    onBack: () -> Unit,
    onNavigate: (Address) -> Unit
) {
    var hexagon by remember { mutableStateOf("7F") }
    var wall by remember { mutableStateOf("A") }
    var shelf by remember { mutableStateOf("00") }
    var volume by remember { mutableStateOf("00") }
    var page by remember { mutableStateOf("0001") }
    var error by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val exampleAddress = "7F-3A-12-06-0042"

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
            Text("Ввод адреса", style = MaterialTheme.typography.titleLarge, color = BabelTextPrimary)
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Введите координаты вручную", style = MaterialTheme.typography.bodyMedium, color = BabelTextSecondary)

        Spacer(modifier = Modifier.height(20.dp))

        AddressField("Hexagon", hexagon) { hexagon = it }
        AddressField("Wall (A-F)", wall) { wall = it.uppercase().take(1) }
        AddressField("Shelf (0-4)", shelf, KeyboardType.Number) { shelf = it }
        AddressField("Volume (0-31)", volume, KeyboardType.Number) { volume = it }
        AddressField("Page (0-409)", page, KeyboardType.Number) { page = it }

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = BabelError, style = MaterialTheme.typography.bodyMedium)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val display = buildDisplayString(hexagon, wall, shelf, volume, page)
                val parsed = Address.parse(display)
                if (parsed == null) {
                    error = "Проверьте корректность координат"
                } else {
                    error = null
                    onNavigate(parsed)
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BabelGold, contentColor = BabelBackground)
        ) {
            Text("Перейти →", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(BabelSurface)
                .border(1.dp, BabelBorder, RoundedCornerShape(14.dp))
                .padding(16.dp)
        ) {
            Text("Пример адреса", color = BabelTextSecondary, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(exampleAddress, color = BabelTextPrimary, style = AddressTextStyle)
                Icon(
                    Icons.Filled.ContentCopy,
                    contentDescription = "Копировать",
                    tint = BabelGold,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { copyToClipboard(context, exampleAddress) }
                )
            }
        }
    }
}

private fun buildDisplayString(hexagon: String, wall: String, shelf: String, volume: String, page: String): String {
    val h = hexagon.ifBlank { "0" }
    val w = wall.ifBlank { "A" }
    val s = shelf.padStart(2, '0').takeLast(2)
    val v = volume.padStart(2, '0').takeLast(2)
    val p = page.padStart(4, '0').takeLast(4)
    return "$h$w-$s-$v-$p"
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("address", text))
}

@Composable
private fun AddressField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = BabelTextSecondary, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.width(120.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BabelGold,
                unfocusedBorderColor = BabelBorder,
                focusedTextColor = BabelTextPrimary,
                unfocusedTextColor = BabelTextPrimary
            )
        )
    }
}
