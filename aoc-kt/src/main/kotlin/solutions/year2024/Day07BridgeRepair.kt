package io.github.simonoyen.solutions.year2024

import io.github.simonoyen.tools.Day

object Day07BridgeRepair : Day {
    override fun partA(input: String): Number {
        val tasks = input.lines().map { line -> line.split(": ") }
            .map { task -> Task(task.first().toLong(), task.last().split(" ").map { it.toLong() }) }

        val results = tasks.filter { it.trySolve(2) }.sumOf { it.result }

        return results
    }

    override fun partB(input: String): Number {
        val tasks = input.lines().map { line -> line.split(": ") }
            .map { task -> Task(task.first().toLong(), task.last().split(" ").map { it.toLong() }) }

        val results = tasks.filter { it.trySolve(3) }.sumOf { it.result }

        return results
    }
}

private data class Task(val result: Long, val factors: List<Long>) {
    private fun add(current: Long, other: Long): Long = current + other
    private fun mult(current: Long, other: Long): Long = current * other
    private fun concat(current: Long, other: Long): Long = "$current$other".toLong()

    val operations = listOf(::add, ::mult, ::concat)

    fun trySolve(radix: Int): Boolean {
        val fullMask = List(factors.lastIndex) { (radix - 1).toString() }.joinToString("")
        val upperLimit = fullMask.toLong(radix)
        val masks = (0..upperLimit).map { it.toString(radix).padStart(fullMask.length, '0') }
        val operationList =
            masks.map { mask -> mask.toCharArray().map { digit -> operations[digit.digitToInt()] } }

        val pairs = operationList.any { operation ->
            val combinations = factors.drop(1).zip(operation)

            return@any combinations.fold(factors.first()) { current, (factor, operator) ->
                operator(current, factor)
            } == result
        }

        return pairs
    }
}