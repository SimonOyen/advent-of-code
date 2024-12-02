package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.tools.Day

class Day01Trebuchet : Day {
    private val numberStrings = mapOf(
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

    override fun partA(input: String): Number = input
        .lines()
        .map { line -> line.mapNotNull { character -> character.digitToIntOrNull() } }
        .sumOf { numbers -> "${numbers.first()}${numbers.last()}".toInt() }

    override fun partB(input: String): Number = partA(replaceNumberStrings(input))

    private fun replaceNumberStrings(input: String): String {
        var replacedInput: String = input
        numberStrings.forEach { replacedInput = replacedInput.replace(it.key, "${it.key}${it.value}${it.key}") }
        return replacedInput
    }
}
