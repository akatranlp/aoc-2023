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

data class Hand(val values: List<Int>, val cards: String, val type: HandType, val bid: Int) : Comparable<Hand> {
    val value: Int by lazy {
        values.addFirst(this.type.ordinal)
        values.reduce { acc, i -> acc.shl(4).or(i) }
    }

    companion object {
        fun fromLinePart1(input: String): Hand {
            val part1CardValues = mapOf(
                'A' to 14,
                'K' to 13,
                'Q' to 12,
                'J' to 11,
                'T' to 10,
                '9' to 9,
                '8' to 8,
                '7' to 7,
                '6' to 6,
                '5' to 5,
                '4' to 4,
                '3' to 3,
                '2' to 2
            )

            return input.split(" ").let { (cardsText, bidText) ->
                val bid = bidText.toInt()
                val cards = cardsText.map { part1CardValues[it]!! }
                val array = Array(16) { 0 }
                cards.forEach {
                    array[it] += 1
                }
                array.sortDescending()

                val handType = when (array[0]) {
                    5 -> HandType.FIVE
                    4 -> HandType.FOUR
                    3 -> if (array[1] == 2) HandType.FULL_HOUSE else HandType.THREE
                    2 -> if (array[1] == 2) HandType.TWO_PAIR else HandType.PAIR
                    else -> HandType.HIGH_CARD
                }

                Hand(cards, cardsText, handType, bid)
            }
        }

        fun fromLinePart2(input: String): Hand {
            val part2CardValues = mapOf(
                'A' to 14,
                'K' to 13,
                'Q' to 12,
                'T' to 10,
                '9' to 9,
                '8' to 8,
                '7' to 7,
                '6' to 6,
                '5' to 5,
                '4' to 4,
                '3' to 3,
                '2' to 2,
                'J' to 1,
            )

            return input.split(" ").let { (cardsText, bidText) ->
                val bid = bidText.toInt()
                val cards = cardsText.map { part2CardValues[it]!! }

                val mutableMap = mutableMapOf(*part2CardValues.values.map { it to 0 }.toTypedArray())

                cards.forEach {
                    mutableMap[it] = mutableMap[it]!! + 1
                }

                val sorted = mutableMap.toList().sortedByDescending { it.second }

                val jokers = mutableMap[1]!!
                val handType = when (sorted[0].second) {
                    5 -> {
                        HandType.FIVE
                    }

                    4 -> {
                        // there are 0, 1 or 4 jokers in this
                        if (jokers > 0) HandType.FIVE else HandType.FOUR
                    }

                    3 -> {
                        // There are 0, 1, 2 or 3 jokers in this
                        when (jokers) {
                            3 -> {
                                // With 3 Joker you look if we have a pair, if not its only 4
                                if (sorted[1].second == 2) HandType.FIVE else HandType.FOUR
                            }

                            2 -> {
                                // with 2 joker we have 5 because we are not triple
                                HandType.FIVE
                            }

                            1 -> {
                                // with 1 joker we have 4 because we are not triple
                                HandType.FOUR
                            }

                            else -> {
                                // with 0 Jokers we look if we have a pair
                                if (sorted[1].second == 2) HandType.FULL_HOUSE else HandType.THREE
                            }
                        }
                    }

                    2 -> {
                        // There are 0, 1, or 2 jokers in this
                        when (jokers) {
                            2 -> {
                                // We have two pairs one of them are the jokers
                                if (sorted[1].second == 2) {
                                    HandType.FOUR
                                } else {
                                    HandType.THREE
                                }
                            }

                            1 -> {
                                if (sorted[1].second == 2) {
                                    HandType.FULL_HOUSE
                                } else {
                                    HandType.THREE
                                }
                            }

                            else -> {
                                // no joker look for second pair
                                if (sorted[1].second == 2) HandType.TWO_PAIR else HandType.PAIR
                            }
                        }
                    }

                    1 -> {
                        if (jokers == 0) HandType.HIGH_CARD else HandType.PAIR
                    }

                    else -> {
                        HandType.HIGH_CARD
                    }
                }

                Hand(cards, cardsText, handType, bid)
            }
        }
    }

    override fun compareTo(other: Hand): Int {
        if (this.type.ordinal < other.type.ordinal) return 1
        if (this.type.ordinal > other.type.ordinal) return -1

        for (i in this.values.indices) {
            if (this.values[i] > other.values[i]) return 1
            if (this.values[i] < other.values[i]) return -1
        }
        return 0
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.filter { it.isNotBlank() }.map { Hand.fromLinePart1(it) }.sortedBy { it.value }.also(::println)
            .mapIndexed { i, hand ->
                hand.bid * (i + 1)
            }.also(::println).sum().also(::println)
    }

    fun part2(input: List<String>): Int {
        return input.filter { it.isNotBlank() }.map { Hand.fromLinePart2(it) }.sortedBy { it.value }.also(::println)
            .mapIndexed { i, hand ->
                hand.bid * (i + 1)
            }.sum().also(::println)
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
