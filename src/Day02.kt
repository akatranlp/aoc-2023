fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = """
    """.trimIndent().lines()
    check(part1(testInput1) == 1)

    val testInput2 = """
    """.trimIndent().lines()
    check(part2(testInput2) == 1)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
