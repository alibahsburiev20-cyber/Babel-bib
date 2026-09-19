package com.babel.library

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.babel.library.ui.navigation.BabelNavHost
import com.babel.library.ui.theme.BabelLibraryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BabelLibraryTheme {
                BabelNavHost()
            }
        }
    }
}
