package com.techullurgy.chessie.domain.events

import com.techullurgy.chessie.domain.PieceColor
import com.techullurgy.chessie.domain.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_COLOR_ASSIGNED)
data class ColorAssigned(
    val assignedColor: PieceColor
): ReceiveBaseEvent
