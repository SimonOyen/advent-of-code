package io.github.simonoyen.solutions.year2024

import io.github.simonoyen.tools.Day

class Day03MullItOver : Day {
    override fun partA(input: String): Number {
        val regex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")

        val instructions = regex.findAll(input).map { it.value }
        val numbers = instructions.extractNumbers()

        val result = numbers.sumOf { it.first() * it.last() }

        return result
    }

    // 77.126.144 too high
    // 96.384.317 auch falsch
    //104.796.023
    // 99.667.955
    // 96.384.317
    // 34.992.362 auch falsch
    // 70.879.585
    // 67.269.798
    override fun partB(input: String): Number {
        val regex = Regex("mul\\(\\d+,\\d+\\)|do\\(\\)|don't\\(\\)")

        val matches = regex.findAll(input)

        var result = 0
        var flag = true
        matches.forEach { match ->
            when {
                match.value.contains("don't()") -> {
                    flag = false
                }

                match.value.contains("do()") -> {
                    flag = true
                }

                else -> {
                    if (flag) {
                        val numbers = match.value.replace("mul(", "").replace(")", "").split(",").map { it.toInt() }
                        result = result + (numbers.first() * numbers.last())
                    }
                }
            }
        }


        return result
    }

    private fun Sequence<String>.extractNumbers(): Sequence<List<Int>> =
        map { it.replace("mul(", "").replace(")", "").split(",").map { it.toInt() } }
}