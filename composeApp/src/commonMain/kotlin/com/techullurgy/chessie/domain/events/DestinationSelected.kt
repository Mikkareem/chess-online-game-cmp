package com.techullurgy.chessie.domain.events

import com.techullurgy.chessie.domain.PieceColor
import com.techullurgy.chessie.domain.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_PIECE_DESTINATION_SELECTION_DONE)
data class DestinationSelected(
    val color: PieceColor,
    val destinationIndex: Int
): SendBaseEvent