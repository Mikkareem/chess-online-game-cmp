package com.techullurgy.chessie.domain

import com.techullurgy.chessie.data.ChessGameApi
import com.techullurgy.chessie.domain.events.SendBaseEvent
import kotlinx.coroutines.flow.onEach

class GameRepository(
    private val api: ChessGameApi
) {
    lateinit var game: ChessGame

    suspend fun sendEvent(event: SendBaseEvent) = api.sendEvent(event)

    suspend fun observeGameEvents() {
        if(!::game.isInitialized) return

        api.joinAndReceiveEventsFor(game.userName, game.currentRoom)
            .onEach {
                when(it) {
                    is GameEvent.ColorAssigned -> game.colorAssigned(it)
                    GameEvent.GameLoading -> game.gameLoading()
                    is GameEvent.GameReady -> game.gameReady(it)
                    is GameEvent.MoveDone -> game.moveDone(it)
                    is GameEvent.TimerUpdate -> game.timerUpdate(it)
                    is GameEvent.NextMove -> game.nextMove(it)
                    is GameEvent.SelectionResult -> game.selectionResult(it)
                    GameEvent.ResetSelection -> game.resetSelection()
                }
            }.collect {}
    }
}