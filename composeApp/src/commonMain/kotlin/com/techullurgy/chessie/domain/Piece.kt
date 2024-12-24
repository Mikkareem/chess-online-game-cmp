package com.techullurgy.chessie.domain

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import chessie.composeapp.generated.resources.Res
import chessie.composeapp.generated.resources.bishop
import chessie.composeapp.generated.resources.king
import chessie.composeapp.generated.resources.knight
import chessie.composeapp.generated.resources.pawn
import chessie.composeapp.generated.resources.queen
import chessie.composeapp.generated.resources.rook
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

sealed class Piece(
    private val icon: DrawableResource,
    val pieceColor: PieceColor,
) {
    val content = @Composable {
        Icon(
            modifier = Modifier.fillMaxSize(0.8f),
            painter = painterResource(icon),
            contentDescription = "",
            tint = if(pieceColor == PieceColor.White) Color.White else Color.Black
        )
    }
}


data class Pawn(
    val color: PieceColor
): Piece(Res.drawable.pawn, color)

data class King(
    val color: PieceColor
): Piece(Res.drawable.king, color)

data class Queen(
    val color: PieceColor
): Piece(Res.drawable.queen, color)

data class Bishop(
    val color: PieceColor
): Piece(Res.drawable.bishop, color)

data class Knight(
    val color: PieceColor
): Piece(Res.drawable.knight, color)

data class Rook(
    val color: PieceColor
): Piece(Res.drawable.rook, color)