package com.techullurgy.chessie.domain.events.serializers

import com.techullurgy.chessie.domain.events.ColorAssigned
import com.techullurgy.chessie.domain.events.DestinationSelected
import com.techullurgy.chessie.domain.events.Disconnected
import com.techullurgy.chessie.domain.events.ElapsedTime
import com.techullurgy.chessie.domain.events.GameLoading
import com.techullurgy.chessie.domain.events.GameOver
import com.techullurgy.chessie.domain.events.GameStarted
import com.techullurgy.chessie.domain.events.JoinRoomHandshake
import com.techullurgy.chessie.domain.events.MoveDone
import com.techullurgy.chessie.domain.events.MoveSelection
import com.techullurgy.chessie.domain.events.NextMove
import com.techullurgy.chessie.domain.events.ReceiveBaseEvent
import com.techullurgy.chessie.domain.events.ResetSelection
import com.techullurgy.chessie.domain.events.ResetSelectionDone
import com.techullurgy.chessie.domain.events.SelectionResult
import com.techullurgy.chessie.domain.events.SendBaseEvent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val sendBaseEventJson = Json {
    serializersModule = SerializersModule {
        polymorphic(SendBaseEvent::class, JoinRoomHandshake::class, JoinRoomHandshake.serializer())
        polymorphic(SendBaseEvent::class, MoveSelection::class, MoveSelection.serializer())
        polymorphic(SendBaseEvent::class, DestinationSelected::class, DestinationSelected.serializer())
        polymorphic(SendBaseEvent::class, ResetSelection::class, ResetSelection.serializer())
        polymorphic(SendBaseEvent::class, Disconnected::class, Disconnected.serializer())
    }
}

val receiveBaseEventJson = Json {
    serializersModule = SerializersModule {
        polymorphic(ReceiveBaseEvent::class, MoveDone::class, MoveDone.serializer())
        polymorphic(ReceiveBaseEvent::class, ColorAssigned::class, ColorAssigned.serializer())
        polymorphic(ReceiveBaseEvent::class, ElapsedTime::class, ElapsedTime.serializer())
        polymorphic(ReceiveBaseEvent::class, GameLoading::class, GameLoading.serializer())
        polymorphic(ReceiveBaseEvent::class, GameOver::class, GameOver.serializer())
        polymorphic(ReceiveBaseEvent::class, GameStarted::class, GameStarted.serializer())
        polymorphic(ReceiveBaseEvent::class, NextMove::class, NextMove.serializer())
        polymorphic(ReceiveBaseEvent::class, SelectionResult::class, SelectionResult.serializer())
        polymorphic(ReceiveBaseEvent::class, ResetSelectionDone::class, ResetSelectionDone.serializer())
    }
}