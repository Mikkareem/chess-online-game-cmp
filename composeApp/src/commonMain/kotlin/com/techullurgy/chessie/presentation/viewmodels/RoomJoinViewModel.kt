package com.techullurgy.chessie.presentation.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.techullurgy.chessie.RoomJoinRoute
import com.techullurgy.chessie.data.ChessGameApi
import com.techullurgy.chessie.domain.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomJoinViewModel(
    private val api: ChessGameApi,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val args = savedStateHandle.toRoute<RoomJoinRoute>()

    private val _state = MutableStateFlow(RoomJoinScreenState())
    val state: StateFlow<RoomJoinScreenState> = _state
        .onStart {
            viewModelScope.launch {
                val room = api.getRoomDetailsById(args.roomId)
                room?.let {  _room ->
                    _state.update {
                        it.copy(room = _room)
                    }
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _state.value)
}

data class RoomJoinScreenState(
    val room: Room? = null
)

sealed interface RoomJoinAction {
//    data object OnJoinClick: RoomJoinAction
}