package day02

import utils.readInput

data class Game(val gameID: Int, val red: Int, val blue: Int, val green: Int)
data class RGB(var red: Int, var blue: Int, var green: Int) {
    companion object {
        fun fromLine(line: String): RGB {
            return RGB(0, 0, 0).apply {
                line.split(",").forEach {
                    when {
                        it.endsWith("red") -> red = it.removeSuffix("red").toInt()
                        it.endsWith("blue") -> blue = it.removeSuffix("blue").toInt()
                        it.endsWith("green") -> green = it.removeSuffix("green").toInt()
                    }
                }
            }
        }
    }
}


fun main() {
    fun parseInput(input: List<String>): List<Game> {
        return input.mapIndexed { i, s ->
            s.split(":")[1]
                .replace(" ", "")
                .split(";")
                .map(RGB::fromLine)
                .let {
                    it.fold(RGB(0, 0, 0)) { acc, rgb ->
                        acc.red = maxOf(acc.red, rgb.red)
                        acc.blue = maxOf(acc.blue, rgb.blue)
                        acc.green = maxOf(acc.green, rgb.green)
                        acc
                    }.let {
                        Game(i + 1, it.red, it.blue, it.green)
                    }
                }
        }
    }

    fun part1(input: List<String>): Int {
        val maxRGB = RGB(12, 14, 13)
        return parseInput(input).filter { it.red <= maxRGB.red && it.blue <= maxRGB.blue && it.green <= maxRGB.green }
            .sumOf { it.gameID }
    }

    fun part2(input: List<String>): Int {
        return parseInput(input).sumOf { it.red * it.blue * it.green }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
        Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
        Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
        Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
    """.trimIndent().lines()
    check(testInput.let(::part1) == 8)
    check(testInput.let(::part2) == 2286)

    val input = readInput("day02/Day02")
    input.let(::part1).also(::println).let { check(it == 2105) }
    input.let(::part2).also(::println).let { check(it == 72422) }
}
