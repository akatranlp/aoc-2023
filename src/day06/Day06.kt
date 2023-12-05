package day05

import utils.readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
    """.trimIndent().lines()
    check(testInput.let(::part1) == 1)
    check(testInput.let(::part2) == 1)

    val input = readInput("day06/Day06")
    input.let(::part1).also(::println).let { check(it == 0) }
    input.let(::part2).also(::println).let { check(it == 0) }
}
