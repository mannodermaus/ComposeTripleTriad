package de.mannodermaus.compose.tt.util

import com.google.common.truth.Truth
import de.mannodermaus.compose.tt.boardWithoutRules
import de.mannodermaus.compose.tt.card
import de.mannodermaus.compose.tt.domain.BoardTile
import de.mannodermaus.compose.tt.player1
import de.mannodermaus.compose.tt.player2
import org.junit.jupiter.api.Test

class BoardPrinterTests {

    private val printer = BoardPrinter(player1, player2)

    @Test
    fun `pretty string works for empty board`() {
        val board = boardWithoutRules()

        Truth.assertThat(printer.print(board)).isEqualTo(
            """
            ╔═══════╤═══════╤═══════╗
            ║ ~~~~~ │ ~~~~~ │ ~~~~~ ║
            ╠═══════╪═══════╪═══════╣
            ║       │       │       ║
            ║       │       │       ║
            ║       │       │       ║
            ╟───────┼───────┼───────╢
            ║       │       │       ║
            ║       │       │       ║
            ║       │       │       ║
            ╟───────┼───────┼───────╢
            ║       │       │       ║
            ║       │       │       ║
            ║       │       │       ║
            ╚═══════╧═══════╧═══════╝
            
        """.trimIndent()
        )
    }

    @Test
    fun `pretty string works for partially filled board`() {
        val board = boardWithoutRules()
            .makeMove(0, 1, BoardTile(player2.id, card(7, 6, 2, 4)))
            .makeMove(1, 1, BoardTile(player1.id, card(6, 6, 1, 3)))
            .makeMove(2, 2, BoardTile(player1.id, card(5, 4, 1, 2)))

        Truth.assertThat(printer.print(board)).isEqualTo(
            """
            ╔═══════╤═══════╤═══════╗
            ║ ~~~~~ │ ~~~~~ │ ~~~~~ ║
            ╠═══════╪═══════╪═══════╣
            ║       │   7   │       ║
            ║       │ 6 Ⅱ 2 │       ║
            ║       │   4   │       ║
            ╟───────┼───────┼───────╢
            ║       │   6   │       ║
            ║       │ 6 Ⅰ 1 │       ║
            ║       │   3   │       ║
            ╟───────┼───────┼───────╢
            ║       │       │   5   ║
            ║       │       │ 4 Ⅰ 1 ║
            ║       │       │   2   ║
            ╚═══════╧═══════╧═══════╝
            
        """.trimIndent()
        )
    }

    @Test
    fun `pretty string works for completely filled board`() {
        val board = boardWithoutRules()
            .makeMove(0, 0, BoardTile(player1.id, card(1, 4, 5, 2)))
            .makeMove(0, 1, BoardTile(player2.id, card(7, 6, 2, 4)))
            .makeMove(0, 2, BoardTile(player1.id, card(9, 8, 4, 3)))
            .makeMove(1, 0, BoardTile(player2.id, card(2, 3, 5, 10)))
            .makeMove(1, 1, BoardTile(player1.id, card(6, 6, 1, 3)))
            .makeMove(1, 2, BoardTile(player2.id, card(7, 1, 1, 3)))
            .makeMove(2, 0, BoardTile(player1.id, card(8, 9, 5, 2)))
            .makeMove(2, 1, BoardTile(player2.id, card(10, 10, 2, 1)))
            .makeMove(2, 2, BoardTile(player1.id, card(5, 4, 1, 2)))

        Truth.assertThat(printer.print(board)).isEqualTo(
            """
            ╔═══════╤═══════╤═══════╗
            ║ ~~~~~ │ ~~~~~ │ ~~~~~ ║
            ╠═══════╪═══════╪═══════╣
            ║   1   │   7   │   9   ║
            ║ 4 Ⅰ 5 │ 6 Ⅱ 2 │ 8 Ⅰ 4 ║
            ║   2   │   4   │   3   ║
            ╟───────┼───────┼───────╢
            ║   2   │   6   │   7   ║
            ║ 3 Ⅱ 5 │ 6 Ⅰ 1 │ 1 Ⅱ 1 ║
            ║   A   │   3   │   3   ║
            ╟───────┼───────┼───────╢
            ║   8   │   A   │   5   ║
            ║ 9 Ⅰ 5 │ A Ⅱ 2 │ 4 Ⅰ 1 ║
            ║   2   │   1   │   2   ║
            ╚═══════╧═══════╧═══════╝
            
            """.trimIndent()
        )
    }
}
