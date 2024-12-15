import androidx.compose.runtime.toMutableStateMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val width = 50
const val height = 50

private val input = buildString {
    repeat(width) {
        append('#')
    }
    append('\n')

    repeat(height - 2 ){
        append('#')
        val chars = listOf('#', 'O', 'O', '.', '.', '.', '.', '.', '.')
        repeat(width - 2) {
            append(chars.random())
        }
        append('#')
        append('\n')
    }

    repeat(width) {
        append('#')
    }
}

fun pushBoxes(): Flow<State> {
    val map = input.trim()
        .replace("\n", "")
        .mapIndexed { index, c -> (index % width to index / width) to c }
        .toMutableStateMap()

    fun canPush(point: Pair<Int, Int>, direction: Char): Boolean = when (map[point]) {
        '.' -> true
        '#' -> false

        else -> {
            canPush(point.move(direction), direction)
        }
    }

    fun push(point: Pair<Int, Int>, direction: Char) {
        if (map[point] == '.') return

        val next = point.move(direction)
        push(next, direction)
        map[next] = map[point]!!
        map[point] = '.'
    }

    val directions = setOf('>', '<', '^', 'v')
    return flow {
        var robot = (0 until width).random() to (0 until height).random()
        val state = State(robot, map.filterValues { it == 'O' }.keys, map.filterValues { it == '#' }.keys)

        while (true) {
            val direction = directions.random()
            val next = robot.move(direction)
            if (canPush(next, direction)) {
                push(next, direction)
                robot = next
            }
            emit(
                state.copy(
                    robot = robot,
                    boxes = map.filterValues { it == 'O' }.keys
                )
            )
            delay(100)
        }
    }
}

private fun Pair<Int, Int>.move(direction: Char) = let { (x, y) ->
    when (direction) {
        '^' -> x to y - 1
        '>' -> x + 1 to y
        'v' -> x to y + 1
        '<' -> x - 1 to y
        else -> this
    }
}

data class State(
    val robot: Pair<Int, Int> = 0 to 0,
    val boxes: Set<Pair<Int, Int>> = emptySet(),
    val walls: Set<Pair<Int, Int>> = emptySet()
)
