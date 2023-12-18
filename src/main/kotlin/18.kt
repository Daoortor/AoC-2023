import kotlin.math.*

fun main() {
    val directions = listOf('R', 'D', 'L', 'U')
    val directionToMove = mapOf('U' to Pair(-1, 0), 'D' to Pair(1, 0), 'L' to Pair(0, -1), 'R' to Pair(0, 1))

    fun area(moves: List<Pair<Char, Int>>): Long {
        val vertices = moves.runningFold(Pair(0L, 0L)) { (x, y), (dir, len) ->
            Pair(x + directionToMove[dir]!!.first * len, y + directionToMove[dir]!!.second * len)
        }
        val perimeter = moves.sumOf { it.second }
        return ((0..vertices.size-2).sumOf {
            vertices[it].first * vertices[it+1].second - vertices[it+1].first * vertices[it].second
        } / 2).absoluteValue + perimeter / 2 + 1
    }

    fun task1(lines: List<String>): Long {
        return area(lines.map { Regex("([A-Z]) (\\d+) .*").matchEntire(it)!!.groupValues }.map {
                groups -> Pair(groups[1][0], groups[2].toInt())
        })
    }

    fun task2(lines: List<String>): Long {
        return area(lines.map { Regex(".* \\(#(.*)([0-3])\\)").matchEntire(it)!!.groupValues }.map {
            Pair(directions[it[2][0].digitToInt()], it[1].toInt(radix=16))
        })
    }

    val lines = generateSequence(::readLine).toList()
    println(task1(lines))
    println(task2(lines))
}
