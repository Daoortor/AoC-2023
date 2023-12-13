import java.util.Vector
import kotlin.math.min


fun main() {
    fun transpose(table: List<List<Char>>): List<List<Char>> {
        return table[0].indices.map { j -> table.indices.map { i -> table[i][j] } }
    }

    fun diff(s1: List<Char>, s2: List<Char>): List<Int> {
        return s1.indices.filter { s1[it] != s2[it] }
    }

    fun symm(table: List<List<Char>>): Int {
        var rowSum = 0
        val rowHashes = table.map { it.hashCode() }
        for (row in 1..<table.size) {
            var ok = true
            for (cur in 1..min(row, table.size-row)) {
                if (rowHashes[row-cur] != rowHashes[row+cur-1]) {
                    ok = false
                }
            }
            if (ok) {
                rowSum += row
            }
        }
        return rowSum
    }

    fun smudge(table: List<List<Char>>): Pair<Pair<Int, Int>, Int>? {
        for (row in 1..<table.size) {
            var diffCount = 0
            var smudge = Pair(0, 0)
            for (cur in 1..min(row, table.size-row)) {
                val diff = diff(table[row-cur], table[row+cur-1])
                if (diff.size == 1) {
                    smudge = Pair(row-cur, diff[0])
                }
                diffCount += diff.size
            }
            if (diffCount == 1) {
                return Pair(smudge, row)
            }
        }
        return null
    }

    fun calc2(table: List<List<Char>>): Int {
        val smudge1 = smudge(table)
        val smudge2 = smudge(transpose(table))
        return if (smudge1 != null) {
            100 * smudge1.second
        } else {
            smudge2!!.second
        }
    }

    fun readNotes(lines: List<String>): List<List<List<Char>>> {
        val notes = MutableList(lines.count { it == "" } + 1) { Vector<List<Char>>() }
        var cur = 0
        for (line in lines) {
            if (line == "") {
                cur++
            } else {
                notes[cur].addElement(line.toList())
            }
        }
        return notes
    }

    fun task1(lines: List<String>): Int {
        return readNotes(lines).sumOf { 100 * symm(it) + symm(transpose(it)) }
    }

    fun task2(lines: List<String>): Int {
        return readNotes(lines).sumOf { calc2(it) }
    }

    val lines = generateSequence(::readLine).toList()
    println(task1(lines))
    println(task2(lines))
}
