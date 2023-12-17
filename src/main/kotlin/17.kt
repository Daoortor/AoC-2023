import java.util.PriorityQueue
import java.util.Vector


fun main() {
    val dirs = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))

    fun isValidCell(weights: List<List<Int>>, cell: Pair<Int, Int>): Boolean {
        return (0 <= cell.first) && (cell.first < weights.size) && (0 <= cell.second) && (cell.second < weights[0].size)
    }

    fun neighbors(weights: List<List<Int>>, minStreak: Int, maxStreak: Int, row: Int, col: Int, lastDir: Int, dirStreak: Int): List<List<Int>> {
        return when (dirStreak) {
            in 1..<minStreak -> listOf(listOf(row + dirs[lastDir].first, col + dirs[lastDir].second, lastDir, dirStreak + 1)).filter {
                isValidCell(weights, Pair(it[0], it[1]))
            }
            maxStreak -> dirs.indices.map {
                listOf(row + dirs[it].first, col + dirs[it].second, it, 1)
            }.filter { it[2] != lastDir && it[2] != (lastDir + 2) % 4 && isValidCell(weights, Pair(it[0], it[1])) }
            else -> dirs.indices.map {
                listOf(row + dirs[it].first, col + dirs[it].second, it, if (it == lastDir) dirStreak + 1 else 1)
            }.filter { it[2] != (lastDir + 2) % 4 && isValidCell(weights, Pair(it[0], it[1])) }
        }
    }

    fun minPath(weights: List<List<Int>>, minStreak: Int, maxStreak: Int): Int {
        val dist = List(weights.size) { List(weights[0].size) {
            List(4) { MutableList(maxStreak+1) { 1_000_000_000 } } }
        }
        val prev = List(weights.size) { List(weights[0].size) {
            List(4) { MutableList(maxStreak+1) { MutableList(5) { -1 } } } }
        }
        dist[0][0][0][0] = 0
        val q = PriorityQueue<List<Int>> { v1, v2 -> v1[4] - v2[4] }
        q.add(listOf(0, 0, 0, 0, 0))

        while (!q.isEmpty()) {
            val min = q.remove()
            for (v in neighbors(weights, minStreak, maxStreak, min[0], min[1], min[2], min[3])) {
                val newDist = dist[min[0]][min[1]][min[2]][min[3]] + weights[v[0]][v[1]]
                if (newDist < dist[v[0]][v[1]][v[2]][v[3]]) {
                    dist[v[0]][v[1]][v[2]][v[3]] = newDist
                    prev[v[0]][v[1]][v[2]][v[3]] = min.map { it }.toMutableList()
                    q.add(listOf(v[0], v[1], v[2], v[3], newDist))
                }
            }
        }
        return dist[dist.size-1][dist[0].size-1].minOf { it.subList(minStreak, it.size).min() }
    }

    fun task1(weights: List<List<Int>>) = minPath(weights, 0, 3)
    fun task2(weights: List<List<Int>>) = minPath(weights, 4, 10)

    val weights = generateSequence(::readLine).map { it.map { c -> c.digitToInt() } }.toList()
    println(task1(weights))
    println(task2(weights))
}
