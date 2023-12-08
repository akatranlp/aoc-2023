package day08

import utils.readInput
import java.math.BigInteger

const val RIGHT = 'R'
const val LEFT = 'L'

data class Node(val id: String, val leftId: String, val rightId: String) {

    companion object {
        fun fromLine(input: String): Node {
            return Node(input.substring(0..2), input.substring(7..9), input.substring(12..14))
        }
    }
}

fun traverseNodes(
    startingId: String,
    instructions: String,
    nodes: Map<String, Node>,
    predicate: (String) -> Boolean,
): Long {
    var currentId = startingId
    var counter = 0L
    while (predicate(currentId)) {
        instructions.forEach {
            val node = nodes[currentId]!!
            when (it) {
                RIGHT -> currentId = node.rightId
                LEFT -> currentId = node.leftId
            }
            counter++
        }
    }
    return counter
}

fun main() {
    fun part1(input: List<String>): Long {
        return input
            .drop(2)
            .map { Node.fromLine(it) }
            .associateBy { it.id }
            .let { nodes ->
                traverseNodes("AAA", input[0], nodes) { it != "ZZZ" }
            }
    }

    fun part2(input: List<String>): Long {
        return input
            .drop(2)
            .map { Node.fromLine(it) }
            .associateBy { it.id }
            .let { nodes ->
                nodes
                    .filterKeys { it.last() == 'A' }
                    .map {
                        traverseNodes(it.key, input[0], nodes) { it.last() != 'Z' }
                    }
                    .let(::lcm)
            }
    }


    // test if implementation meets criteria from the description, like:
    val testInput1 = """
        RL

        AAA = (BBB, CCC)
        BBB = (DDD, EEE)
        CCC = (ZZZ, GGG)
        DDD = (DDD, DDD)
        EEE = (EEE, EEE)
        GGG = (GGG, GGG)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent().lines()
    val testInput2 = """
        LLR
        
        AAA = (BBB, BBB)
        BBB = (AAA, ZZZ)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent().lines()
    check(testInput1.let(::part1) == 2L)
    check(testInput2.let(::part1) == 6L)

    val testInput3 = """
        LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)
    """.trimIndent().lines()

    check(testInput3.let(::part2) == 6L)

    val input = readInput("day08/Day08")
    input.let(::part1).also(::println).let { check(it == 18023L) }
    input.let(::part2).also(::println).let { check(it == 14449445933179L) }
}

private fun lcm(input: List<Long>): Long {
    return input.map { BigInteger.valueOf(it) }.reduce { acc, i -> acc * i / acc.gcd(i) }.toLong()
}
