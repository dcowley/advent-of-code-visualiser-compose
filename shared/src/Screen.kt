import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

@Composable
fun Screen() {
    val textMeasurer = rememberTextMeasurer()

    val stateFlow = pushBoxes()
    val state by stateFlow.collectAsState(State.INITIAL)

    MaterialTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                val fontSize = (size.width / 50).toSp()
                val ratio = min(size.width / 50, size.height / 50)
                state.robot.let { (x, y) ->
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "🤖",
                        topLeft = Offset(x * ratio, y * ratio),
                        style = TextStyle(
                            fontSize = fontSize
                        )
                    )
                }
                state.boxes.forEach { (x, y) ->
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "📦",
                        topLeft = Offset(x * ratio, y * ratio),
                        style = TextStyle(
                            fontSize = fontSize
                        )
                    )
                }
                state.walls.forEach { (x, y) ->
                    drawText(
                        textMeasurer = textMeasurer,
                        text = "⬛️",
                        topLeft = Offset(x * ratio, y * ratio),
                        style = TextStyle(
                            fontSize = fontSize
                        )
                    )
                }
            }
        }
    }
}
