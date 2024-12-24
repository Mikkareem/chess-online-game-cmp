package com.techullurgy.chessie.domain

import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: String,
    val name: String,
    val description: String,
    val createdBy: String,
)
