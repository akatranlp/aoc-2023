fun main() {
    val maxRed = 12
    val maxGreen = 13
    val maxBlue = 14

    fun part1(input: List<String>): Int {
        return input.mapIndexed { i, s ->
            s.split(":")[1].replace(" ", "").split(";").map {
                var red = 0
                var blue = 0
                var green = 0

                it.split(",").forEach {
                    if (it.endsWith("red")) {
                        red = it.removeSuffix("red").toInt()
                    } else if (it.endsWith("blue")) {
                        blue = it.removeSuffix("blue").toInt()
                    } else {
                        green = it.removeSuffix("green").toInt()
                    }
                }

                red > maxRed || blue > maxBlue || green > maxGreen
            }.count { it }.let {
                if (it == 0) {
                    i + 1
                } else {
                    0
                }
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.size
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

    val testInput2 = """
    """.trimIndent().lines()
    check(part2(testInput2) == 1)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
