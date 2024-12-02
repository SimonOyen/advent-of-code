package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.tools.Day

class Day06WaitForIt : Day {
    override fun partA(input: String): Number {
        val (times, distances) = input.lines()
            .map { Regex("\\d+").findAll(it).map { r -> r.value.toLong() }.toList() }

        val possibilitiesPerRace = times.mapIndexed { raceIndex, time -> LongRange(distances[raceIndex] / time, time) }
        return possibilitiesPerRace.mapIndexed { raceIndex, possibilities ->
            possibilities.map { speed ->
                val raceDuration = times[raceIndex]
                val possibleDistance = (raceDuration - speed) * speed
                possibleDistance > distances[raceIndex]
            }.filter { it }.size
        }.reduce { acc, i -> acc * i }
    }


    // Time:              53837288
    // Distance:   333163512891532
    // d/t = 6188341
    // t - (d/t) = 47648947
    override fun partB(input: String): Number {
        val shortInput = input.replace(Regex("\\s+"), "").replace("D", "\nD")
        val (time, distance) = shortInput.lines()
            .map { Regex("\\d+").find(it) }.map { it!!.value.toLong() }

        val range = LongRange(distance / time, time)
        val first = range.first { (time - it) * it > distance }
        val last = range.last { (time - it) * it > distance }

        return last - first + 1
    }
}
