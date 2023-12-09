import kotlin.math.pow


fun main() {
    fun diff(seq: List<Long>) = generateSequence(seq) {
        s -> (1..<s.size).map { s[it] - s[it - 1] }
    }.take(seq.size).toList()

    fun task1(numbers: List<List<Long>>) = numbers.sumOf { seq -> diff(seq).sumOf { it[it.size - 1] } }

    fun task2(numbers: List<List<Long>>) = numbers.sumOf { seq ->
        val diff = diff(seq)
        seq.indices.sumOf { i -> (if (i % 2 == 0) 1 else -1) * diff[i][0] }
    }

    val lines = generateSequence(::readLine).toList()
    val numbers = lines.map {
            line -> Regex("([0-9-]+)").findAll(line).map { it.groupValues[1].toLong() }.toList()
    }
    println(task1(numbers))
    println(task2(numbers))
}