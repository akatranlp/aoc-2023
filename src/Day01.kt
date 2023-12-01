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

    fun findFirstDigit(input: String): Int {
        for (i in input.indices) {
            if (input[i].isDigit()) {
                return input[i].digitToInt()
            }
            val sub = input.substring(i)
            for (num in lookupTable.keys) {
                if (sub.startsWith(num)) {
                    return lookupTable[num]!!
                }
            }
        }
        return 0
    }

    fun findLastDigit(input: String): Int {
        for (i in input.indices.reversed()) {
            if (input[i].isDigit()) {
                return input[i].digitToInt()
            }
            val sub = input.substring(0, i + 1)
            for (num in lookupTable.keys) {
                if (sub.endsWith(num)) {
                    return lookupTable[num]!!
                }
            }
        }
        return 0
    }

    fun part2(input: List<String>): Int {
        return input.fold(0) { acc, s ->
            val firstDigit = findFirstDigit(s)
            val lastDigit = findLastDigit(s)
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
