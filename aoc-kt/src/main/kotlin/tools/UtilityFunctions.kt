package io.github.simonoyen.tools

fun splitUpList(input: String): List<String> = input
    .trim()
    .replace("  ", " ")
    .split(" ")

fun parseNumbersToList(input: String): List<Long> = splitUpList(input).map { it.toLong() }
fun parseNumbersToIntList(input: String): List<Int> = splitUpList(input).map { it.toInt() }
