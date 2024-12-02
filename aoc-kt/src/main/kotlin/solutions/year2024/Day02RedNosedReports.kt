package io.github.simonoyen.solutions.year2024

import io.github.simonoyen.tools.Day
import kotlin.math.abs

class Day02RedNosedReports : Day {
    override fun partA(input: String): Number {
        val filtered = input.parseNumbers()
            .map { it.calculateChanges() }
            .filter { it.isValid() }

        return filtered.size
    }

    override fun partB(input: String): Number {
        val numbers = input.parseNumbers()

        val filtered = numbers.filter { row ->
            when {
                row.calculateChanges().isValid() -> true
                else -> (0..row.size).any { dropIndex ->
                    row.filterIndexed { index, _ -> index != dropIndex }
                        .calculateChanges()
                        .isValid()
                }
            }
        }

        return filtered.size
    }

    private fun List<Int>.isValid(): Boolean {
        val validRange = this.all { abs(it) >= 1 && abs(it) <= 3 }
        val allDecreasing = this.all { it < 0 }
        val allIncreasing = this.all { it > 0 }

        return validRange && (allDecreasing || allIncreasing)
    }

    private fun String.parseNumbers(): List<List<Int>> = this.lines().map { it.split(" ").map { it.toInt() } }

    private fun List<Int>.calculateChanges(): List<Int> = this.zip(this.drop(1) + 0)
        .map { (value, nextValue) -> value - nextValue }
        .dropLast(1)

}
