fun calc(line: String, seq: List<Int>): Long {
    val dp = MutableList(line.length+1) {seq.map { MutableList(it+1) {0L} }.toMutableList()}
    dp[0][0][0] = 1
    for (pos in 1..line.length) {
        for (block in seq.indices) {
            for (pref in 0..seq[block]) {
                if (line[pos-1] == '.') {
                    if (pref != 0) {
                        dp[pos][block][pref] = 0
                    } else {
                        dp[pos][block][pref] = dp[pos-1][block][pref]
                        if (block != 0) {
                            dp[pos][block][pref] += dp[pos-1][block-1][seq[block-1]]
                        }
                    }
                } else if (line[pos-1] == '#') {
                    if (pref == 0) {
                        dp[pos][block][pref] = 0
                    } else {
                        dp[pos][block][pref] = dp[pos-1][block][pref-1]
                    }
                } else {
                    if (pref == 0) {
                        dp[pos][block][pref] = dp[pos-1][block][pref]
                        if (block != 0) {
                            dp[pos][block][pref] += dp[pos-1][block-1][seq[block-1]]
                        }
                    } else {
                        dp[pos][block][pref] = dp[pos-1][block][pref-1]
                    }
                }
            }
        }
    }
    return dp[line.length][seq.size-1][0]
}


fun main() {
    fun task1(lines: List<String>): Long {
        var result = 0L
        for (line in lines) {
            val parts = Regex("(.*) (.*)").matchEntire(line)!!.groupValues
            val seq = Regex("(\\d+)").findAll(parts[2]).map { it.groupValues[1].toInt() }.toMutableList()
            seq.add(0)
            result += calc(parts[1] + '.', seq)
        }
        return result
    }

    fun task2(lines: List<String>): Long {
        var result = 0L
        for (line in lines) {
            val parts = Regex("(.*) (.*)").matchEntire(line)!!.groupValues
            val seq = Regex("(\\d+)").findAll(parts[2]).map { it.groupValues[1].toInt() }.toList()
            val template = (parts[1] + '?').repeat(4) + parts[1] + '.'
            val modifiedSeq = List(5) {seq}.flatten().toMutableList()
            modifiedSeq.add(0)
            result += calc(template, modifiedSeq)
        }
        return result
    }

    val lines = generateSequence(::readLine).toList()
    println(task1(lines))
    println(task2(lines))
}
