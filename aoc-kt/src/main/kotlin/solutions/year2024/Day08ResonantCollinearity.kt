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

        val size = AntennaPosition(input.lines().first().length, input.lines().size)

        val firstAntenna = antennas.first()
        val sameSignalAntennas = antennas.drop(1).filter { it.signal == firstAntenna.signal }

        val distancesToAllPoints = sameSignalAntennas.map { currentAntenna ->
            (0..size.x).map { x ->
                (0..size.y).map { y ->
                    val inspectionPoint = AntennaPosition(x, y)
                    val distanceFirstAntenna = AntennaPosition.distance(firstAntenna.position, inspectionPoint)
                    val distanceCurrentAntenna = AntennaPosition.distance(currentAntenna.position, inspectionPoint)

                    if (distanceFirstAntenna == distanceCurrentAntenna * 2 || distanceFirstAntenna * 2 == distanceCurrentAntenna) {
                        println("${inspectionPoint}: ${firstAntenna.position} = $distanceFirstAntenna     ${currentAntenna.position} = $distanceCurrentAntenna")
                    }
                }
            }
        }
        
        println(antennas)
        return 0
    }


    override fun partB(input: String): Number {
        TODO()
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