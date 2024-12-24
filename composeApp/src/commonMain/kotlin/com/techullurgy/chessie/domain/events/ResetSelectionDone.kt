package com.techullurgy.chessie.domain.events

import com.techullurgy.chessie.domain.events.constants.BaseEventConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName(BaseEventConstants.TYPE_RESET_SELECTION_DONE)
data object ResetSelectionDone: ReceiveBaseEvent