package de.mannodermaus.compose.tt.domain

sealed class BoardUpdate {
    abstract val row: Int
    abstract val column: Int

    data class Add(
        override val row: Int,
        override val column: Int,
        val tile: BoardTile,
    ) : BoardUpdate()

    data class Takeover(
        override val row: Int,
        override val column: Int,
        val fromPlayer: PlayerId,
        val toPlayer: PlayerId,
    ) : BoardUpdate()
}
