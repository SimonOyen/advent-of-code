package io.github.simonoyen.solutions.year2024

import io.github.simonoyen.tools.Day
import kotlin.math.pow
import kotlin.math.sqrt

object Day08ResonantCollinearity : Day {
    override fun partA(input: String): Number {
        val antennas = input.lines().mapIndexed { y, text ->
            Regex("[^.]").findAll(text).map { antennaSighting ->
                Antenna(
                    AntennaPosition(antennaSighting.range.first, y),
                    antennaSighting.value
                )
            }.toList()
        }.flatten().toSet()

        val antennasBySignal = antennas.groupBy { antenna -> antenna.signal }
        val pairings = antennasBySignal.map { (signal, antennas) ->
            antennas.mapIndexed { antennaNr, antenna ->
                antennasBySignal[signal]!!.drop(antennaNr + 1).mapNotNull { otherAntenna ->
                    if (antenna != otherAntenna) ADP(antenna, otherAntenna) else null
                }
            }.flatten()
        }.flatten()

        val size = AntennaPosition(input.lines().first().length, input.lines().size)
        val antinodes = pairings.map { it.antiNodes }.flatten().toSet()
            .filter { it.position.x >= 0 && it.position.x < size.x && it.position.y >= 0 && it.position.y < size.y }

        // drawGrid(size, antennas + antinodes)

        return antinodes.size
    }

    override fun partB(input: String): Number {
        val antennas = input.lines().mapIndexed { y, text ->
            Regex("[^.]").findAll(text).map { antennaSighting ->
                Antenna(
                    AntennaPosition(antennaSighting.range.first, y),
                    antennaSighting.value
                )
            }.toList()
        }.flatten().toSet()

        val antennasBySignal = antennas.groupBy { antenna -> antenna.signal }
        val pairings = antennasBySignal.map { (signal, antennas) ->
            antennas.mapIndexed { antennaNr, antenna ->
                antennasBySignal[signal]!!.drop(antennaNr + 1).mapNotNull { otherAntenna ->
                    if (antenna != otherAntenna) HarmonicADP(200, antenna, otherAntenna) else null
                }
            }.flatten()
        }.flatten()

        val size = AntennaPosition(input.lines().first().length, input.lines().size)
        val antinodes = pairings.map { it.antiNodes }.flatten().toSet()
            .filter { it.position.x >= 0 && it.position.x < size.x && it.position.y >= 0 && it.position.y < size.y }

        // drawGrid(size, antennas + antinodes)

        return antinodes.size
    }

    private fun drawGrid(
        size: AntennaPosition,
        everything: Set<Antenna>
    ) {
        (0..<size.y).forEach { y ->
            (0..<size.x).forEach { x ->
                val antinodeThere = everything.find { it.position == AntennaPosition(x, y) && it.signal == "#" }
                when {
                    antinodeThere != null -> print(antinodeThere.signal)
                    else -> print(everything.find { it.position == AntennaPosition(x, y) }?.signal ?: ".")
                }
            }
            println()
        }
    }
}

private data class Antenna(val position: AntennaPosition, val signal: String)

private data class AntennaPosition(val x: Int, val y: Int) {
    override fun toString(): String = "($x,$y)"

    companion object {
        fun distance(p1: AntennaPosition, p2: AntennaPosition): Double =
            sqrt((p2.x - p1.x).toDouble().pow(2.0) + (p2.y - p2.x).toDouble().pow(2.0))
    }
}

private data class ADP(val antenna1: Antenna, val antenna2: Antenna) {
    val pointX = AntennaPosition(2 * antenna1.position.x - antenna2.position.x, 2 * antenna1.position.y - antenna2.position.y)
    val pointY = AntennaPosition(2 * antenna2.position.x - antenna1.position.x, 2 * antenna2.position.y - antenna1.position.y)

    val antiNodes = listOf(Antenna(pointX, "#"), Antenna(pointY, "#"))

    val signal: String = antenna1.signal
}

private data class HarmonicADP(val maxAmount: Int, val antenna1: Antenna, val antenna2: Antenna) {
    val pointsInXDirection =
        (2..maxAmount).map {
            Antenna(
                AntennaPosition(
                    it * antenna1.position.x - (it - 1) * antenna2.position.x,
                    it * antenna1.position.y - (it - 1) * antenna2.position.y
                ), "#"
            )
        }

    val pointsInYDirection =
        (2..maxAmount).map {
            Antenna(AntennaPosition(it * antenna2.position.x - (it - 1) * antenna1.position.x, it * antenna2.position.y - (it - 1) * antenna1.position.y), "#")
        }

    val antiNodes = listOf(antenna1.copy(signal = "#"), antenna2.copy(signal = "#")) + pointsInXDirection + pointsInYDirection
}