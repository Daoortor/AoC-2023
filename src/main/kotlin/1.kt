import java.util.Scanner

val digits = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

fun isDigit(c: Char): Boolean {
    return ('0' <= c) and (c <= '9')
}

fun firstSpelled(s: String): Int {
    var ind = 1000000
    var ans = 0
    for (i in 1..9) {
        if ((s.indexOf(digits[i-1]) != -1) and (s.indexOf(digits[i-1]) < ind)) {
            ind = s.indexOf(digits[i-1])
            ans = i
        }
        if ((s.indexOf(i.toString()) != -1) and (s.indexOf(i.toString()) < ind)) {
            ind = s.indexOf(i.toString())
            ans = i
        }
    }
    return ans
}

fun lastSpelled(s: String): Int {
    var ind = -1
    var ans = 0
    for (i in 1..9) {
        if ((s.lastIndexOf(digits[i-1]) != -1) and (s.lastIndexOf(digits[i-1]) > ind)) {
            ind = s.lastIndexOf(digits[i-1])
            ans = i
        }
        if ((s.lastIndexOf(i.toString()) != -1) and (s.lastIndexOf(i.toString()) > ind)) {
            ind = s.lastIndexOf(i.toString())
            ans = i
        }
    }
    return ans
}

fun main() {
    val scanner = Scanner(System.`in`)
    var sum = 0
    while (scanner.hasNextLine()) {
        val line = scanner.nextLine()
        var first = firstSpelled(line)
        var last = lastSpelled(line)
        if (first != -1) {
            sum += 10*first + last
        }
    }
    println(sum)
}