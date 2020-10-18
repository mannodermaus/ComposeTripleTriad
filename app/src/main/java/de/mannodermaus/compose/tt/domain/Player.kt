package de.mannodermaus.compose.tt.domain

inline class PlayerId(val value: String)

data class Player(
    val id: PlayerId,
    val name: String,
)
