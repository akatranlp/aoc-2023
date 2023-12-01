fun main() {
    fun part1(input: List<String>): Int {
        return input.fold(0) { acc, s ->
            s.filter { it.isDigit() }.let {
                (it.first() + "" + it.last()).toInt()
            } + acc
        }
    }

    val lookupTable = mapOf(
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9
    )

    fun part2(input: List<String>): Int {
        return input.fold(0) { acc, s ->
            var firstDigit = -1
            var lastDigit = -1
            var leftIndex = 0
            var rightIndex = s.length

            loop@ while (leftIndex != rightIndex) {
                val sub = s.substring(leftIndex, rightIndex)
                if (firstDigit == -1) {
                    if (sub.first().isDigit()) {
                        firstDigit = sub.first().digitToInt()
                    } else {
                        for (num in lookupTable.keys) {
                            if (sub.startsWith(num)) {
                                firstDigit = lookupTable[num]!!
                                continue@loop
                            }
                        }
                        leftIndex++
                    }
                }
                if (lastDigit == -1) {
                    if (sub.last().isDigit()) {
                        lastDigit = sub.last().digitToInt()
                    } else {
                        for (num in lookupTable.keys) {
                            if (sub.endsWith(num)) {
                                lastDigit = lookupTable[num]!!
                                continue@loop
                            }
                        }
                        rightIndex--
                    }
                }
                if (firstDigit != -1 && lastDigit != -1) {
                    break
                }
            }
            val sum = firstDigit * 10 + lastDigit
            sum + acc
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
