fun main() {
    fun load(lines: List<List<Char>>): Long {
        return lines.indices.sumOf { i -> lines[i].indices.sumOf {
            j -> if (lines[i][j] == 'O') (lines.size - i).toLong() else 0
        } }
    }

    fun turn(lines: List<List<Char>>): List<List<Char>> {
        val newLines = lines.map { it.map { _ -> '.' }.toMutableList() }
        for (colIndex in lines[0].indices) {
            var lastCube = -1
            var circleCount = 0
            for (rowIndex in lines.indices) {
                if (lines[rowIndex][colIndex] == '#') {
                    newLines[rowIndex][colIndex] = '#'
                    lastCube = rowIndex
                    circleCount = 0
                } else if (lines[rowIndex][colIndex] == 'O') {
                    circleCount++
                    newLines[lastCube+circleCount][colIndex] = 'O'
                }
            }
        }
        return newLines
    }

    fun rotate(lines: List<List<Char>>): List<List<Char>> {
        return lines[0].indices.map { j -> lines.indices.map { i -> lines[lines.size-i-1][j] } }
    }

    fun cycle(lines: List<List<Char>>): Pair<List<List<Char>>, Long> {
        var newLines = lines.map { it.map { cell -> cell } }
        var result = 0L
        for (i in 0..3) {
            newLines = rotate(turn(newLines))
            result += load(newLines)
        }
        return Pair(newLines, result)
    }

    fun task1(lines: List<List<Char>>): Long {
        return load(turn(lines))
    }

    fun task2(lines: List<List<Char>>): Long {
        fun iter(curLines: List<List<Char>>) = cycle(curLines).first
        var slow = lines
        var fast = lines
        val cycleLengths = mutableListOf(0, 0)
        for (i in 0..1) {
            slow = iter(slow)
            fast = iter(iter(fast))
            var iterNum = 1
            while (slow != fast) {
                slow = iter(slow)
                fast = iter(iter(fast))
                iterNum++
            }
            cycleLengths[i] = iterNum
        }
        val preCycle = cycleLengths[0]
        val cycle = cycleLengths[1]
        for (iter in 1..(1_000_000_000 - preCycle) % cycle) {
            slow = iter(slow)
        }
        return load(slow)
    }

    val lines = generateSequence(::readLine).map { it.toList() }.toList()
    println(task1(lines))
    println(task2(lines))
}
