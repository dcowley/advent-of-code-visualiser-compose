import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*

fun main() = application {
    val state = rememberWindowState(
        position = WindowPosition(Alignment.Center),
        size = DpSize(800.dp, 828.dp)
    )

    Window(onCloseRequest = ::exitApplication, state = state, resizable = false, title = "Warehouse Woes") {
        Screen()
    }
}