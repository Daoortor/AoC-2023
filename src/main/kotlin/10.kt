import java.util.Vector


fun main() {
    val deltas = arrayOf(Pair(0, -1), Pair(0, 1), Pair(-1, 0), Pair(1, 0), Pair(-1, -1), Pair(1, -1), Pair(-1, 1), Pair(1, 1))
    val facingUp = mapOf('L' to true, 'J' to true, '7' to false, 'F' to false)
    val diffToStartType = mapOf(Pair(Pair(-1, 0), Pair(1, 0)) to '|', Pair(Pair(1, 0), Pair(-1, 0)) to '|',
        Pair(Pair(0, -1), Pair(0, 1)) to '-', Pair(Pair(0, 1), Pair(0, -1)) to '-',
        Pair(Pair(-1, 0), Pair(0, 1)) to 'L', Pair(Pair(0, 1), Pair(-1, 0)) to 'L',
        Pair(Pair(-1, 0), Pair(0, -1)) to 'J', Pair(Pair(0, -1), Pair(-1, 0)) to 'J',
        Pair(Pair(1, 0), Pair(0, -1)) to '7', Pair(Pair(0, -1), Pair(1, 0)) to '7',
        Pair(Pair(1, 0), Pair(0, 1)) to 'F', Pair(Pair(0, 1), Pair(1, 0)) to 'F')

    fun neighbors(point: Pair<Int, Int>, sizeY: Int, sizeX: Int): Vector<Pair<Int, Int>> {
        val (y, x) = point
        val result = Vector<Pair<Int, Int>>()
        for ((deltaY, deltaX) in deltas) {
            val (cellY, cellX) = Pair(y + deltaY, x + deltaX)
            if (cellX in 0..<sizeX && cellY in 0..<sizeY) {
                result.addElement(Pair(cellY, cellX))
            }
        }
        return result
    }

    fun pipe_neighbors(point: Pair<Int, Int>, type: Char): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val (y, x) = point
        if (type == '|') {
            return Pair(Pair(y-1, x), Pair(y+1, x))
        }
        if (type == '-') {
            return Pair(Pair(y, x-1), Pair(y, x+1))
        }
        if (type == 'L') {
            return Pair(Pair(y-1, x), Pair(y, x+1))
        }
        if (type == 'J') {
            return Pair(Pair(y-1, x), Pair(y, x-1))
        }
        if (type == '7') {
            return Pair(Pair(y+1, x), Pair(y, x-1))
        }
        else if (type == 'F') {
            return Pair(Pair(y+1, x), Pair(y, x+1))
        } else {
            return Pair(point, point)
        }
    }


    fun loop(lines: List<List<Char>>): List<Pair<Int, Int>> {
        var start = Pair(0, 0)
        for (row in lines.indices) {
            for (col in lines[0].indices) {
                if (lines[row][col] == 'S') {
                    start = Pair(row, col)
                }
            }
        }
        val loop = Vector<Pair<Int, Int>>()
        loop.addElement(start)
        var cur = Pair(0, 0)
        for (p in neighbors(start, lines.size, lines[0].size)) {
            val nb = pipe_neighbors(p, lines[p.first][p.second])
            if (nb.first == start) {
                loop.addElement(p)
                cur = nb.second
                break
            } else if (nb.second == start) {
                loop.addElement(p)
                cur = nb.first
                break
            }
        }
        while (cur != start) {
            val nb = pipe_neighbors(cur, lines[cur.first][cur.second])
            loop.addElement(cur)
            cur = if (nb.first == loop[loop.size-2]) {
                nb.second
            } else {
                nb.first
            }
        }
        return loop
    }


    fun task1(lines: List<List<Char>>): Int {
        return loop(lines).size / 2
    }

    fun task2(lines: List<List<Char>>): Int {
        val border = MutableList(lines.size) {_ -> MutableList(lines[0].size) {_ -> '.'}}
        val loop = loop(lines)
        val diff1 = Pair(loop[1].first - loop[0].first, loop[1].second - loop[0].second)
        val diff2 = Pair(loop.last().first - loop[0].first, loop.last().second - loop[0].second)
        border[loop[0].first][loop[0].second] = diffToStartType[Pair(diff1, diff2)]!!
        for (cell in loop.subList(1, loop.size)) {
            border[cell.first][cell.second] = lines[cell.first][cell.second]
        }
        var result = 0
        for (row in border) {
            var inside = false
            var inBorder = false
            var borderStart = '.'
            for (cell in row) {
                if (cell == '.') {
                    if (inside) {
                        result++
                    }
                } else if (cell == '|') {
                    inside = !inside
                }
                else if (cell != '-') {
                    if (inBorder) {
                        if (facingUp[cell] != facingUp[borderStart]) {
                            inside = !inside
                        }
                    } else {
                        borderStart = cell
                    }
                    inBorder = !inBorder
                }
            }
        }
        return result
    }

    val lines = generateSequence(::readLine).map { it.toList() }.toList()
    println(task1(lines))
    println(task2(lines))
}
