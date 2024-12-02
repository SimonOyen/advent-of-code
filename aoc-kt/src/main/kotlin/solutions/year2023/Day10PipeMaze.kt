package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.solutions.year2023.Day10PipeMaze.Direction.*
import io.github.simonoyen.tools.Day


class Day10PipeMaze : Day {

    // | is a vertical pipe connecting north and south.
    // - is a horizontal pipe connecting east and west.
    // L is a 90-degree bend connecting north and east.
    // J is a 90-degree bend connecting north and west.
    // 7 is a 90-degree bend connecting south and west.
    // F is a 90-degree bend connecting south and east.
    internal enum class Pipe(val directions: List<Direction>, val literal: Char) {
        VERTICAL(TOP, BOTTOM, '|'),
        HORIZONTAL(LEFT, RIGHT, '-'),
        L(TOP, RIGHT, 'L'),
        J(TOP, LEFT, 'J'),
        SEVEN(LEFT, BOTTOM, '7'),
        F(RIGHT, BOTTOM, 'F');

        constructor(openingA: Direction, openingB: Direction, literal: Char) : this(listOf(openingA, openingB), literal)

        companion object {
            operator fun get(char: Char): Pipe = entries.first { it.literal == char }
        }
    }

    internal enum class Direction {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT;
    }

    internal val oppositeDirections = mapOf(
        TOP to BOTTOM,
        BOTTOM to TOP,
        LEFT to RIGHT,
        RIGHT to LEFT
    )

    internal data class Coordinate(val x: Int, val y: Int, val value: Pipe, var valid: Boolean = true)
    internal class Grid(val locations: Map<Pair<Int, Int>, Coordinate>) {
        fun getNeighboursOf(x: Int, y: Int): Map<Direction, Coordinate?> = mapOf(
            TOP to locations[x to y - 1],
            BOTTOM to locations[x to y + 1],
            LEFT to locations[x - 1 to y],
            RIGHT to locations[x + 1 to y]
        ).filter { it.value != null }

    }

    override fun partA(input: String): Number {
        val lines = input.lines()
        val parsed =
            lines.mapIndexed { y, line -> line.mapIndexed { x, char -> (x to y) to Coordinate(x, y, Pipe[char]) } }
                .flatten()
                .toMap()
        val grid = Grid(parsed)

        grid.locations.map { (xy, coordinate) ->
            val (x, y) = xy
            val neighbours = grid.getNeighboursOf(x, y)
            neighbours.forEach { (direction, coord) ->
                val c = coord!!
                if (direction in coordinate.value.directions && oppositeDirections[direction] !in c.value.directions) {
                    coordinate.valid = false
                }
            }
        }
        
        TODO()
    }

    override fun partB(input: String): Number {
        TODO()
    }


}
