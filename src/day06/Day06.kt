package day06

import utils.readInput

data class Race(val time: Int, val distance: Int)

fun main() {
    fun parseInput(input: List<String>): List<Race> {
        val (timeLine, distanceLine) = input
        val times = timeLine.drop(5).split(" ").filter { it.isNotBlank() }.map { it.toInt() }
        val distances = distanceLine.drop(9).split(" ").filter { it.isNotBlank() }.map { it.toInt() }

        return times.indices.map {
            Race(times[it], distances[it])
        }
    }

    fun part1(input: List<String>): Int {
        val races = parseInput(input)

        return races.map { race ->
            // it mirrors so we only need to check the first half
            (0..race.time).map {
                it * (race.time - it)
            }.count { it > race.distance }
        }.reduce { acc, i -> acc * i }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent().lines()
    check(testInput.let(::part1) == 288)
    // check(testInput.let(::part2) == 1)

    val input = readInput("day06/Day06")
    input.let(::part1).also(::println).let { check(it == 211904) }
    input.let(::part2).also(::println).let { check(it == 0) }
}
