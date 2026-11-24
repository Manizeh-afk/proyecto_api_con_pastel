package com.example.proyecto_api_con_pastel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.proyecto_api_con_pastel.ui.navigation.AppNavigation
import com.example.proyecto_api_con_pastel.ui.theme.Proyecto_api_con_pastelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_api_con_pastelTheme {
                AppNavigation()
            }
        }
    }
}
