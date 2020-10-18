package de.mannodermaus.compose.tt.screens.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.mannodermaus.compose.tt.domain.*

class GameViewModel(
    private val player1: Player,
    private val player2: Player,
) {

    private var board: Board = Board.create(size = 3)
    private var currentPlayerId: PlayerId = player1.id

    var boardTileMovements by mutableStateOf<List<BoardUpdate>>(emptyList())
        private set

    fun makeMove(playerId: PlayerId, card: Card, row: Int, column: Int) {
        if (playerId != currentPlayerId) {
            // Not your turn, bro
            return
        }

        // Apply the move and check if anything changed
        val oldBoard = board
        val newBoard = board.makeMove(row, column, BoardTile(playerId, card))
        this.board = newBoard

        if (oldBoard == newBoard) {
            // Nothing changed
            return
        }

        // Update state, notifying downstream listeners to render changes
        boardTileMovements = newBoard.updates
    }
}
