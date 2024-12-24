package com.techullurgy.chessie.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.chessie.data.ChessGameApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomHomeViewModel(
    private val api: ChessGameApi
): ViewModel() {

    private val _state = MutableStateFlow(RoomHomeState())
    val state = _state.asStateFlow()

    fun onAction(action: RoomHomeAction) {
        when(action) {
            is RoomHomeAction.OnRoomCreate -> {
                viewModelScope.launch {
                    val room = api.createRoom(action.name, action.description)
                    room?.let {
                        action.onSuccess(it.id)
                    } ?: let {
                        _state.update {
                            it.copy(
                                error = "Something went wrong"
                            )
                        }
                    }
                }
            }
            is RoomHomeAction.OnRoomSearch -> {
                viewModelScope.launch {
                    val room = api.getRoomDetailsById(action.id)
                    room?.let {
                        action.onSuccess(it.id)
                    } ?: let {
                        _state.update {
                            it.copy(
                                error = "Something went wrong"
                            )
                        }
                    }
                }
            }
            is RoomHomeAction.OnTabChange -> {
                _state.update {
                    it.copy(
                        currentTab = action.index
                    )
                }
            }
        }
    }
}

sealed interface RoomHomeAction {
    data class OnRoomCreate(val name: String, val description: String, val onSuccess: (String) -> Unit): RoomHomeAction
    data class OnRoomSearch(val id: String, val onSuccess: (String) -> Unit): RoomHomeAction
    data class OnTabChange(val index: Int): RoomHomeAction
}

data class RoomHomeState(
    val tabs: List<String> = listOf("Join", "Create"),
    val currentTab: Int = 0,
    val loading: Boolean = false,
    val error: String = ""
)