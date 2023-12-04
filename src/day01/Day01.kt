package day01

import utils.readInput

import utils.println
import utils.readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { s ->
            s.first { it.isDigit() }.digitToInt() * 10 + s.last { it.isDigit() }.digitToInt()
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

                for ((num, numC) in lookupTable.entries) {
                    if (sub.startsWith(num)) {
                        returnValue = numC
                        break
                    }
                }

                returnValue
            }.joinToString()
        }.let(::part1)
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

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
