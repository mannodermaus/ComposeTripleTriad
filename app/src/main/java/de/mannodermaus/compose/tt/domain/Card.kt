package de.mannodermaus.compose.tt.domain

import androidx.annotation.IntRange
import java.util.*

private const val LOWEST_VALUE: Long = 1
private const val HIGHEST_VALUE: Long = 10

data class Deck(private val cards: List<Card>)

inline class CardId(val value: String)

data class Card(
    val id: CardId,
    val name: String,
    val imageUrl: String,
    private val value: CardValues,
) {
    operator fun get(dir: Dir): CardValue =
        dir.get(this.value)
}

inline class CardValues(val value: Int)

fun cardValuesOf(
    @IntRange(from = LOWEST_VALUE, to = HIGHEST_VALUE) north: Int,
    @IntRange(from = LOWEST_VALUE, to = HIGHEST_VALUE) west: Int,
    @IntRange(from = LOWEST_VALUE, to = HIGHEST_VALUE) east: Int,
    @IntRange(from = LOWEST_VALUE, to = HIGHEST_VALUE) south: Int,
): CardValues = Dir.cardValue(north, west, east, south)

inline class CardValue(val value: Int) : Comparable<CardValue> {
    val displayName: String
        get() = value.toString(radix = 16).toUpperCase(Locale.ROOT)

    override fun compareTo(other: CardValue): Int =
        this.value.compareTo(other.value)
}

enum class Dir(private val shiftBits: Int, private val vertical: Boolean) {
    North(shiftBits = 12, vertical = true),
    West(shiftBits = 8, vertical = false),
    East(shiftBits = 4, vertical = false),
    South(shiftBits = 0, vertical = true);

    companion object {
        fun cardValue(
            @IntRange(from = LOWEST_VALUE, to = HIGHEST_VALUE) north: Int,
            @IntRange(from = LOWEST_VALUE, to = HIGHEST_VALUE) west: Int,
            @IntRange(from = LOWEST_VALUE, to = HIGHEST_VALUE) east: Int,
            @IntRange(from = LOWEST_VALUE, to = HIGHEST_VALUE) south: Int
        ) = CardValues(
            (north shl North.shiftBits) +
                    (west shl West.shiftBits) +
                    (east shl East.shiftBits) +
                    (south shl South.shiftBits)
        )
    }

    private val mask = 0b1111 shl shiftBits

    /**
     * The opposite of this cardinal direction
     */
    val opposite by lazy { values().first { it != this && it.vertical == this.vertical } }

    /**
     *
     */
    fun get(target: CardValues): CardValue =
        CardValue((target.value and mask) shr shiftBits)
}
