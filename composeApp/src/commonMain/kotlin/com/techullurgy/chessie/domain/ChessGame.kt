package com.techullurgy.chessie.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ChessGame(val currentRoom: String, val userName: String) {

    private val _assignedColor = MutableStateFlow<PieceColor?>(null)
    val assignedColor = _assignedColor.asStateFlow()

    private val _board = MutableStateFlow(emptyList<Piece?>())
    val board = _board.asStateFlow()

    private val _isMyTurn = MutableStateFlow(false)
    val isMyTurn = _isMyTurn.asStateFlow()

    private val _isGameLoading = MutableStateFlow(true)
    val isGameLoading = _isGameLoading.asStateFlow()

    private val _yourTime = MutableStateFlow(0L)
    val yourTime = _yourTime.asStateFlow()

    private val _opponentTime = MutableStateFlow(0L)
    val opponentTime = _opponentTime.asStateFlow()

    private val _availableIndicesForMove = MutableStateFlow(emptyList<Int>())
    val availableIndicesForMove = _availableIndicesForMove.asStateFlow()

    private val _selectedIndexForMove = MutableStateFlow(-1)
    val selectedIndexForMove = _selectedIndexForMove.asStateFlow()

    private val _lastMoveFrom = MutableStateFlow(-1)
    val lastMoveFrom = _lastMoveFrom.asStateFlow()

    private val _lastMoveTo = MutableStateFlow(-1)
    val lastMoveTo = _lastMoveTo.asStateFlow()

    fun gameLoading() {
        _isGameLoading.value = true
    }

    fun colorAssigned(event: GameEvent.ColorAssigned) {
        _assignedColor.value = event.color
    }

    fun gameReady(event: GameEvent.GameReady) {
        _isGameLoading.value = false
        _board.value = event.board
        if(_assignedColor.value == PieceColor.White) {
            _isMyTurn.value = true
        }
        _availableIndicesForMove.value = emptyList()
    }

    fun moveDone(event: GameEvent.MoveDone) {
        val currentBoard = _board.value
        val newBoard = currentBoard.toMutableList().apply {
            val currentPiece = get(event.from)
            set(event.from, null)
            set(event.to, currentPiece)
        }
        _board.value = newBoard
        _selectedIndexForMove.value = -1
        _availableIndicesForMove.value = emptyList()
    }

    fun timerUpdate(event: GameEvent.TimerUpdate) {
        when(_assignedColor.value) {
            PieceColor.Black -> {
                _yourTime.value = event.blackTime
                _opponentTime.value = event.whiteTime
            }
            PieceColor.White -> {
                _yourTime.value = event.whiteTime
                _opponentTime.value = event.blackTime
            }
            null -> {}
        }
    }

    fun nextMove(event: GameEvent.NextMove) {
        _isMyTurn.value = _assignedColor.value == event.by
        _lastMoveFrom.value = event.lastMoveFrom
        _lastMoveTo.value = event.lastMoveTo
    }

    fun selectionResult(event: GameEvent.SelectionResult) {
        _selectedIndexForMove.value = event.selectedIndex
        _availableIndicesForMove.value = event.availableMoves
    }

    fun resetSelection() {
        _selectedIndexForMove.value = -1
        _availableIndicesForMove.value = emptyList()
    }
}