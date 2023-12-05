import java.util.Scanner
import java.util.Vector

val deltas = arrayOf(Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(1, 0), Pair(-1, -1), Pair(1, -1), Pair(-1, 1), Pair(1, 1))

fun neighbours(x: Int, y: Int, sizeX: Int, sizeY: Int): Vector<Pair<Int, Int>> {
    val result = Vector<Pair<Int, Int>>()
    for ((deltaX, deltaY) in deltas) {
        val (cellX, cellY) = Pair(x + deltaX, y + deltaY)
        if (cellX in 0..<sizeX && cellY in 0..<sizeY) {
            result.addElement(Pair(cellX, cellY))
        }
    }
    return result
}

fun main() {
    val lines = Vector<String>()
    val scanner = Scanner(System.`in`)
    while (scanner.hasNextLine()) {
        lines.addElement(scanner.nextLine())
    }
    val numberPlaces = Array(lines.size) { _ -> Array(lines[0].length) { _ -> 0 } }
    for (i in lines.indices) {
        val matchNumbers = Regex("([0-9]+)")
        val matches = matchNumbers.findAll(lines[i])
        val numberRanges = matches.map { Pair(it.groups[1]!!.range, it.groupValues[1].toInt()) }
        for ((range, number) in numberRanges) {
            for (pos in range) {
                numberPlaces[i][pos] = number
            }
        }
    }
    var result = 0
    for (i in lines.indices) {
        for (j in lines[0].indices) {
            if (lines[i][j] != '*') {
                continue
            }
            val adjacentNumbers = mutableSetOf<Int>()
            for ((x, y) in neighbours(i, j, lines.size, lines[0].length)) {
                if (numberPlaces[x][y] != 0) {
                    adjacentNumbers.add(numberPlaces[x][y])
                }
            }
            if (adjacentNumbers.size == 2) {
                result += adjacentNumbers.elementAt(0) * adjacentNumbers.elementAt(1)
            }
        }
    }
    println(result)
}