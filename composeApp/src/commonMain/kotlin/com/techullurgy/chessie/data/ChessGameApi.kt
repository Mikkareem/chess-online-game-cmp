package com.techullurgy.chessie.data

import com.techullurgy.chessie.domain.Bishop
import com.techullurgy.chessie.domain.GameEvent
import com.techullurgy.chessie.domain.King
import com.techullurgy.chessie.domain.Knight
import com.techullurgy.chessie.domain.Pawn
import com.techullurgy.chessie.domain.PieceColor
import com.techullurgy.chessie.domain.Queen
import com.techullurgy.chessie.domain.Rook
import com.techullurgy.chessie.domain.Room
import com.techullurgy.chessie.domain.events.ColorAssigned
import com.techullurgy.chessie.domain.events.ElapsedTime
import com.techullurgy.chessie.domain.events.GameLoading
import com.techullurgy.chessie.domain.events.GameOver
import com.techullurgy.chessie.domain.events.GameStarted
import com.techullurgy.chessie.domain.events.JoinRoomHandshake
import com.techullurgy.chessie.domain.events.MoveDone
import com.techullurgy.chessie.domain.events.NextMove
import com.techullurgy.chessie.domain.events.ReceiveBaseEvent
import com.techullurgy.chessie.domain.events.ResetSelectionDone
import com.techullurgy.chessie.domain.events.SelectionResult
import com.techullurgy.chessie.domain.events.SendBaseEvent
import com.techullurgy.chessie.domain.events.serializers.receiveBaseEventJson
import com.techullurgy.chessie.domain.events.serializers.sendBaseEventJson
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString

class ChessGameApi(
    private val socketClient: HttpClient,
    private val httpClient: HttpClient,
) {

    private var session: DefaultClientWebSocketSession? = null

    suspend fun getRoomDetailsById(id: String): Room? = withContext(Dispatchers.IO) {
        val response = httpClient.get("$HTTP_BASE_URL/room/$id")

        if(response.status != HttpStatusCode.OK) {
            return@withContext null
        }

        val room = response.body<Room>()
        room
    }

    fun joinAndReceiveEventsFor(name: String, roomId: String): Flow<GameEvent> = channelFlow {
        launch {
            withContext(Dispatchers.IO) {
                try {
                    session = socketClient.webSocketSession(
                        urlString = "$WS_BASE_URL/join/ws"
                    )
                } catch (e: Throwable) {
                    println("No way of joining")
                    close()
                    return@withContext
                }

                sendEvent(JoinRoomHandshake(name, roomId))

                session!!.incoming.consumeEach { frame ->
                    val text = (frame as Frame.Text).readText()
                    val eventReceived = receiveBaseEventJson.decodeFromString<ReceiveBaseEvent>(text)

                    when(eventReceived) {
                        is ColorAssigned -> send(GameEvent.ColorAssigned(eventReceived.assignedColor))
                        is ElapsedTime -> send(
                            GameEvent.TimerUpdate(
                                whiteTime = eventReceived.whiteTime,
                                blackTime = eventReceived.blackTime
                            )
                        )
                        GameLoading -> send(GameEvent.GameLoading)
                        is GameOver -> {
                            close()
                        }
                        is GameStarted -> {
                            val board = eventReceived.boardString.split("***").map {
                                if(it.first() == 'W') {
                                    when(it.last()) {
                                        'K' -> King(PieceColor.White)
                                        'Q' -> Queen(PieceColor.White)
                                        'B' -> Bishop(PieceColor.White)
                                        'R' -> Rook(PieceColor.White)
                                        'N' -> Knight(PieceColor.White)
                                        'P' -> Pawn(PieceColor.White)
                                        else -> null
                                    }
                                } else if (it.first() == 'B') {
                                    when(it.last()) {
                                        'K' -> King(PieceColor.Black)
                                        'Q' -> Queen(PieceColor.Black)
                                        'B' -> Bishop(PieceColor.Black)
                                        'R' -> Rook(PieceColor.Black)
                                        'N' -> Knight(PieceColor.Black)
                                        'P' -> Pawn(PieceColor.Black)
                                        else -> null
                                    }
                                } else {
                                    null
                                }
                            }
                            send(GameEvent.GameReady(board))
                        }
                        is NextMove -> send(
                            GameEvent.NextMove(
                                by = eventReceived.by,
                                lastMoveFrom = eventReceived.previousMoveFrom,
                                lastMoveTo = eventReceived.previousMoveTo
                            )
                        )

                        is MoveDone -> {
                            send(
                                GameEvent.MoveDone(
                                    from = eventReceived.from,
                                    to = eventReceived.to
                                )
                            )
                        }
                        is SelectionResult -> {
                            send(
                                GameEvent.SelectionResult(
                                    availableMoves = eventReceived.availableIndices,
                                    selectedIndex = eventReceived.selectedIndex
                                )
                            )
                        }

                        ResetSelectionDone -> send(GameEvent.ResetSelection)
                    }
                }
            }
        }

        println("Awaiting for close")
        awaitClose {
            println("Start closing ${Clock.System.now().toEpochMilliseconds()}")
            session?.let { s ->
                s.launch {
                    s.close(CloseReason(CloseReason.Codes.NORMAL, "disconnected"))
                }
            }
            session = null
            println("Channel Flow Closed")
        }
    }

    suspend fun sendEvent(event: SendBaseEvent) {
        session?.let {
            if(it.isActive) {
                val message = sendBaseEventJson.encodeToString<SendBaseEvent>(event)
                println("Sending.... $message")
                it.send(Frame.Text(message))
            }
        }
    }

    suspend fun createRoom(name: String, description: String): Room? {
        val roomRequest = Room(id = "", name, description, "Irsath")
        val response = httpClient.post("$HTTP_BASE_URL/create") {
            contentType(ContentType.Application.Json)
            setBody(roomRequest)
        }
        return response.body()
    }

    companion object {
        private const val HOST_AND_PORT = "192.168.225.184:8080"

        private const val HTTP_BASE_URL = "http://$HOST_AND_PORT"
        private const val WS_BASE_URL = "ws://$HOST_AND_PORT"
    }
}