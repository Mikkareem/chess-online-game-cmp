package com.techullurgy.chessie.di

import com.techullurgy.chessie.data.ChessGameApi
import com.techullurgy.chessie.domain.GameRepository
import com.techullurgy.chessie.presentation.viewmodels.GameViewModel
import com.techullurgy.chessie.presentation.viewmodels.RoomHomeViewModel
import com.techullurgy.chessie.presentation.viewmodels.RoomJoinViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.generateNonce
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val Intercept = createClientPlugin("Intercept") {
    onRequest { request, _ ->
        request.parameter("client_id", generateNonce())
    }
}

val sharedModule = module {
    viewModelOf(::GameViewModel)
    viewModelOf(::RoomHomeViewModel)
    viewModelOf(::RoomJoinViewModel)

    single<GameRepository> { GameRepository(get()) }

    single<HttpClient>(named("Websocket")) {
        HttpClient {
            install(Intercept)
            install(HttpCookies)
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(Json)
                pingIntervalMillis = 20_000
            }
        }
    }

    single<HttpClient>(named("Http")) {
        HttpClient {
            install(Intercept)
            install(HttpCookies)
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single<ChessGameApi> {
        ChessGameApi(
            socketClient = get(named("Websocket")),
            httpClient = get(named("Http"))
        )
    }
}