data class Number(val row: Int, val start: Int, val end: Int, val number: Int)

val neighbours = setOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)

fun main() {
    fun parseNumbers(input: List<String>): List<Number> {
        val numbers = mutableListOf<Number>()
        for (i in input.indices) {
            val line = input[i]
            var startIndex = line.length
            for (j in line.indices) {
                val c = input[i][j]
                if (c.isDigit()) {
                    if (j < startIndex) startIndex = j
                }
                if (j == line.length - 1 || !c.isDigit()) {
                    var endIndex = j
                    if (j == line.length - 1 && c.isDigit()) endIndex += 1

                    if (startIndex < endIndex) {
                        numbers.add(Number(i, startIndex, endIndex, line.substring(startIndex, endIndex).toInt()))
                    }

                    startIndex = line.length
                }
            }
        }
        return numbers
    }

    fun parseSymbols(input: List<String>): Map<Pair<Int, Int>, Char> {
        val symbols = mutableMapOf<Pair<Int, Int>, Char>()
        for (i in input.indices) {
            val line = input[i]
            for (j in line.indices) {
                val c = input[i][j]
                if (!c.isDigit() && c != '.') {
                    symbols[(i to j)] = c
                }
            }
        }
        return symbols
    }

    fun part1(input: List<String>): Int {
        val numbers = parseNumbers(input)
        val symbols = parseSymbols(input)

        return numbers.map {
            for (col in it.start..<it.end) {
                for ((dr, dc) in neighbours) {
                    val newRow = it.row + dr
                    val newCol = col + dc

                    val symbol = symbols[newRow to newCol]
                    if (symbol != null) {
                        return@map it.number
                    }
                }
            }
            null
        }.filterNotNull().sum()

        // 611907
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..
    """.trimIndent().lines()
    check(part1(testInput) == 4361)

    check(part2(testInput) == 1)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
