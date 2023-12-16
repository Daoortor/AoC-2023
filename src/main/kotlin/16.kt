import java.util.Vector


fun main() {
    val transform = mapOf<Char, (Pair<Int, Int>) -> List<Pair<Int, Int>>>('\\' to { (a, b) -> listOf(Pair(b, a)) },
        '.' to { listOf(it) }, '/' to { (a, b) -> listOf(Pair(-b, -a)) },
        '|' to { (a, b) -> if (a != 0) listOf(Pair(0, 1), Pair(0, -1)) else listOf(Pair(a, b)) },
        '-' to { (a, b) -> if (b != 0) listOf(Pair(1, 0), Pair(-1, 0)) else listOf(Pair(a, b)) })
    val dirToNum = mapOf(Pair(0, 1) to 0, Pair(1, 0) to 1, Pair(0, -1) to 2, Pair(-1, 0) to 3 )

    val lines = generateSequence(::readLine).toList()
    var charged = lines.map { it.map { false }.toMutableList() }
    var visited = lines.map { it.map { (0..3).map { false }.toMutableList() } }
    val cells = Vector<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
    cells.addElement(Pair(Pair(0, 0), Pair(0, 1)))
    val nextCells = Vector<Pair<Pair<Int, Int>, Pair<Int, Int>>>()

    fun isValidCell(cell: Pair<Int, Int>): Boolean {
        return (0 <= cell.first) && (cell.first < lines[0].length) && (0 <= cell.second) && (cell.second < lines.size)
    }

    fun propagate(start: Pair<Int, Int>, direction: Pair<Int, Int>) {
        if (visited[start.second][start.first][dirToNum[direction]!!]) {
            return
        }
        visited[start.second][start.first][dirToNum[direction]!!] = true
        charged[start.second][start.first] = true
        val newDirections = transform[lines[start.second][start.first]]!!(direction)
        newDirections.map {
            val nextCell = Pair(start.first + it.first, start.second + it.second)
            if (isValidCell(nextCell)) {
                nextCells.addElement(Pair(nextCell, it))
            }
        }
    }

    fun energize(start: Pair<Int, Int>, direction: Pair<Int, Int>): Int {
        visited = lines.map { it.map { (0..3).map { false }.toMutableList() } }
        charged = lines.map { it.map { false }.toMutableList() }
        cells.clear()
        cells.addElement(Pair(start, direction))
        nextCells.clear()
        while (!cells.isEmpty()) {
            for (cell in cells) {
                propagate(cell.first, cell.second)
            }
            cells.clear()
            cells.addAll(nextCells)
            nextCells.clear()
        }
        return charged.sumOf { it.count { cell -> cell } }
    }

    fun task1() = energize(Pair(0, 0), Pair(1, 0))

    fun task2(): Int {
        val starts = listOf(lines[0].indices.map { Pair(Pair(it, 0), Pair(0, 1)) },
            lines[0].indices.map { Pair(Pair(it, lines.size-1), Pair(0, -1)) },
            lines.indices.map { Pair(Pair(0, it), Pair(1, 0)) },
            lines.indices.map { Pair(Pair(lines.size-1, it), Pair(-1, 0)) }).flatten()
        return starts.maxOf { energize(it.first, it.second) }
    }

    println(task1())
    println(task2())
}
