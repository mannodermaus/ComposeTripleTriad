package de.mannodermaus.compose.tt.domain.rules

import de.mannodermaus.compose.tt.domain.Board
import de.mannodermaus.compose.tt.domain.BoardUpdate
import de.mannodermaus.compose.tt.domain.Dir
import de.mannodermaus.compose.tt.domain.Move

typealias BoardNeighbor = Triple<Dir, Int, Int>

abstract class BoardRule(val priority: Int) {

    /**
     * Calculate the list of changes to the given [board]
     * if the provided [move] would be made.
     */
    abstract fun apply(move: Move, board: Board): List<BoardUpdate>

    protected fun neighborsOf(move: Move, board: Board): List<BoardNeighbor> {
        return listOf(
            // All possible combinations, regardless of validity
            Triple(Dir.North, move.row - 1, move.column),
            Triple(Dir.West, move.row, move.column - 1),
            Triple(Dir.East, move.row, move.column + 1),
            Triple(Dir.South, move.row + 1, move.column),
        ).mapNotNull { (dir, row, column) ->
            // Filter invalid, out-of-bounds cells
            if (board.isInBounds(row, column)) {
                // Filter empty cells
                val existing = board[row, column]
                if (existing != null) {
                    BoardNeighbor(dir, row, column)
                } else {
                    null
                }
            } else {
                null
            }
        }
    }
}
