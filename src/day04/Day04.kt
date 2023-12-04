package day04

import utils.readInput

fun main() {
    fun parseCards(input: List<String>): List<Int> {
        return input.mapIndexed { i, s ->
            s.split(":", " |").let { (_, winningNumbersText, myNumbersText) ->
                val winningNumbers = winningNumbersText.chunked(3).map { it.trimStart().toInt() }.toSet()
                val myNumbers = myNumbersText.chunked(3).map { it.trimStart().toInt() }
                myNumbers.count { it in winningNumbers }
            }
        }
    }

    fun part1(input: List<String>): Int {
        return input
            .let(::parseCards)
            .sumOf {
                if (it == 0) 0 else 1.shl(it - 1)
            }
    }

    fun part2(input: List<String>): Int {
        return input.let(::parseCards).let { cardPoints ->
            val map = MutableList(cardPoints.size) { 1 }

            cardPoints.forEachIndexed { index, points ->
                (1..points).forEach {
                    map[index + it] += map[index]
                }
            }
            map
        }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
    """.trimIndent().lines()
    check(testInput.let(::part1) == 13)
    check(testInput.let(::part2) == 30)

    val input = readInput("day04/Day04")
    input.let(::part1).also(::println).let { check(it == 23941) }
    input.let(::part2).also(::println).let { check(it == 5571760) }
}
