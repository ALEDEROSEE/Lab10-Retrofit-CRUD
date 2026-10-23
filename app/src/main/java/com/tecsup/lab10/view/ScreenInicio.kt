package com.tecsup.lab10.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/** La guía navega a "inicio" pero nunca define esta pantalla. */
@Composable
fun ScreenInicio() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("INICIO", style = MaterialTheme.typography.headlineMedium)
        Text("CRUD de series por Retrofit")
    }
}
