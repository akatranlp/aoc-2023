import kotlin.math.pow

data class Card(val id: Int, val winningNumbers: Set<Int>, val myNumbers: List<Int>)
data class CardResult(val id: Int, val numbers: Int)

fun main() {
    fun parseCards(input: List<String>): List<Card> {
        return input.mapIndexed { i, s ->
            s.split(":")[1].split("|").let {
                val winningNumbers = it[0].split(" ").filter { it != "" }.map { it.toInt() }.toSet()
                val myNumbers = it[1].split(" ").filter { it != "" }.map { it.toInt() }
                Card(i + 1, winningNumbers, myNumbers)
            }
        }
    }

    fun part1(input: List<String>): Int {
        val cards = parseCards(input)
        return cards.sumOf { card ->
            card.myNumbers.mapNotNull { if (it in card.winningNumbers) it else null }.let {
                (2.toDouble().pow((it.size - 1).toDouble())).toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        val cards = parseCards(input).map { card ->
            CardResult(card.id, card.myNumbers.map { it in card.winningNumbers }.filter { it }.size)
        }
        val map = mutableMapOf(*cards.map { it.id to 1 }.toTypedArray())

        for (cardResult in cards) {
            val entry = map[cardResult.id]!!
            for (j in 0..<entry) {
                val numbers = cardResult.numbers
                for (k in cardResult.id + 1..cardResult.id + numbers) {
                    if (k <= cards.size) {
                        map[k] = map[k]!! + 1
                    }
                }
            }
        }

        return map.values.sum()
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
    check(part1(testInput) == 13)

    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
