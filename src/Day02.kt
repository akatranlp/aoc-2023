data class Game(val gameID: Int, val red: Int, val blue: Int, val green: Int)
data class RGB(var red: Int, var blue: Int, var green: Int)

fun main() {
    fun parseInput(input: List<String>): List<Game> {
        return input.mapIndexed { i, s ->
            s.split(":")[1].replace(" ", "").split(";").map {
                val rgb = RGB(0, 0, 0)
                it.split(",").forEach {
                    val num = it.toInt()
                    when {
                        it.endsWith("red") -> rgb.red = num
                        it.endsWith("blue") -> rgb.blue = num
                        it.endsWith("green") -> rgb.green = num
                    }
                }
                rgb
            }.let {
                val red = it.maxOf { it.red }
                val blue = it.maxOf { it.blue }
                val green = it.maxOf { it.green }
                Game(i + 1, red, blue, green)
            }
        }
    }

    fun part1(input: List<String>): Int {
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14
        return parseInput(input).filter { it.red <= maxRed && it.blue <= maxBlue && it.green <= maxGreen }
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
    check(part1(testInput) == 8)

    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
