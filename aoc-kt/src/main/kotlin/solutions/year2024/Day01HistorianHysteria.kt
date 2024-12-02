package io.github.simonoyen.solutions.year2024

import io.github.simonoyen.tools.Day
import kotlin.math.abs

class Day01HistorianHysteria : Day {
    override fun partA(input: String): Number {
        val (firstColumn, secondColumn) = input.lines().getColumns()

        return firstColumn.sorted()
            .zip(secondColumn.sorted())
            .sumOf { (first, second) -> abs(first - second) }
    }

    override fun partB(input: String): Number {
        val (firstColumn, secondColumn) = input.lines().getColumns()

        val countedValues = firstColumn
            .map { valueFromFirstCol ->
                valueFromFirstCol to secondColumn.count { valueFromSecondCol -> valueFromFirstCol == valueFromSecondCol }
            }

        return countedValues.sumOf { (id, count) -> id * count }
    }

    private fun List<String>.getColumns(): Pair<List<Int>, List<Int>> = this
        .map { it.split("   ") }
        .map { it.first().toInt() to it.last().toInt() }
        .unzip()
}
