package com.techullurgy.chessie.domain

sealed interface GameEvent {
    data object GameLoading: GameEvent
    data class GameReady(
        val board: List<Piece?>
    ): GameEvent

    data class MoveDone(
        val from: Int,
        val to: Int
    ): GameEvent

    data class TimerUpdate(
        val whiteTime: Long,
        val blackTime: Long
    ): GameEvent

    data class ColorAssigned(
        val color: PieceColor
    ): GameEvent

    data class NextMove(
        val by: PieceColor,
        val lastMoveFrom: Int,
        val lastMoveTo: Int,
        val kingCheckIndex: Int,
    ): GameEvent

    data class SelectionResult(
        val availableMoves: List<Int>,
        val selectedIndex: Int
    ): GameEvent

    data object ResetSelection: GameEvent
}