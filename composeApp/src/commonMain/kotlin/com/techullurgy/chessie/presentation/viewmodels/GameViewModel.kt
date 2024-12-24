package com.techullurgy.chessie.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.techullurgy.chessie.GameRoute
import com.techullurgy.chessie.domain.ChessGame
import com.techullurgy.chessie.domain.GameRepository
import com.techullurgy.chessie.domain.Piece
import com.techullurgy.chessie.domain.PieceColor
import com.techullurgy.chessie.domain.events.DestinationSelected
import com.techullurgy.chessie.domain.events.MoveSelection
import com.techullurgy.chessie.domain.events.ResetSelection
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: GameRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val args = savedStateHandle.toRoute<GameRoute>()

    val gameState = callbackFlow {
        var gameState = GameState()

        repository.game.isGameLoading.onEach { isLoading ->
            gameState = gameState.copy(
                loading = isLoading
            )
            send(gameState)
        }.launchIn(this)

        repository.game.yourTime.onEach { time ->
            gameState = gameState.copy(
                yourTime = time
            )
            send(gameState)
        }.launchIn(this)

        repository.game.opponentTime.onEach { time ->
            gameState = gameState.copy(
                opponentTime = time
            )
            send(gameState)
        }.launchIn(this)

        repository.game.assignedColor.onEach { color ->
            gameState = gameState.copy(
                pieceColor = color
            )
            send(gameState)
        }.launchIn(this)

        repository.game.board.onEach { board ->
            gameState = gameState.copy(
                board = board
            )
            send(gameState)
        }.launchIn(this)

        repository.game.isMyTurn.onEach { isMyTurn ->
            gameState = gameState.copy(
                isMyTurn = isMyTurn
            )
            send(gameState)
        }.launchIn(this)

        repository.game.availableIndicesForMove.onEach { indices ->
            gameState = gameState.copy(
                availableIndicesForMove = indices
            )
            send(gameState)
        }.launchIn(this)

        repository.game.selectedIndexForMove.onEach { index ->
            gameState = gameState.copy(
                selectedIndex = index
            )
            send(gameState)
        }.launchIn(this)

        repository.game.lastMoveFrom.onEach { index ->
            gameState = gameState.copy(
                lastMoveFrom = index
            )
            send(gameState)
        }.launchIn(this)

        repository.game.lastMoveTo.onEach { index ->
            gameState = gameState.copy(
                lastMoveTo = index
            )
            send(gameState)
        }.launchIn(this)

        repository.game.kingCheckIndex.onEach { index ->
            gameState = gameState.copy(
                kingCheckIndex = index
            )
            send(gameState)
        }.launchIn(this)

        awaitClose {}
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), GameState())

    init {
        repository.game = ChessGame(args.roomId, args.name)
        viewModelScope.launch {
            repository.observeGameEvents()
        }
    }

    fun onAction(action: GameAction) {
        viewModelScope.launch {
            when(action) {
                is GameAction.OnDestinationSelected -> {
                    val event = DestinationSelected(repository.game.assignedColor.value!!, action.index)
                    repository.sendEvent(event)
                }
                GameAction.OnResetSelection -> repository.sendEvent(ResetSelection)
                is GameAction.OnSelectionDone -> {
                    val event = MoveSelection(repository.game.assignedColor.value!!, action.index)
                    repository.sendEvent(event)
                }
            }
        }
    }
}

data class GameState(
    val board: List<Piece?> = emptyList(),
    val loading: Boolean = true,
    val yourTime: Long = 0L,
    val opponentTime: Long = 0L,

    val opponentLastMoveFrom: Int = -1,
    val opponentLastMoveTo: Int = -1,
    val pieceColor: PieceColor? = null,
    val kingCheckIndex: Int = -1,

    val isMyTurn: Boolean = false,
    val selectedIndex: Int = -1,
    val availableIndicesForMove: List<Int> = emptyList(),
    val lastMoveFrom: Int = -1,
    val lastMoveTo: Int = -1,
)

sealed interface GameAction {
    data class OnSelectionDone(val index: Int): GameAction
    data object OnResetSelection: GameAction
    data class OnDestinationSelected(val index: Int): GameAction
}