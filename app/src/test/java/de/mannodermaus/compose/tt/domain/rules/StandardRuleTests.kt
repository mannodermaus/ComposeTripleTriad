package de.mannodermaus.compose.tt.domain.rules

import com.google.common.truth.Truth.assertThat
import de.mannodermaus.compose.tt.card
import de.mannodermaus.compose.tt.domain.Board
import de.mannodermaus.compose.tt.domain.BoardTile
import de.mannodermaus.compose.tt.domain.BoardUpdate
import de.mannodermaus.compose.tt.player1
import de.mannodermaus.compose.tt.player2
import de.mannodermaus.compose.tt.util.BoardPrinter
import org.junit.jupiter.api.Test

class StandardRuleTests {

    private val printer = BoardPrinter(player1, player2)

    // ╔═══════╤═══════╤═══════╗
    // ║ ~~~~~ │ ~~~~~ │ ~~~~~ ║
    // ╠═══════╪═══════╪═══════╣
    // ║   1   │       │       ║
    // ║ 4 Ⅰ 5 │       │       ║
    // ║   2   │       │       ║
    // ╟───────┼───────┼───────╢
    // ║       │       │   8   ║
    // ║       │       │ 9 Ⅰ 5 ║
    // ║       │       │   2   ║
    // ╟───────┼───────┼───────╢
    // ║   7   │       │       ║
    // ║ 6 Ⅰ 2 │       │       ║
    // ║   4   │       │       ║
    // ╚═══════╧═══════╧═══════╝
    private val board = Board.create(3)
        .makeMove(0, 0, BoardTile(player1.id, card(1, 4, 5, 2)))
        .makeMove(1, 2, BoardTile(player1.id, card(8, 9, 5, 2)))
        .makeMove(2, 0, BoardTile(player1.id, card(7, 6, 2, 4)))

    @Test
    fun `overtaking a weaker card using West direction`() {
        val tile = BoardTile(player2.id, card(1, 1, 10, 1))
        val newBoard = board.makeMove(1, 1, tile)
        val updates = newBoard.updates

        assertThat(updates).containsExactly(
            BoardUpdate.Takeover(
                1, 2,
                fromPlayer = player1.id,
                toPlayer = player2.id,
            ),
            BoardUpdate.Add(1, 1, tile),
        )

        assertThat(printer.print(newBoard)).isEqualTo(
            """
                ╔═══════╤═══════╤═══════╗
                ║ ~~~~~ │ ~~~~~ │ ~~~~~ ║
                ╠═══════╪═══════╪═══════╣
                ║   1   │       │       ║
                ║ 4 Ⅰ 5 │       │       ║
                ║   2   │       │       ║
                ╟───────┼───────┼───────╢
                ║       │   1   │   8   ║
                ║       │ 1 Ⅱ A │ 9 Ⅱ 5 ║
                ║       │   1   │   2   ║
                ╟───────┼───────┼───────╢
                ║   7   │       │       ║
                ║ 6 Ⅰ 2 │       │       ║
                ║   4   │       │       ║
                ╚═══════╧═══════╧═══════╝
                
            """.trimIndent()
        )
    }

    @Test
    fun `overtaking multiple cards with the same move`() {
        val tile = BoardTile(player2.id, card(5, 1, 1, 9))
        val newBoard = board.makeMove(1, 0, tile)
        val updates = newBoard.updates

        assertThat(updates).containsExactly(
            BoardUpdate.Takeover(
                0, 0,
                fromPlayer = player1.id,
                toPlayer = player2.id,
            ),
            BoardUpdate.Takeover(
                2, 0,
                fromPlayer = player1.id,
                toPlayer = player2.id,
            ),
            BoardUpdate.Add(1, 0, tile),
        )

        assertThat(printer.print(newBoard)).isEqualTo(
            """
                ╔═══════╤═══════╤═══════╗
                ║ ~~~~~ │ ~~~~~ │ ~~~~~ ║
                ╠═══════╪═══════╪═══════╣
                ║   1   │       │       ║
                ║ 4 Ⅱ 5 │       │       ║
                ║   2   │       │       ║
                ╟───────┼───────┼───────╢
                ║   5   │       │   8   ║
                ║ 1 Ⅱ 1 │       │ 9 Ⅰ 5 ║
                ║   9   │       │   2   ║
                ╟───────┼───────┼───────╢
                ║   7   │       │       ║
                ║ 6 Ⅱ 2 │       │       ║
                ║   4   │       │       ║
                ╚═══════╧═══════╧═══════╝
                
            """.trimIndent()
        )
    }
}
