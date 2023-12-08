import java.util.Arrays.compare


val nominalToStrength1 = mapOf(Pair('2', 2), Pair('3', 3),
    Pair('4', 4), Pair('5', 5), Pair('6', 6), Pair('7', 7), Pair('8', 8),
    Pair('9', 9), Pair('T', 10), Pair('J', 11), Pair('Q', 12), Pair('K', 13),
    Pair('A', 14))
val nominalToStrength2 = mapOf(Pair('J', 1), Pair('2', 2), Pair('3', 3),
    Pair('4', 4), Pair('5', 5), Pair('6', 6), Pair('7', 7), Pair('8', 8),
    Pair('9', 9), Pair('T', 10), Pair('Q', 11), Pair('K', 12),
    Pair('A', 13))

val freqToType = mapOf(listOf(5) to 6, listOf(1, 4) to 5, listOf(2, 3) to 4,
    listOf(1, 1, 3) to 3, listOf(1, 2, 2) to 2, listOf(1, 1, 1, 2) to 1,
    listOf(1, 1, 1, 1, 1) to 0)


fun main() {
    fun type(card: String, task: Boolean): Int {
        if (task) {
            val filteredCard = card.filter { it != 'J' }
            if (filteredCard.isEmpty()) {
                return 6
            }
            val filteredFreq = filteredCard.groupBy { it }.values.map { it.size }.sorted().toMutableList()
            filteredFreq[filteredFreq.size-1] += card.count { it == 'J' }
            return freqToType[filteredFreq]!!
        }
        val freq = card.groupBy { it }.values.map { it.size }.sorted()
        return freqToType[freq]!!
    }

    fun nominals(card: String, task: Boolean): Array<Int> {
        return card.map {
            if (task) nominalToStrength2[it]!! else nominalToStrength1[it]!!
        }.toTypedArray()
    }

    fun cost(lines: List<String>, task: Boolean): Long {
        val matches = lines.map { Regex("(.*) (.*)").matchEntire(it)!!.groupValues }
        val cards = matches.map { Pair(it[1], it[2].toLong()) }
        val comp = Comparator<Pair<String, Long>> { a, b ->
            val card1 = a.first
            val card2 = b.first
            if (type(card1, task) == type(card2, task)) {
                compare(nominals(card1, task), nominals(card2, task))
            }
            else type(card1, task) - type(card2, task)
        }
        val sortedCards = cards.sortedWith(comp)
        return sortedCards.indices.sumOf { (it + 1) * sortedCards[it].second }
    }

    fun part1(lines: List<String>) = cost(lines, false)
    fun part2(lines: List<String>) = cost(lines, true)

    val lines = generateSequence(::readLine).toList()
    println(part1(lines))
    println(part2(lines))
}
