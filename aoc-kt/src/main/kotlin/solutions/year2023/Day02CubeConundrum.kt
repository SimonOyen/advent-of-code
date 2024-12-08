package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.tools.Day

object Day02CubeConundrum : Day {
    override fun partA(input: String): Number = createGames(input).filter {
        it.reds.max() <= 12 && it.greens.max() <= 13 && it.blues.max() <= 14
    }.sumOf { it.index }

    override fun partB(input: String): Number =
        createGames(input).sumOf { it.reds.max() * it.greens.max() * it.blues.max() }

    private fun createGames(input: String) =
        input.lines().mapIndexed { index, line ->
            val roundString = line.split(":")[1]
            Game(
                index + 1,
                roundString.extractDice("red"),
                roundString.findColor("green").map { it.getCount() },
                roundString.findColor("blue").map { it.getCount() }
            )
        }

    private fun String.findColor(color: String): List<String> =
        split(";").map { Regex("\\d+ $color").find(it)?.value ?: "0 $color" }

    private fun String.getCount(): Int = split(" ").first().toInt()

    private fun String.extractDice(color: String) = this.findColor(color).map { it.getCount() }

    data class Game(val index: Int, val reds: List<Int>, val greens: List<Int>, val blues: List<Int>)
}
