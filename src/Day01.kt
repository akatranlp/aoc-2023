fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(0) { acc, s ->
            s.filter { it.isDigit() }.map { it.digitToInt() }.let {
                it.first() * 10 + it.last() + acc
            }
        }
    }

    val lookupTable = mapOf(
        "one" to '1',
        "two" to '2',
        "three" to '3',
        "four" to '4',
        "five" to '5',
        "six" to '6',
        "seven" to '7',
        "eight" to '8',
        "nine" to '9'
    )

    fun part2(input: List<String>): Int {
        return input.map {
            it.mapIndexed { i, c ->
                val sub = it.substring(i)
                var returnValue = c

                for (num in lookupTable.keys) {
                    if (sub.startsWith(num)) {
                        returnValue = lookupTable[num]!!
                    }
                }
                returnValue
            }.toString()
        }.let { part1(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """.trimIndent().lines()
    check(part1(testInput1) == 142)

    val testInput2 = """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """.trimIndent().lines()
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
