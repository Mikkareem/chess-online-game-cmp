package com.techullurgy.chessie

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techullurgy.chessie.presentation.screens.GameScreen
import com.techullurgy.chessie.presentation.screens.RoomHomeScreen
import com.techullurgy.chessie.presentation.screens.RoomJoinScreen
import kotlinx.serialization.Serializable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier.fillMaxSize().background(Color(0xff25d366)),
            contentAlignment = Alignment.Center
        ) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = RoomRoute,
            ) {
                composable<RoomRoute> {
                    RoomHomeScreen(
                        navigateToRoomJoin = {
                            navController.navigate(RoomJoinRoute(it))
                        }
                    )
                }

                composable<GameRoute> {
                    GameScreen()
                }

                composable<RoomJoinRoute> {
                    RoomJoinScreen(
                        navigateToGameScreen = { roomId, username ->
                            navController.navigate(GameRoute(roomId, username))
                        }
                    )
                }
            }
        }
    }
}

@Serializable
data object RoomRoute

@Serializable
data class GameRoute(
    val roomId: String,
    val name: String,
)

@Serializable
data class RoomJoinRoute(
    val roomId: String
)

