import java.util.Scanner

val mapColors = mapOf("red" to 0, "green" to 1, "blue" to 2)

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    var result = 0
    var gameNumber = 0
    while (scanner.hasNextLine()) {
        gameNumber++
        val line = scanner.nextLine()
        val parts = line.substringAfter(": ").split("; ")
        val counts = arrayOf(0, 0, 0)
        for (i in parts.indices) {
            val words = parts[i].split(" ", ", ")
            for (j in 1..<words.size step 2) {
                counts[mapColors[words[j]]!!] = kotlin.math.max(counts[mapColors[words[j]]!!], words[j - 1].toInt())
            }
        }
        result += counts[0] * counts[1] * counts[2]
    }
    println(result)
}