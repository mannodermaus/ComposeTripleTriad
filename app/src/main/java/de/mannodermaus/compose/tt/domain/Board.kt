package de.mannodermaus.compose.tt.domain

import androidx.annotation.IntRange
import de.mannodermaus.compose.tt.domain.rules.BoardRule
import de.mannodermaus.compose.tt.domain.rules.StandardRule

typealias BoardGrid = List<List<BoardTile?>>

private fun emptyGrid(size: Int): BoardGrid =
    MutableList(size) { MutableList<BoardTile?>(size) { null } }

data class BoardTile(
    val playerId: PlayerId,
    val card: Card,
)

data class Move(val row: Int, val column: Int, val tile: BoardTile)

class Board private constructor(
    val size: Int,
    val fields: BoardGrid,
    val rules: List<BoardRule>,
    val updates: List<BoardUpdate>,
) {
    companion object {
        fun create(
            @IntRange(from = 3) size: Int = 3,
            rules: Set<BoardRule> = setOf(StandardRule),
        ) =
            Board(
                size = size,
                fields = emptyGrid(size),
                rules = rules.sortedBy(BoardRule::priority),
                updates = emptyList(),
            )
    }

    // If an element is null, no card has been placed on that field yet
    private val fieldCount = size * size

    val filledFieldCount: Int
        get() = fields.fold(0) { i, column -> i + column.count { it != null } }

    val emptyFieldCount: Int
        get() = fieldCount - filledFieldCount

    operator fun get(row: Int, column: Int): BoardTile? {
        require(isInBounds(row, column))
        return fields[row][column]
    }

    fun makeMove(row: Int, column: Int, tile: BoardTile): Board {
        require(isInBounds(row, column))

        // If the field is already occupied, change nothing.
        // Otherwise, create a new grid and board state without altering the current one
        return if (get(row, column) != null) {
            this
        } else {
            // Apply the different rules attached to the board,
            // populating a list of changes if necessary
            val move = Move(row, column, tile)
            val updates = calculateUpdates(move)

            // Apply the calculated updates to the board and create a new Board object from them
            val newFields = fields.map { it.toMutableList() }.toMutableList()

            for (update in updates) {
                val currentTile = newFields[update.row][update.column]

                when (update) {
                    is BoardUpdate.Add -> {
                        newFields[update.row][update.column] = update.tile
                    }
                    is BoardUpdate.Takeover -> {
                        newFields[update.row][update.column] =
                            currentTile!!.copy(playerId = update.toPlayer)
                    }
                }
            }

            Board(size, newFields, rules, updates)
        }
    }

    fun isInBounds(row: Int, column: Int): Boolean =
        row in 0 until size && column in 0 until size

    /* Private */

    private fun calculateUpdates(move: Move): List<BoardUpdate> {
        // Let all rules get their turn trying to change the board state
        val updates = mutableListOf<BoardUpdate>()
        for (rule in rules) {
            updates.addAll(rule.apply(move, this))
        }

        // Additionally, the actual move triggers an "Add" move as well
        updates.add(
            BoardUpdate.Add(
                row = move.row,
                column = move.column,
                tile = move.tile,
            )
        )

        return updates
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (size != other.size) return false
        if (fields != other.fields) return false
        if (fieldCount != other.fieldCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + fields.hashCode()
        result = 31 * result + fieldCount
        return result
    }

    override fun toString(): String {
        return "Board(size=$size, fields=$fields, fieldCount=$fieldCount)"
    }
}
