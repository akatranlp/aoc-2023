package day03

import utils.println
import utils.readInput

data class Number(val row: Int, val start: Int, val end: Int, val number: Int)

val neighbours = setOf(-1 to -1, -1 to 0, -1 to 1, 0 to -1, 0 to 1, 1 to -1, 1 to 0, 1 to 1)

fun main() {
    fun parseNumbers(input: List<String>): List<Number> {
        return buildList {
            for ((row, line) in input.withIndex()) {
                var startIndex = line.length
                for ((col, c) in line.withIndex()) {
                    if (c.isDigit()) {
                        if (col < startIndex) startIndex = col
                    }
                    if (col == line.length - 1 || !c.isDigit()) {
                        var endIndex = col
                        if (col == line.length - 1 && c.isDigit()) endIndex += 1

                        if (startIndex < endIndex) {
                            this.add(Number(row, startIndex, endIndex, line.substring(startIndex, endIndex).toInt()))
                        }

                        startIndex = line.length
                    }
                }
            }
        }
    }

    fun parseSymbols(input: List<String>): Map<Pair<Int, Int>, Char> {
        return buildMap {
            for ((row, line) in input.withIndex()) {
                for ((col, c) in line.withIndex()) {
                    if (!c.isDigit() && c != '.') {
                        this[(row to col)] = c
                    }
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val numbers = parseNumbers(input)
        val symbols = parseSymbols(input)

        return numbers.map {
            for (col in it.start..<it.end) {
                for ((dr, dc) in neighbours) {
                    val newRow = it.row + dr
                    val newCol = col + dc

                    if (newRow to newCol in symbols) {
                        return@map it.number
                    }
                }
            }
            null
        }.filterNotNull().sum()
    }

    fun part2(input: List<String>): Int {
        val numbers = parseNumbers(input)
        val symbols = parseSymbols(input).filterValues { it == '*' }

        return symbols.map { (coords, _) ->
            val neighbourNumbers = mutableListOf<Number>()
            val (row, col) = coords
            for ((dr, dc) in neighbours) {
                val newRow = row + dr
                val newCol = col + dc
                for (num in numbers) {
                    if (newRow != num.row) continue
                    if (newCol in num.start..<num.end) {
                        if (num !in neighbourNumbers)
                            neighbourNumbers.add(num)
                    }
                }
            }
            if (neighbourNumbers.size == 2) {
                return@map neighbourNumbers.map { it.number }.reduce(Int::times)
            }
            null
        }.filterNotNull().sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        467..114..
        ...*......
        ..35..633.
        ......#...
        617*......
        .....+.58.
        ..592.....
        ......755.
        ...${'$'}.*....
        .664.598..
    """.trimIndent().lines()
    check(testInput.let(::part1) == 4361)
    check(testInput.let(::part2) == 467835)

    val input = readInput("day03/Day03")
    input.let(::part1).also(::println).let { check(it == 546563) }
    input.let(::part2).also(::println).let { check(it == 91031374) }
}
