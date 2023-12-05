package day05

import kotlin.io.path.Path
import kotlin.io.path.readText


data class Almanac(
    val seeds: List<Long>,
    val rest: List<List<Mapping>>,
)

data class Mapping(val destination: Long, val source: Long, val size: Long) {
    companion object {
        fun manyFromBlock(blockName: String, block: String): List<Mapping> {
            return block.removePrefix("$blockName map:\n").split("\n").filter { it.isNotBlank() }
                .map { fromLine(it) }
        }

        private fun fromLine(line: String): Mapping {
            return line.split(" ").filter { it.isNotBlank() }.map { it.toLong() }.let { (destination, source, size) ->
                Mapping(destination, source, size)
            }
        }
    }
}

fun main() {
    fun parseInput(input: String): Almanac {
        return input.split("\n\n").let { blocks ->
            val seeds = blocks[0].trim().split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }
            val seedToSoil = Mapping.manyFromBlock("seed-to-soil", blocks[1])
            val soilToFertilizer = Mapping.manyFromBlock("soil-to-fertilizer", blocks[2])
            val fertilizerToWater = Mapping.manyFromBlock("fertilizer-to-water", blocks[3])
            val waterToLight = Mapping.manyFromBlock("water-to-light", blocks[4])
            val lightToTemperature = Mapping.manyFromBlock("light-to-temperature", blocks[5])
            val temperatureToHumidity = Mapping.manyFromBlock("temperature-to-humidity", blocks[6])
            val humidityToLocation = Mapping.manyFromBlock("humidity-to-location", blocks[7])

            Almanac(
                seeds,
                listOf(
                    seedToSoil,
                    soilToFertilizer,
                    fertilizerToWater,
                    waterToLight,
                    lightToTemperature,
                    temperatureToHumidity,
                    humidityToLocation,
                )
            )
        }
    }

    fun seedToLocation(seed: Long, almanac: Almanac): Long {
        return almanac.rest.fold(seed) { acc, mappings ->
            mappings.forEach {
                if (acc in (it.source..<it.source + it.size)) {
                    return@fold it.destination + (acc - it.source)
                }
            }
            acc
        }
    }

    fun part1(input: String): Long {
        return input.let(::parseInput).also(::println).let { almanac ->
            almanac.seeds.map {
                seedToLocation(it, almanac)
            }.also(::println).min()
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = """
    seeds: 79 14 55 13

    seed-to-soil map:
    50 98 2
    52 50 48

    soil-to-fertilizer map:
    0 15 37
    37 52 2
    39 0 15

    fertilizer-to-water map:
    49 53 8
    0 11 42
    42 0 7
    57 7 4

    water-to-light map:
    88 18 7
    18 25 70

    light-to-temperature map:
    45 77 23
    81 45 19
    68 64 13

    temperature-to-humidity map:
    0 69 1
    1 0 69

    humidity-to-location map:
    60 56 37
    56 93 4
    """.trimIndent()
    check(testInput.let(::part1) == 35L)
    check(testInput.let(::part2) == 1L)

    val input = Path("src/day05/Day05.txt").readText()
    input.let(::part1).also(::println).let { check(it == 424490994L) }
    input.let(::part2).also(::println).let { check(it == 0L) }
}
