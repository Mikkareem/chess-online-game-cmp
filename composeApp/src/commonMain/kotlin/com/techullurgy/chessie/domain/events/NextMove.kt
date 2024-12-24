package com.techullurgy.chessie.domain.events

import com.techullurgy.chessie.domain.PieceColor
import com.techullurgy.chessie.domain.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_NEXT_MOVE)
data class NextMove(
    val by: PieceColor,
    val previousMoveBy: PieceColor,
    val previousMoveFrom: Int,
    val previousMoveTo: Int
): ReceiveBaseEvent