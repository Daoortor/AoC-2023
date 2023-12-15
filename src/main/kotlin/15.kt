import java.util.Vector


fun main() {
    fun hash(s: String): Int = s.fold(0) { cur, chr -> (cur + chr.code) * 17 % 256 }
    fun task1(line: String): Int = line.split(',').sumOf { hash(it) }

    fun task2(line: String): Int {
        val operations = line.split(',').map {
            if (it.contains('='))
                Pair('=', it.split('='))
            else
                Pair('-', listOf(it.substring(0..it.length-2)))
        }
        val boxes = (0..255).map { mutableMapOf<String, Int>() }
        for (op in operations) {
            if (op.first == '=') {
                val label = op.second[0]
                val hash = hash(label)
                boxes[hash][label] = op.second[1].toInt()
            } else {
                boxes[hash(op.second[0])].remove(op.second[0])
            }
        }
        var result = 0
        for ((boxIndex, box) in boxes.withIndex()) {
            for ((slotIndex, entry) in box.entries.withIndex()) {
                result += (boxIndex + 1) * (slotIndex + 1) * entry.value
            }
        }
        return result
    }

    val line = readlnOrNull()!!
    println(task1(line))
    println(task2(line))
}
