package com.techullurgy.chessie

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.techullurgy.chessie.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState().apply {
                size = DpSize(500.dp, 500.dp)
            },
            title = "Chessie",
        ) {
            App()
        }
    }
}