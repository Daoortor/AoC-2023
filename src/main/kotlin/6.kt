import kotlin.math.max
import kotlin.math.sqrt

fun main() {
    fun winCount(time: Long, record: Long): Long {
        if (time*time - 4*record <= 0) {
            return 0
        }
        val discriminant = (time*time - 4*record).toDouble()
        val (root1, root2) = Pair((time - sqrt(discriminant)) / 2, (time + sqrt(discriminant)) / 2)
        val (winningFrom, winningTo) = Pair(kotlin.math.floor(root1).toLong() + 1, kotlin.math.ceil(root2).toLong() - 1)
        return max(0, winningTo - winningFrom + 1)
    }

    fun part1(lines: List<String>): Long {
        val numberStrings = lines.map { Regex(".*: *(.*)").matchEntire(it)!!.groupValues[1] }
        val numbers = numberStrings.map { Regex("(\\d+)").findAll(it).map { match -> match.groupValues[1].toLong() }.toList() }
        val races = numbers[0].indices.map { Pair(numbers[0][it], numbers[1][it]) }
        var result = 1L
        for ((time, record) in races) {
            result *= winCount(time, record)
        }
        return result
    }

    fun part2(lines: List<String>): Long {
        val numberStrings = lines.map { Regex(".*: *(.*)").matchEntire(it)!!.groupValues[1] }
        val numbers = numberStrings.map { Regex("(\\d+)").findAll(it).map {
            match -> match.groupValues[1] }.joinToString("").toLong()
        }
        return winCount(numbers[0], numbers[1])
    }

    val lines = generateSequence(::readLine).toList()
    println(part1(lines))
    println(part2(lines))
}