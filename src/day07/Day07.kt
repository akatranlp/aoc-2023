package day07

import utils.readInput

enum class HandType {
    HIGH_CARD,
    PAIR,
    TWO_PAIR,
    THREE,
    FULL_HOUSE,
    FOUR,
    FIVE,
}

data class Hand(val cards: String, val bid: Int, val joker: Boolean) : Comparable<Hand> {
    private val handValues = if (joker) "AKQT98765432J".reversed() else "AKQJT98765432".reversed()

    private val values: List<Int> by lazy {
        cards.map { handValues.indexOf(it) + 1 }
    }

    val value: Int by lazy {
        values.fold(this.handType.ordinal) { acc, i -> acc.shl(4).or(i) }
    }

    val handType: HandType by lazy {
        val map = values.groupingBy { it }.eachCount()
        val sorted = map.toList().sortedByDescending { it.second }

        if (!joker) {
            when (sorted[0].second) {
                5 -> HandType.FIVE
                4 -> HandType.FOUR
                3 -> if (sorted[1].second == 2) HandType.FULL_HOUSE else HandType.THREE
                2 -> if (sorted[1].second == 2) HandType.TWO_PAIR else HandType.PAIR
                else -> HandType.HIGH_CARD
            }
        } else {
            val jokers = map[1] ?: 0
            when (sorted[0].second) {
                5 -> HandType.FIVE
                // there are 0, 1 or 4 jokers in this
                4 -> if (jokers > 0) HandType.FIVE else HandType.FOUR
                // There are 0, 1, 2 or 3 jokers in this
                3 -> when (jokers) {
                    // With 3 Joker you look if we have a pair, if not its only 4
                    3 -> if (sorted[1].second == 2) HandType.FIVE else HandType.FOUR
                    // with 2 joker we have 5 because we are not triple
                    2 -> HandType.FIVE
                    // with 1 joker we have 4 because we are not triple
                    1 -> HandType.FOUR
                    // with 0 Jokers we look if we have a pair
                    else -> if (sorted[1].second == 2) HandType.FULL_HOUSE else HandType.THREE
                }
                // There are 0, 1, or 2 jokers in this
                2 -> when (jokers) {
                    // We have two pairs one of them are the jokers
                    2 -> if (sorted[1].second == 2) HandType.FOUR else HandType.THREE
                    1 -> if (sorted[1].second == 2) HandType.FULL_HOUSE else HandType.THREE
                    // no joker look for second pair
                    else -> if (sorted[1].second == 2) HandType.TWO_PAIR else HandType.PAIR
                }

                1 -> if (jokers == 0) HandType.HIGH_CARD else HandType.PAIR
                else -> HandType.HIGH_CARD
            }
        }
    }

    override fun compareTo(other: Hand): Int {
        if (this.value < other.value) return -1
        if (this.value > other.value) return 1
        return 0
    }

    companion object {
        fun fromLine(input: String, joker: Boolean): Hand {
            return input.split(" ").let { (cardsText, bidText) ->
                Hand(cardsText, bidText.toInt(), joker)
            }
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .filter { it.isNotBlank() }
            .map { Hand.fromLine(it, false) }
            .sorted()
            // .onEach { println("$it, ${it.handType}, ${it.value}") }
            .mapIndexed { i, hand -> hand.bid * (i + 1) }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .filter { it.isNotBlank() }
            .map { Hand.fromLine(it, true) }
            .sorted()
            // .onEach { println("$it, ${it.handType}, ${it.value}") }
            .mapIndexed { i, hand -> hand.bid * (i + 1) }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        32T3K 765
        T55J5 684
        KK677 28
        KTJJT 220
        QQQJA 483
    """.trimIndent().lines()
    check(testInput.let(::part1) == 6440)
    check(testInput.let(::part2) == 5905)

    val input = readInput("day07/Day07")
    input.let(::part1).also(::println).let { check(it == 251136060) }
    input.let(::part2).also(::println).let { check(it == 249400220) }
}
