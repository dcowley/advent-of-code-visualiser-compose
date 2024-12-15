import androidx.compose.runtime.toMutableStateMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

private val input = buildString {
    repeat(50) {
        append('#')
    }
    append('\n')

    repeat(48 ){
        append('#')
        val chars = listOf('#', 'O', 'O', '.', '.', '.', '.', '.', '.')
        repeat(48) {
            append(chars.random())
        }
        append('#')
        append('\n')
    }

    repeat(50) {
        append('#')
    }
}

fun pushBoxes(): Flow<State> {
    val w = input.indexOf('\n')
    val map = input.trim()
        .replace("\n", "")
        .mapIndexed { index, c -> (index % w to index / w) to c }
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
        var robot = (0 until 50).random() to (0 until 50).random()
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
