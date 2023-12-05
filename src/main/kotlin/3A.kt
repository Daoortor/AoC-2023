import java.util.Scanner
import java.util.Vector

fun main() {
    val lines = Vector<String>()
    val scanner = Scanner(System.`in`)
    while (scanner.hasNextLine()) {
        lines.addElement(scanner.nextLine())
    }
    var result = 0
    for (i in lines.indices) {
        val matchNumbers = Regex("([0-9]+)")
        val matches = matchNumbers.findAll(lines[i])
        val numbers = matches.map { Pair(it.groups[1]!!.range, it.groupValues[1].toInt()) }
        for ((range, number) in numbers) {
            val extendedRange = kotlin.math.max(range.first-1, 0)..kotlin.math.min(range.last+1, lines[i].length-1)
            val regexHasSymbol = Regex(".*[^0-9.].*")
            val case1 = (range.first != 0) && (lines[i][range.first - 1] != '.')
            val case2 = (range.last != lines[i].length - 1) && (lines[i][range.last + 1] != '.')
            val case3 = (i != 0) && (lines[i - 1].substring(extendedRange).matches(regexHasSymbol))
            val case4 = (i != lines.size - 1) && (lines[i + 1].substring(extendedRange).matches(regexHasSymbol))
            if (case1 || case2 || case3 || case4) {
                result += number
            }
        }
    }
    println(result)
}