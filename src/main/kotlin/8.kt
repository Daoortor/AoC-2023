fun main() {
    fun parse(lines: List<String>): Pair<List<Char>, Map<String, Pair<String, String>>> {
        val moves = lines[0].toList()
        val network = lines.subList(2, lines.size).associate {
            val numbers = Regex("([A-Z0-9]+)").findAll(it).map { match ->
                match.groupValues[0]
            }.toList()
            Pair(numbers[0], Pair(numbers[1], numbers[2]))
        }
        return Pair(moves, network)
    }

    fun go(cur: String, moves: List<Char>, network: Map<String, Pair<String, String>>, step: Long): String {
        return if (moves[step.mod(moves.size)] == 'L') {
            network[cur]!!.first
        } else {
            network[cur]!!.second
        }
    }

    fun task1(lines: List<String>): Int {
        val (moves, network) = parse(lines)
        var cur = "AAA"
        var step = 0
        while (cur != "ZZZ") {
            cur = if (moves[step.mod(moves.size)] == 'L') {
                network[cur]!!.first
            } else {
                network[cur]!!.second
            }
            step++
        }
        return step
    }

    fun analyseSequence(start: String, steps: Int, transform: (s: String, step: Long) -> String): Long {
        var slow = transform(start, 0)
        var fast = transform(transform(start, 0), 1)
        var step = 1L
        while (slow != fast || step.mod(steps) != 0) {
            slow = transform(slow, step)
            fast = transform(transform(fast, step*2), step*2+1)
            step++
        }
        return step
    }

    fun gcd(a: Long, b: Long): Long {
        if (a == 0L) {
            return b
        }
        if (b == 0L) {
            return a
        }
        if (a > b) {
            return gcd(b, a.mod(b))
        }
        return gcd(a, b.mod(a))
    }

    fun task2(lines: List<String>): Long {
        val (moves, network) = parse(lines)
        val initial = network.keys.filter { node -> node.endsWith("A") }
        val rem = initial.map {
            var step = 0L
            var cur = it
            while (!cur.endsWith('Z')) {
                cur = go(cur, moves, network, step)
                step++
            }
            step
        }.toList()
        var curLCM = rem[0]
        for (step in rem) {
            curLCM = curLCM * step / gcd(curLCM, step)
        }
        return curLCM
    }

    val lines = generateSequence(::readLine).toList()
    println(task1(lines))
    println(task2(lines))
}