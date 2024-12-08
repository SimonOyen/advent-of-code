package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.solutions.year2023.Day05IfYouGiveASeedAFertilizer.Categories.*
import io.github.simonoyen.tools.Day
import io.github.simonoyen.tools.parseNumbersToList

// seed - soil - fertilizer - water - light - temperature - humidity - location
object Day05IfYouGiveASeedAFertilizer : Day {
    enum class Categories {
        SEED,
        SOIL,
        FERTILIZER,
        WATER,
        LIGHT,
        TEMPERATURE,
        HUMIDITY,
        LOCATION;
    }

    val conversions = mapOf(
        SEED to SOIL,
        SOIL to FERTILIZER,
        FERTILIZER to WATER,
        WATER to LIGHT,
        LIGHT to TEMPERATURE,
        TEMPERATURE to HUMIDITY,
        HUMIDITY to LOCATION
    )

    data class Conversion(val from: Categories, val to: Categories, val offset: Long)

    fun solve(input: String) {
        val lines = input.lines()
        val seeds = lines.first().split(": ").last().split(" ").map { it.toLong() }
        val conversionCatalogue = input.split("\n\n").drop(1)
        val arstaresitn = conversionCatalogue.map { conversion ->
            {
                conversion.split("\n")
            }
        }


        val category = Categories.SEED


    }


    override fun partA(input: String): Long {
        val lines = input.lines()
        val seeds = lines[0].split(": ")[1].split(" ").map { it.toLong() }
        val mapSections = input.split("\n\n").drop(1)
        val mappings = mapSections.associate {
            val l = it.split("\n")
            val mapping = Mapping(l[0], l.drop(1).map { numbers -> parseNumbersToList(numbers) })
            mapping.from to mapping
        }


        val results = seeds.mapIndexed { index, seed ->
            println("seed $index of ${seeds.lastIndex}")
            var currentCategory = "seed"
            var value = seed

            while (currentCategory != "location") {
                value = mappings[currentCategory]?.getMapped(value) ?: -1
                currentCategory = mappings[currentCategory]?.to ?: "error"

                if (value == -1L || currentCategory == "error") {
                    break
                }
            }
            value
        }

        return results.min()
    }

    override fun partB(input: String): Long {
        return -1
    }

    class Mapping(name: String, mappingList: List<List<Long>>) {
        val from: String
        val to: String
        val mapping: MutableList<Pair<LongRange, LongRange>>

        init {
            val splitName = name.split("-")
            from = splitName[0]
            to = splitName[2].removeSuffix(" map:")

            mapping = mappingList.map {
                val fromStart = it[1]
                val toStart = it[0]
                val length = it[2]
                val f = LongRange(fromStart, fromStart + length)
                val t = LongRange(toStart, toStart + length)
                f to t
            }.sortedBy { it.first.first }.toMutableList()

        }

        fun getMapped(number: Long): Long {
            val (key, value) =
                mapping.toList().find { (key, _) -> key.contains(number) } ?: Pair(
                    LongRange.EMPTY,
                    LongRange.EMPTY
                )

            if (key == LongRange.EMPTY) {
                return number
            }

            val indexOfNumber = key.indexOf(number)
            return value.first + indexOfNumber
        }
    }
}
