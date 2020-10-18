package de.mannodermaus.compose.tt.domain.rules

import de.mannodermaus.compose.tt.domain.Board
import de.mannodermaus.compose.tt.domain.Move
import de.mannodermaus.compose.tt.domain.BoardUpdate

/**
 * The default board rule for the game.
 * When a card is put down, it will take over neighboring, opposing cards with lower values.
 */
object StandardRule : BoardRule(priority = 1) {

    override fun apply(move: Move, board: Board): List<BoardUpdate> {
        val neighbors = neighborsOf(move, board)
        val moveCard = move.tile.card
        val updates = mutableListOf<BoardUpdate>()
        for ((dir, row, column) in neighbors) {
            val neighbor = board[row, column] ?: throw IllegalArgumentException()
            if (neighbor.playerId == move.tile.playerId) {
                // For standard rule, the player's own cards do not matter
                continue
            }

            // Challenge the neighbor's value and cause an update if the new card wins
            if (moveCard[dir] > neighbor.card[dir.opposite]) {
                updates.add(
                    BoardUpdate.Takeover(
                        row = row,
                        column = column,
                        fromPlayer = neighbor.playerId,
                        toPlayer = move.tile.playerId,
                    )
                )
            }
        }

        return updates
    }
}
