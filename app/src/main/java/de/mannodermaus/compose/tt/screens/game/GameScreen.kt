package de.mannodermaus.compose.tt.screens.game

import androidx.compose.animation.animate
import androidx.compose.foundation.Image
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.drawLayer
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ConfigurationAmbient
import androidx.compose.ui.platform.DensityAmbient
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import de.mannodermaus.compose.tt.R
import de.mannodermaus.compose.tt.domain.*
import de.mannodermaus.compose.tt.ui.blue700
import de.mannodermaus.compose.tt.ui.red700
import de.mannodermaus.compose.tt.widgets.DraggableBox
import kotlin.random.Random

private val testCard = Card(
    id = CardId("id1"),
    name = "Monster",
    imageUrl = "https://image.com/image.jpg",
    value = cardValuesOf(north = 8, west = 7, east = 1, south = 4),
)

private val player1 = PlayerId("player1")
private val player2 = PlayerId("player2")

@Composable
fun GameScreen(board: Board) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Draw background

        // Draw board grid
        BoardComponent(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .align(Alignment.Center),
            board = board,
        )

        // Draw cards (top)
        (0 until 5).map { index ->
            CardComponent(
                alignment = Alignment.Top,
                player = player2,
                index = index,
                card = testCard,
            )
        }
        
        // Draw cards (bottom)
        (0 until 5).map { index ->
            CardComponent(
                alignment = Alignment.Bottom,
                player = player1,
                index = index,
                card = testCard,
            )
        }

        // Draw other UI and HUD elements
    }
}

@Composable
private fun BoardComponent(
    modifier: Modifier = Modifier,
    board: Board
) {
    Column(modifier = modifier) {
        (0 until board.size).map { rowIndex ->
            Row(modifier = Modifier.fillMaxWidth()) {
                (0 until board.size).map { columnIndex ->
                    Text(
                        modifier = Modifier
                            .background(Color(Random.nextInt()))
                            .weight(1f)
                            .aspectRatio(1f),
                        text = "[$rowIndex,$columnIndex]"
                    )
                }
            }
        }
    }
}

@Composable
private fun CardComponent(
    modifier: Modifier = Modifier,
    alignment: Alignment.Vertical,
    player: PlayerId,
    index: Int,
    card: Card,
) {
    // Calculate initial X position
    val screenWidthPx = with(DensityAmbient.current) {
        ConfigurationAmbient.current.screenWidthDp.dp.toPx()
    }
    val screenHeightPx = with(DensityAmbient.current) {
        ConfigurationAmbient.current.screenHeightDp.dp.toPx()
    }

    val verticalPaddingPx = with(DensityAmbient.current) { 32.dp.toPx() }

    val initialXPx = screenWidthPx / 5 * index
    val initialYPx = when (alignment) {
        Alignment.Top -> verticalPaddingPx
        Alignment.Bottom -> screenHeightPx - verticalPaddingPx
        else -> throw IllegalArgumentException("")
    }

    DraggableBox(
        modifier = modifier,
        initialPosition = Offset(initialXPx, initialYPx)
    ) { dragging ->
        // Animated properties
        val cardScale = animate(if (dragging) 1.0f else 0.8f)
        val cardElevation = animate(if (dragging) 12.dp else 0.dp)

        Box(
            modifier = Modifier
                .width(96.dp)
                .aspectRatio(0.8f)
                .drawLayer(
                    scaleX = cardScale,
                    scaleY = cardScale,
                )
                .drawShadow(elevation = cardElevation)
                .clip(MaterialTheme.shapes.small)
                .background(if (player == player1) blue700 else red700)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight,
                asset = imageResource(R.drawable.cactuar)
            )

            CardValuesComponent(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                card = card,
            )
        }
    }
}

@Composable
private fun CardValuesComponent(
    modifier: Modifier = Modifier,
    card: Card,
) {
    Text(
        modifier = modifier,
        text = " ${card[Dir.North].displayName}\n${card[Dir.West].displayName} ${card[Dir.East].displayName}\n ${card[Dir.South].displayName}",
        fontFamily = FontFamily.Monospace,
        color = Color.White,
        fontWeight = FontWeight.W700,
    )
}
