package day06

import utils.readInput

fun main() {
    fun calculateWins(time: Long, distance: Long): Long {
        // f(0) -> 0
        // f(x) -> f(x-1) + <time> - 1 - 2(x-1)
        // The Curve of this function is symmetric at (<time> / 2 + 1) so we only need to look at the first half of indices
        // And at the first hit we know that all upcoming indices till (<time>-x) are also hits
        val loops = time.shr(1) + 1
        var prev = 0L
        var winningIndex = -1L
        for (i in 1..<loops) {
            prev = prev + time - 1 - ((i - 1) * 2)
            if (prev > distance) {
                winningIndex = i
                break
            }
        }
        //           ==> time % 2 == 0
        return if (time.and(1L) == 1L) 2 * (loops - winningIndex) else 2 * (loops - winningIndex) - 1

    }

    fun part1(input: List<String>): Long {
        val (timeLine, distanceLine) = input
        val times = timeLine.substringAfter(':').split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        val distances = distanceLine.substringAfter(':').split(" ").filter { it.isNotBlank() }.map { it.toLong() }

        return times.zip(distances).map {
            calculateWins(it.first, it.second)
        }.reduce(Long::times)
    }

    fun part2(input: List<String>): Long {
        val (timeLine, distanceLine) = input
        val time = timeLine.substringAfter(':').replace(" ", "").toLong()
        val distance = distanceLine.substringAfter(':').replace(" ", "").toLong()

        return calculateWins(time, distance)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        Time:      7  15   30
        Distance:  9  40  200
    """.trimIndent().lines()
    check(testInput.let(::part1) == 288L)
    check(testInput.let(::part2) == 71503L)

    val input = readInput("day06/Day06")
    input.let(::part1).also(::println).let { check(it == 211904L) }
    input.let(::part2).also(::println).let { check(it == 43364472L) }
}
