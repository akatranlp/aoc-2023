package day05

import kotlin.io.path.Path
import kotlin.io.path.readText


//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//---------------Disclaimer this code doesn't work for part2--------------------
//-------------I spent a couple of hours to work with my approach---------------
//---------------------Tomorrow i look at some solutions------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------


data class Almanac(
    val seeds: List<Long>,
    val rest: List<List<Mapping>>,
)

data class Mapping(val source: Long, val destination: Long, val size: Long) {
    val sourceRange get() = (source..<source + size)
    val destinationRange get() = (destination..<destination + size)


    companion object {
        fun manyFromBlock(blockName: String, block: String): List<Mapping> {
            return block.removePrefix("$blockName map:\n").split("\n").filter { it.isNotBlank() }
                .map { fromLine(it) }.sortedBy { it.source }
        }

        private fun fromLine(line: String): Mapping {
            return line.split(" ").filter { it.isNotBlank() }.map { it.toLong() }.let { (destination, source, size) ->
                Mapping(source, destination, size)
            }
        }
    }
}

fun UNREACHABLE(): Nothing {
    throw Error("UNREACHABLE")
}

// Not fully and correctly implemented
fun findMappingsInOtherMap(
    sourceIndex: Long,
    destinationIndex: Long,
    size: Long,
    map: List<Mapping>
): Pair<Long, List<Mapping>> {
    var currentSourceIndex = sourceIndex
    var currentIndex = destinationIndex
    var currentSize = size

    val resultList = mutableListOf<Mapping>()

    for (mapping in map) {
        if (currentIndex < mapping.source) {
            if (currentIndex + currentSize < mapping.source) {
                resultList.add(Mapping(currentSourceIndex, currentIndex, currentSize))

                currentSourceIndex += currentIndex
                currentIndex += currentSize
                currentSize = 0
                break
            } else if (currentIndex + currentSize >= mapping.source && currentIndex + currentSize < mapping.source + mapping.size) {
                val thisSize = mapping.source - currentIndex
                resultList.add(Mapping(currentSourceIndex, currentIndex, thisSize))

                currentSourceIndex += thisSize
                currentIndex += thisSize
                currentSize -= thisSize

                resultList.add(Mapping(currentSourceIndex, mapping.destination, currentSize))
                currentSourceIndex += currentSize
                currentIndex += currentSize
                currentSize = 0
                break
            } else if (currentIndex + currentSize >= mapping.source && currentIndex + currentSize >= mapping.source + mapping.size) {
                val thisSize = mapping.source - currentIndex
                resultList.add(Mapping(currentSourceIndex, currentIndex, thisSize))

                currentSourceIndex += thisSize
                currentIndex = mapping.source
                currentSize -= thisSize

                resultList.add(Mapping(currentSourceIndex, mapping.destination, mapping.size))

                currentSourceIndex += mapping.size
                currentIndex += mapping.size
                currentSize -= mapping.size
                continue
            } else {
                UNREACHABLE()
            }
        } else if (currentIndex == mapping.source) {
            if (currentSize <= mapping.size) {
                resultList.add(Mapping(currentSourceIndex, mapping.destination, currentSize))

                currentSourceIndex += currentSize
                currentIndex += currentSize
                currentSize = 0
                break
            } else {
                resultList.add(Mapping(currentSourceIndex, mapping.destination, mapping.size))

                currentSourceIndex += mapping.size
                currentIndex += mapping.size
                currentSize -= mapping.size
                continue
            }
        } else if (currentIndex > mapping.source) {
            if (currentIndex >= mapping.source + mapping.size) {
                continue
            } else if (currentIndex < mapping.source + mapping.size) {
                if (currentIndex + currentSize <= mapping.source + mapping.size) {

                    resultList.add(
                        Mapping(
                            currentSourceIndex,
                            currentIndex - mapping.source + mapping.destination,
                            currentSize
                        )
                    )

                    currentIndex += currentSize
                    currentSourceIndex += currentSize
                    currentSize = 0
                    break
                } else {
                    // TODO()
                    val thisSize = (mapping.source + mapping.size) - currentSize
                    resultList.add(Mapping(currentSourceIndex, mapping.destination, thisSize))

                }
            } else {
                TODO()
            }
        } else {
            UNREACHABLE()
        }
    }

    return Pair(currentSourceIndex - sourceIndex, resultList)
}


// Not fully and correctly implemented
fun List<Mapping>.combine(other: List<Mapping>): List<Mapping> {
    val list = this
    return buildList<Mapping> {
        var currentSourceIndex = 0L

        for (thisMapping in list) {
            if (thisMapping.source > currentSourceIndex) {

                val (steps, newMappings) = findMappingsInOtherMap(
                    currentSourceIndex,
                    currentSourceIndex,
                    thisMapping.source - currentSourceIndex,
                    other
                )
                this.addAll(newMappings)

                if (steps != thisMapping.source - currentSourceIndex) {
                    println("HERe")
                    break
                }
                currentSourceIndex += steps
            }

            assert(thisMapping.source == currentSourceIndex)

            var currentSteps = 0L

            var counter = 0

            var steps: Long
            do {
                val (localSteps, newMappings) = findMappingsInOtherMap(
                    currentSourceIndex,
                    thisMapping.destination + currentSteps,
                    thisMapping.size - currentSteps,
                    other
                )

                steps = localSteps
                currentSourceIndex += steps
                currentSteps += steps
                this.addAll(newMappings)

                counter++
                if (counter > 3) {
                    println("$thisMapping, $steps, $currentSteps, $currentSourceIndex, $this")
                    TODO()
                }
            } while (steps != 0L)

            if (currentSourceIndex < thisMapping.source + thisMapping.size) {

                this.add(
                    Mapping(
                        currentSourceIndex,
                        thisMapping.destination + currentSteps,
                        thisMapping.size - currentSteps
                    )
                )
                currentSourceIndex += thisMapping.size - currentSteps
            }
        }
    }.filter { it.size != 0L }.sortedBy { it.source }
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


            /* "seed-to-soil map:"
            0 0 50
            50 52 48
            98 50 2

            soil-to-fertilizer map:
            0 39 15
            15 0 37
            52 37 2

            ->
            0 -> 0 -> 39 15    -> 15 15 35
            15 -> 15 -> 0 35   -> first seed mapping 50 52 48

            50 -> 52 -> 37 2   -> 52 54 46
            52 -> 54 -> 54 46  -> next seed mapping 98 50 2

            98 -> 50 -> 0+35 2
             */

            // doesn't work
            /*
            val seedToFertilizer =
                seedToSoil
                    .combine(soilToFertilizer)
                    .also(::println)
                    .combine(fertilizerToWater)
                    .also(::println)
                    .combine(waterToLight)
                    .also(::println)
                    .combine(lightToTemperature)
                    .also(::println)
                    .combine(temperatureToHumidity)
                    .also(::println)
                    .combine(humidityToLocation)
                    .also(::println)
                */

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
                if (acc in it.sourceRange) {
                    return@fold it.destination + (acc - it.source)
                }
            }
            acc
        }
    }

    fun part1(input: String): Long {
        return input.let(::parseInput).let { almanac ->
            almanac.seeds.map {
                seedToLocation(it, almanac)
            }.also(::println).min()
        }
    }

    fun part2(input: String): Long {
        return input.let(::parseInput).let { almanac ->
            almanac.seeds.chunked(2).flatMap { (seedStart, size) ->
                (seedStart..<seedStart + size).map {
                    it// seedToLocation(it, almanac)
                }
            }.also(::println).min()
        }
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
    // check(testInput.let(::part2) == 1L)

    val input = Path("src/day05/Day05.txt").readText()
    input.let(::part1).also(::println).let { check(it == 424490994L) }
    //input.let(::part2).also(::println).let { check(it == 0L) }
}
