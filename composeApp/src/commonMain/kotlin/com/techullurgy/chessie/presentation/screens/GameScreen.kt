package com.techullurgy.chessie.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.techullurgy.chessie.presentation.components.ChessBoard
import com.techullurgy.chessie.presentation.viewmodels.GameAction
import com.techullurgy.chessie.presentation.viewmodels.GameViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameScreen() {
    val viewModel = koinViewModel<GameViewModel>()

    val state by viewModel.gameState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = state.loading
    ) { isLoading ->
        if(isLoading) {
            Box(Modifier.fillMaxSize(), Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Box(Modifier.fillMaxSize()) {
                ChessBoard(
                    board = state.board,
                    isMyTurn = state.isMyTurn,
                    assignedColor = state.pieceColor!!,
                    timeString = "${state.yourTime}::${state.opponentTime}",
                    availableIndicesForMove = state.availableIndicesForMove,
                    lastMoveFrom = state.lastMoveFrom,
                    lastMoveTo = state.lastMoveTo,
                    selectedIndex = state.selectedIndex,
                    onCellSelected = { viewModel.onAction(GameAction.OnSelectionDone(it)) },
                    onDestinationSelected = { viewModel.onAction(GameAction.OnDestinationSelected(it)) },
                    onResetSelection = { viewModel.onAction(GameAction.OnResetSelection) }
                )
            }
        }
    }
}