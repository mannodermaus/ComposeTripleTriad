package de.mannodermaus.compose.tt.util

import com.jakewharton.fliptables.FlipTable
import de.mannodermaus.compose.tt.domain.Board
import de.mannodermaus.compose.tt.domain.Dir
import de.mannodermaus.compose.tt.domain.Player
import de.mannodermaus.compose.tt.domain.PlayerId

private const val OWNER1 = "Ⅰ" // Roman Numeral One, not the letter "I"
private const val OWNER2 = "Ⅱ" // Roman Numeral Two
private const val HEADER = "~~~~~"

class BoardPrinter(private val player1: PlayerId, private val player2: PlayerId) {

    constructor(player1: Player, player2: Player) : this(player1.id, player2.id)

    fun print(board: Board): String {

        // The formatter expects "headers" and "data".
        // Headers are not necessary for our use case, so use a static header instead
        val headers = Array(board.size) { HEADER }

        val data = board.fields.map { row ->
            row.map { field ->
                if (field != null) {
                    val playerMarker = when (field.playerId) {
                        player1 -> OWNER1
                        player2 -> OWNER2
                        else -> throw IllegalArgumentException("did not expect BoardPrinter to get a field owned by '${field.playerId}'")
                    }

                    val card = field.card
                    "  ${card[Dir.North].displayName}  \n" +
                            "${card[Dir.West].displayName} $playerMarker ${card[Dir.East].displayName}\n" +
                            "  ${card[Dir.South].displayName}  "
                } else {
                    // Force three lines regardless
                    " \n \n "
                }
            }.toTypedArray()
        }

        return FlipTable.of(headers, data.toTypedArray()).toString()
    }
}
