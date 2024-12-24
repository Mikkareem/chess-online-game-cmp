package com.techullurgy.chessie.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Constraints
import com.techullurgy.chessie.domain.Piece
import com.techullurgy.chessie.domain.PieceColor
import kotlin.math.min

@Composable
fun ChessBoard(
    board: List<Piece?>,
    isMyTurn: Boolean,
    assignedColor: PieceColor,
    timeString: String,
    selectedIndex: Int,
    availableIndicesForMove: List<Int>,
    lastMoveFrom: Int,
    lastMoveTo: Int,
    kingCheckIndex: Int,
    onCellSelected: (Int) -> Unit,
    onDestinationSelected: (Int) -> Unit,
    onResetSelection: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Text(timeString)
        LazyVerticalGrid(
            columns = GridCells.Fixed(8),
            modifier = modifier
                .layout { measurable, constraints ->
                    val cellSize = min(constraints.maxWidth, constraints.maxHeight)
                    val placeable = measurable.measure(
                        Constraints(
                            minWidth = cellSize,
                            maxWidth = cellSize,
                            minHeight = cellSize,
                            maxHeight = cellSize
                        )
                    )
                    layout(placeable.width, placeable.height) {
                        placeable.place(0, 0)
                    }
                },
            userScrollEnabled = false
        ) {
            itemsIndexed(board) { index, item ->
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .background(
                            if(index == selectedIndex) {
                                Color.Blue
                            } else if(index == kingCheckIndex) {
                                Color.Red
                            } else if (availableIndicesForMove.contains(index)) {
                                Color.Magenta
                            } else if ((index/8) % 2 == 0) {
                                if (index % 2 == 0) {
                                    Color(0xff6681b2)
                                } else {
                                    Color(0xffb0c2e1)
                                }
                            } else {
                                if (index % 2 == 0) {
                                    Color(0xffb0c2e1)
                                } else {
                                    Color(0xff6681b2)
                                }
                            }
                        )
                        .drawBehind {
                            if(index == lastMoveFrom) {
                                lastMoveFromIndicator(Color.Blue)
                            }
                            if(index == lastMoveTo) {
                                lastMoveToIndicator(Color.Blue)
                            }
                        }
                        .clickable(
                            enabled = isMyTurn && (availableIndicesForMove.contains(index) || item?.pieceColor == assignedColor)
                        ) {
                            if(availableIndicesForMove.contains(index)) {
                                onDestinationSelected(index)
                            } else if(selectedIndex == index) {
                                onResetSelection()
                            } else {
                                onCellSelected(index)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    item?.content?.let { it() }
                }
            }
        }
    }
}

private fun DrawScope.lastMoveFromIndicator(color: Color) {
    val path = Path().apply {
        val padding = size.width * 0.1f
        moveTo(padding, padding)
        cubicTo(
            center.x - padding*2f,
            center.y + padding*2f,
            center.x + padding*2f,
            center.y - padding*2f,
            padding,
            padding
        )
    }
    rotate(0f) { drawPath(path, color) }
    rotate(90f) { drawPath(path, color) }
    rotate(180f) { drawPath(path, color) }
    rotate(270f) { drawPath(path, color) }
}

private fun DrawScope.lastMoveToIndicator(color: Color) {
    val path = Path().apply {
        val padding = size.width * 0.1f
        moveTo(padding, padding)
        lineTo(padding, center.y)
        quadraticTo(
            padding,
            padding,
            center.x,
            padding
        )
        close()
    }
    rotate(0f) { drawPath(path, color) }
    rotate(90f) { drawPath(path, color) }
    rotate(180f) { drawPath(path, color) }
    rotate(270f) { drawPath(path, color) }
}