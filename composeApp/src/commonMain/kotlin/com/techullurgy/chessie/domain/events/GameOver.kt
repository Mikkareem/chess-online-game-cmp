package com.techullurgy.chessie.domain.events

import com.techullurgy.chessie.domain.PieceColor
import com.techullurgy.chessie.domain.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_GAME_OVER)
data class GameOver(
    val winner: PieceColor
): ReceiveBaseEvent