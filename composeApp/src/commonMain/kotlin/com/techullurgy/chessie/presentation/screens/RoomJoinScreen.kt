package com.techullurgy.chessie.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.chessie.presentation.viewmodels.RoomJoinViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RoomJoinScreen(
    navigateToGameScreen: (String, String) -> Unit
) {
    val viewModel = koinViewModel<RoomJoinViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    AnimatedContent(state.room) { room ->
        if(room == null) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("No Room found")
            }
        } else {
            Box(
                Modifier.fillMaxSize(),
                Alignment.Center
            ) {
                var username by remember { mutableStateOf("") }

                Column {
                    // Image
                    Box(Modifier.size(80.dp).clip(CircleShape).background(Color.Green))
                    Text(room.name)
                    Text(room.id)
                    Text(room.description)
                    Text(room.createdBy)


                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                    )
                }

                Button(
                    onClick = { navigateToGameScreen(room.id, username) },
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    Text("Join Game")
                }
            }
        }
    }
}