package day08

import utils.readInput

val RIGHT = 1
val LEFT = -1

data class Node(val id: String, val leftId: String, val rightId: String) {
    companion object {
        fun fromLine(input: String): Node {
            return Node(input.substring(0..2), input.substring(7..9), input.substring(12..14))
        }
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        val instructions = input[0].map { if (it == 'R') RIGHT else LEFT }.toTypedArray()
        val nodes = input.drop(2).map { Node.fromLine(it) }
        val nodesMap = nodes.associateBy { it.id }

        var currentId = nodes.first().id
        var currentInstructionIndex = 0
        var counter = 0L
        while (currentId != "ZZZ") {
            val instruction = instructions[currentInstructionIndex]
            val node = nodesMap[currentId]!!
            currentId = if (instruction == RIGHT) {
                node.rightId
            } else {
                node.leftId
            }
            counter++
            currentInstructionIndex = (currentInstructionIndex + 1) % instructions.size
        }
        return counter
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
        LLR
        
        AAA = (BBB, BBB)
        BBB = (AAA, ZZZ)
        ZZZ = (ZZZ, ZZZ)
    """.trimIndent().lines()
    check(testInput.let(::part1) == 6L)
    // check(testInput.let(::part2) == 1)

    val input = readInput("day08/Day08")
    input.let(::part1).also(::println).let { check(it == 0L) }
    input.let(::part2).also(::println).let { check(it == 0) }
}
