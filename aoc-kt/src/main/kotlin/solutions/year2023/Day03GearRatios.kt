package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.tools.Day
import kotlin.math.max
import kotlin.math.min

object Day03GearRatios : Day {
    override fun partA(input: String): Number {
        val lines = input.lines()
        val padded = pad(lines)
        val numbersPerLine = padded.map { Regex("\\d+").findAll(it).toList() }

        return numbersPerLine.mapIndexed { index, numbers ->
            numbers.filter {
                val range = IntRange(max(0, it.range.first - 1), min(padded[index].lastIndex, it.range.last + 1))
                val inFront = padded[index][range.first] == '.'
                val inRear = padded[index][range.last] == '.'

                val onTop = range.fold(true) { acc, i ->
                    acc && padded[index - 1][i] == '.'
                }
                val onBottom = range.fold(true) { acc, i ->
                    acc && padded[index + 1][i] == '.'
                }

                !(inFront && inRear && onTop && onBottom)
            }.map { it.value.toInt() }
        }.flatten().sum()
    }

    //467..114..
    //...*......
    //..35..633.
    //......#...
    //617*......
    //.....+.58.
    //..592.....
    //......755.
    //...$.*....
    //.664.598..


    override fun partB(input: String): Number {
        val lines = input.lines()
        val padded = pad(lines)
        val numbersPerLine = padded.map { Regex("\\d+").findAll(it).toList() }

        return numbersPerLine.asSequence().mapIndexed { index, numbers ->
            numbers.map {
                val intValue = it.value.toInt()
                val gearHits: MutableList<GearHit> = mutableListOf()
                val range = IntRange(max(0, it.range.first - 1), min(padded[index].lastIndex, it.range.last + 1))
                listOf(range.first, range.last).forEach { position ->
                    if (padded[index][position] == '*') {
                        gearHits.add(GearHit(index, position, intValue))
                    }
                }

                range.forEach { column ->
                    listOf(1, -1).forEach { position ->
                        if (padded[index + position][column] == '*') {
                            gearHits.add(GearHit(index + position, column, intValue))
                        }
                    }
                }
                gearHits.toList()
            }.flatten()
        }.flatten()
            .groupBy { it.line to it.column }
            .filter { it.value.size == 2 }
            .map { it.value[0].number * it.value[1].number }.sum()
    }

    private fun pad(input: List<String>): List<String> {
        val paddedLines = input.map { ".$it$." }
        val padding = List(paddedLines[0].length) { '.' }.joinToString("")
        return listOf(padding) + paddedLines + listOf(padding)
    }

    data class GearHit(val line: Int, val column: Int, val number: Int)
}
