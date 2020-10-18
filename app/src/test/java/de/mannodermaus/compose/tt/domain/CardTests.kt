package de.mannodermaus.compose.tt.domain

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory

class CardTests {

    @Test
    fun `accessing directional values works`() {
        val card = Card(
            id = CardId("id1"),
            name = "Squall",
            imageUrl = "https://lol.com/image.jpg",
            value = cardValuesOf(north = 1, west = 6, east = 8, south = 10),
        )

        assertThat(card[Dir.North]).isEqualTo(CardValue(1))
        assertThat(card[Dir.West]).isEqualTo(CardValue(6))
        assertThat(card[Dir.East]).isEqualTo(CardValue(8))
        assertThat(card[Dir.South]).isEqualTo(CardValue(10))
    }

    @TestFactory
    fun `converting value to display name works`() =
        listOf(
            1 to "1",
            2 to "2",
            3 to "3",
            4 to "4",
            5 to "5",
            6 to "6",
            7 to "7",
            8 to "8",
            9 to "9",
            10 to "A",
        ).map { (input, expected) ->
            dynamicTest("input=$input, expected=$expected") {
                assertThat(CardValue(input).displayName).isEqualTo(expected)
            }
        }
}
