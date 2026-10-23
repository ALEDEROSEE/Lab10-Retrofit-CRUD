package com.tecsup.lab10

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.tecsup.lab10.ui.theme.Lab10Theme
import com.tecsup.lab10.view.SeriesApp

/**
 * Desde Android 17 (API 37) una app que apunta a ese nivel necesita este permiso para hablar con direcciones
 * de red local, y 10.0.2.2 (el servidor Django de mi PC visto desde el emulador) cuenta como red local.
 */
private const val RED_LOCAL = "android.permission.ACCESS_LOCAL_NETWORK"

class MainActivity : ComponentActivity() {

    private val pedirRedLocal = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (checkSelfPermission(RED_LOCAL) != PackageManager.PERMISSION_GRANTED) {
            pedirRedLocal.launch(RED_LOCAL)
        }
        setContent {
            Lab10Theme {
                SeriesApp()
            }
        }
    }
}
