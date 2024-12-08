package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.tools.Day
import io.github.simonoyen.tools.parseNumbersToIntList

object Day09MirageMaintenance : Day {

    override fun partA(input: String): Number {
        val lines = input.lines()
        val numbers = lines.map { parseNumbersToIntList(it) }

        val results = numbers.map {
            // Erstmal nur eine Zeile
            var differences = calculateDifferences(listOf(it))
            while (!differences.last().all { d -> d == 0 }) {
                differences = calculateDifferences(differences)
            }

            differences.sumOf { extrapolation -> extrapolation.last() }
        }

        return results.sum()
    }

    override fun partB(input: String): Number {
        val lines = input.lines()
        val numbers = lines.map { parseNumbersToIntList(it) }

        val results = numbers.map {
            // Erstmal nur eine Zeile
            var differences = calculateDifferences(listOf(it))
            while (!differences.last().all { d -> d == 0 }) {
                differences = calculateDifferences(differences)
            }

            val extrapolatedValues = mutableListOf<Int>()
            differences.reversed().forEachIndexed { index, ints ->
                if (index == 0) extrapolatedValues.add(0)
                else extrapolatedValues.add(ints.first() - extrapolatedValues.last())
            }

            extrapolatedValues.last()
        }

        return results.sum()
    }

    private fun calculateDifferences(numbers: List<List<Int>>): List<List<Int>> =
        numbers + listOf(
            numbers.last()
                .mapIndexed { index, number -> if (index < numbers.last().lastIndex) numbers.last()[index + 1] - number else null }
                .filterNotNull()
        )

}
