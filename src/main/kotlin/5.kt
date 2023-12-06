import java.util.*

fun main() {
    fun evalTable(source: Long, table: Vector<Array<Long>>): Long {
        for (range in table) {
            if ((range[1] <= source) && (source < range[1] + range[2])) {
                return source + (range[0] - range[1])
            }
        }
        return source
    }

    fun evalTables(source: Long, tables: Vector<Vector<Array<Long>>>): Long {
        var result = source
        for (table in tables) {
            result = evalTable(result, table)
        }
        return result
    }

    fun evalTableInterval(source: Vector<Pair<Long, Long>>, table: Vector<Array<Long>>): Vector<Pair<Long, Long>> {
        val result = Vector<Pair<Long, Long>>()
        for ((start, end) in source) {
            if (start < table[0][1]) {
                result.add(Pair(start, table[0][1]))
            }
            for (map in table) {
                val mapSegment = Pair(map[1], map[1] + map[2])
                if ((mapSegment.first < end) && (start < mapSegment.second)) {
                    val intersection = Pair(kotlin.math.max(start, mapSegment.first), kotlin.math.min(end, mapSegment.second))
                    result.add(Pair(intersection.first + (map[0] - map[1]), intersection.second + (map[0] - map[1])))
                }
            }
            val lastEnd = table[table.size-1][1] + table[table.size-1][2]
            if (end >= lastEnd) {
                result.add(Pair(lastEnd, end))
            }
        }
        return result
    }

    fun parseSeeds(line: String): List<Long> {
        val seedsString = Regex("seeds: (.*)").matchEntire(line)!!.groupValues[1]
        return Regex("(\\d+)").findAll(seedsString).toList().map { it.groupValues[1].toLong() }
    }

    fun parseRanges(line: String): Vector<Pair<Long, Long>> {
        val seedsString = Regex("seeds: (.*)").matchEntire(line)!!.groupValues[1]
        val numbers = Regex("(\\d+)").findAll(seedsString).toList().map { it.groupValues[1].toLong() }
        val result = Vector<Pair<Long, Long>>()
        for (rangeIndex in 0..<numbers.size/2) {
            result.add(Pair(numbers[2*rangeIndex], numbers[2*rangeIndex] + numbers[2*rangeIndex+1]))
        }
        return result
    }

    fun parseTables(lines: List<String>): Vector<Vector<Array<Long>>> {
        val tables = Vector<Vector<Array<Long>>>()
        for (line in lines) {
            if (line == "") {
                tables.add(Vector<Array<Long>>())
                continue
            }
            if (!line[0].isDigit()) {
                continue
            }
            val nextRange = Regex("(\\d+)").findAll(line).toList().map { it.groupValues[1].toLong() }.toTypedArray()
            tables[tables.size-1].add(nextRange)
        }
        val comp = Comparator<Array<Long>> { a, b -> if (a.contentEquals(b)) 0 else if (a[1] < b[1]) -1 else 1 }
        tables.map { it.sortWith(comp) }
        val newTables = Vector<Vector<Array<Long>>>()
        for (table in tables) {
            val newTable = Vector<Array<Long>>()
            for (mapIndex in 0..<table.size-1) {
                newTable.add(table[mapIndex])
                val start = table[mapIndex][1] + table[mapIndex][2]
                if (start != table[mapIndex+1][1]) {
                    newTable.add(arrayOf(start, start, table[mapIndex+1][1]-start))
                }
            }
            newTable.add(table[table.size-1])
            newTables.add(newTable)
        }
        return newTables
    }

    fun task1(lines: List<String>): Long {
        val seeds = parseSeeds(lines[0])
        val tables = parseTables(lines.subList(1, lines.size))
        val locations = seeds.map { evalTables(it, tables) }
        return locations.min()
    }

    fun task2(lines: List<String>): Long {
        var ranges = parseRanges(lines[0])
        val tables = parseTables(lines.subList(1, lines.size))
        for (table in tables) {
            ranges = evalTableInterval(ranges, table)
        }
        return ranges.minOf { it.first }
    }

    val lines = generateSequence(::readLine).toList()
    println(task1(lines))
    println(task2(lines))
}
