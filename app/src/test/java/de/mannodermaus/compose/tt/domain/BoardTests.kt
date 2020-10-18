package de.mannodermaus.compose.tt.domain

import com.google.common.truth.Truth.assertThat
import de.mannodermaus.compose.tt.boardWithoutRules
import de.mannodermaus.compose.tt.card
import de.mannodermaus.compose.tt.player1
import de.mannodermaus.compose.tt.player2
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class BoardTests {

    private val size = 3
    private val card: Card = card(1, 1, 1, 1)

    @Test
    fun `is empty after creation`() {
        val board = boardWithoutRules()

        assertThat(board.filledFieldCount).isEqualTo(0)
        assertThat(board.emptyFieldCount).isEqualTo(size * size)
    }

    @Test
    fun `get returns null on empty field`() {
        val board = boardWithoutRules()

        for (row in 0 until size) {
            for (column in 0 until size) {
                assertThat(board[row, column]).isNull()
            }
        }
    }

    @Test
    fun `get throws if out of bounds`() {
        val board = boardWithoutRules()

        assertThrows<IllegalArgumentException> { board[-1, 0] }
        assertThrows<IllegalArgumentException> { board[0, -1] }
        assertThrows<IllegalArgumentException> { board[100, 0] }
        assertThrows<IllegalArgumentException> { board[0, 100] }
    }

    @Test
    fun `set throws if out of bounds`() {
        val board = boardWithoutRules()
        val move = BoardTile(player1.id, card)

        assertThrows<IllegalArgumentException> { board.makeMove(-1, 0, move) }
        assertThrows<IllegalArgumentException> { board.makeMove(0, -1, move) }
        assertThrows<IllegalArgumentException> { board.makeMove(100, 0, move) }
        assertThrows<IllegalArgumentException> { board.makeMove(0, 100, move) }
    }

    @Test
    fun `set a move on an empty field`() {
        val board = boardWithoutRules()
        val newBoard = board.makeMove(0, 0, BoardTile(player1.id, card))

        // The original board should not have been altered
        assertThat(board).isNotEqualTo(newBoard)
        assertThat(board[0, 0]).isNull()

        // The new board should have the move
        assertThat(newBoard[0, 0]).isEqualTo(BoardTile(player1.id, card))
    }

    @Test
    fun `set a move on an occupied field`() {
        val board = boardWithoutRules().makeMove(0, 0, BoardTile(player1.id, card))
        val newBoard = board.makeMove(0, 0, BoardTile(player2.id, card))

        // Nothing should have changed
        assertThat(board).isEqualTo(newBoard)
        assertThat(board).isSameInstanceAs(newBoard)

        // The old move should've persisted
        assertThat(newBoard[0, 0]).isEqualTo(BoardTile(player1.id, card))
    }
}
