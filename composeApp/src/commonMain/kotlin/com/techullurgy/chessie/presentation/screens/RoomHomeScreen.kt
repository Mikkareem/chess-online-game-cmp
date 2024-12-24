package com.techullurgy.chessie.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.chessie.presentation.viewmodels.RoomHomeAction
import com.techullurgy.chessie.presentation.viewmodels.RoomHomeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RoomHomeScreen(
    navigateToRoomJoin: (String) -> Unit
) {
    val viewModel = koinViewModel<RoomHomeViewModel>()

    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .widthIn(max = 350.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TabRow(
                selectedTabIndex = state.currentTab
            ) {
                state.tabs.forEachIndexed { index, it ->
                    Tab(
                        selected = state.currentTab == index,
                        onClick = { viewModel.onAction(RoomHomeAction.OnTabChange(index)) },
                        text = {
                            Text(it, fontWeight = FontWeight.ExtraBold)
                        }
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            when(state.currentTab) {
                0 -> {
                    JoinForm {
                        viewModel.onAction(
                            RoomHomeAction.OnRoomSearch(it, onSuccess = navigateToRoomJoin)
                        )
                    }
                }
                1 -> {
                    NewRoomForm { roomName, roomDescription ->
                        viewModel.onAction(
                            RoomHomeAction.OnRoomCreate(roomName, roomDescription, onSuccess = navigateToRoomJoin)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun JoinForm(
    onSubmit: (roomId: String) -> Unit
) {
    var roomId by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = roomId,
            onValueChange = { roomId = it },
            label = {
                Text("Room Id")
            }
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = { onSubmit(roomId) }
        ) {
            Text("Search & Join Room")
        }
    }
}

@Composable
private fun NewRoomForm(
    onSubmit: (title: String, description: String) -> Unit
) {
    var roomTitle by remember { mutableStateOf("") }
    var roomDescription by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = roomTitle,
            onValueChange = { roomTitle = it }
        )
        OutlinedTextField(
            value = roomDescription,
            onValueChange = { roomDescription = it }
        )
        Button(
            onClick = { onSubmit(roomTitle, roomDescription) }
        ) {
            Text("Create New Room")
        }
    }
}