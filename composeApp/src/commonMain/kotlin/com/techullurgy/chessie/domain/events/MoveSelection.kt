package com.techullurgy.chessie.domain.events

import com.techullurgy.chessie.domain.PieceColor
import com.techullurgy.chessie.domain.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_SELECTION_FOR_MOVE_DONE)
data class MoveSelection(
    val color: PieceColor,
    val selectedIndex: Int
): SendBaseEvent