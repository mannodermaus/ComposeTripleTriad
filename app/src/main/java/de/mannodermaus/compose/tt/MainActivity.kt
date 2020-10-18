package de.mannodermaus.compose.tt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.setContent
import de.mannodermaus.compose.tt.domain.Board
import de.mannodermaus.compose.tt.ui.TripleTriadTheme
import de.mannodermaus.compose.tt.screens.game.GameScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripleTriadTheme {
                Surface(color = MaterialTheme.colors.background) {
                    GameScreen(Board.create())
                }
            }
        }
    }
}
