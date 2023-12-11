import java.util.Vector


fun main() {
    fun calc(lines: List<List<Char>>, expansion: Int): Long {
        val isRowEmpty = lines.map { it.all { c -> c == '.' } }
        val isColEmpty = lines[0].indices.map { lines.indices.all { row -> lines[row][it] == '.' } }
        var emptyRowCount = 0L
        val galaxies = listOf(Vector<Long>(), Vector<Long>())
        for (row in lines.indices) {
            if (isRowEmpty[row]) {
                emptyRowCount++
                continue
            }
            var emptyColCount = 0L
            for (col in lines[0].indices) {
                if (isColEmpty[col]) {
                    emptyColCount++
                    continue
                }
                if (lines[row][col] == '#') {
                    galaxies[0].addElement(row + emptyRowCount * (expansion - 1))
                    galaxies[1].addElement(col + emptyColCount * (expansion - 1))
                }
            }
        }
        galaxies.map { it.sort() }
        var result = 0L
        for (axis in galaxies) {
            for (ind in 0..axis.size-2) {
                result += (axis[ind+1] - axis[ind]) * (ind + 1) * (axis.size - ind - 1)
            }
        }
        return result
    }

    fun task1(lines: List<List<Char>>) = calc(lines, 2)
    fun task2(lines: List<List<Char>>) = calc(lines, 1_000_000)

    val inputLines = generateSequence(::readLine).map { it.toList() }.toList()
    println(task1(inputLines))
    println(task2(inputLines))
}
