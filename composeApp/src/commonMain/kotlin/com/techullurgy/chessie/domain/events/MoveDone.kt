package com.techullurgy.chessie.domain.events

import com.techullurgy.chessie.domain.PieceColor
import com.techullurgy.chessie.domain.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_MOVE_DONE)
data class MoveDone (
    val by: PieceColor,
    val from: Int,
    val to: Int,
): ReceiveBaseEvent