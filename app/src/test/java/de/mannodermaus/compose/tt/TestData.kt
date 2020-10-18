package de.mannodermaus.compose.tt

import de.mannodermaus.compose.tt.domain.*

val player1 = Player(PlayerId("id1"), "Player 1")
val player2 = Player(PlayerId("id2"), "Player 2")

fun card(north: Int, west: Int, east: Int, south: Int) =
    Card(
        id = CardId("card$north$west$east$south"),
        name = "C$north$west$east$south",
        imageUrl = "https://cards.com/$north$west$east$south.jpg",
        value = cardValuesOf(north, west, east, south)
    )

fun boardWithoutRules(size: Int = 3) = Board.create(size, rules = emptySet())
