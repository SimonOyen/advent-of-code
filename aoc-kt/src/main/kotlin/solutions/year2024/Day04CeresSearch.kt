package io.github.simonoyen.solutions.year2024

import io.github.simonoyen.tools.Day

class Day04CeresSearch : Day {
    override fun partA(input: String): Number {
        val grid = Grid(input.lines().map { it.toCharArray().toList() })

        // ListOf(Cell to SetOf(Direction to Cell, Direction to Cell, Direction to Cell), Cell to SetOf(Direction to Cell, Direction to Cell, Direction to Cell))
        val cellsWithNeighbors = grid.data.map { it to grid.neighborsWithDirection(it) }

        val filteredForX = cellsWithNeighbors.map { (cell, neighborsWithDirection) ->
            if (cell.value != 'X') {
                return@map 0
            }

            val msInRange = neighborsWithDirection.filter { (_, neighbor) -> neighbor.value == 'M' }

            if (msInRange.isEmpty()) {
                return@map 0
            }

            val neighborsOfMs = msInRange.map { it.first to grid.neighborsWithDirection(it.second) }
            val neighborsOfMsWithA =
                neighborsOfMs.map { (directionOfM, neighborsOfM) -> neighborsOfM.filter { (direction, neighborOfM) -> direction == directionOfM && neighborOfM.value == 'A' } }
                    .flatten()

            if (neighborsOfMsWithA.isEmpty()) {
                return@map 0
            }

            val neighborsOfAs = neighborsOfMsWithA.map { it.first to grid.neighborsWithDirection(it.second) }
            val neighborsOfAsWithS =
                neighborsOfAs.map { (directionOfA, neighborsOfA) -> neighborsOfA.filter { (direction, neighborOfA) -> direction == directionOfA && neighborOfA.value == 'S' } }
                    .flatten()

            if (neighborsOfAsWithS.isEmpty()) {
                return@map 0
            }

            neighborsOfAsWithS.size
        }.sum()

        return filteredForX
    }

    override fun partB(input: String): Number {
        val grid = Grid(input.lines().map { it.toCharArray().toList() })

        // ListOf(Cell to SetOf(Direction to Cell, Direction to Cell, Direction to Cell), Cell to SetOf(Direction to Cell, Direction to Cell, Direction to Cell))
        val cellsWithNeighbors = grid.data.map { it to grid.diagonalNeighborsWithDirection(it) }

        val searchCross = cellsWithNeighbors.map { (cell, neighborsWithDirection) ->
            if (cell.value != 'A') {
                return@map 0
            }

            val msInRange = neighborsWithDirection.filter { (_, neighbor) -> neighbor.value == 'M' }
            val ssInRange = neighborsWithDirection.filter { (_, neighbor) -> neighbor.value == 'S' }

            if (msInRange.size < 2 || ssInRange.size < 2) {
                return@map 0
            }

            val sOnOtherSideOfM =
                neighborsWithDirection.filter { (direction, neighbor) -> msInRange.any { (directionOfM, _) -> direction == directionOfM.opposite() && neighbor.value == 'S' } }

            if (sOnOtherSideOfM.size == 2) {
                return@map 1
            }

            0
        }.sum()

        return searchCross
    }
}

private class Grid<T>(var data: Set<Cell<T>>, val size: Pair<Int, Int>) {
    constructor(input: List<List<T>>) : this(
        input.mapIndexed { y, row -> row.mapIndexed { x, c -> Cell(x, y, c) } }
            .flatten()
            .toSet(), input[0].size to input.size)

    fun neighborsWithDirection(currentCell: Cell<T>): Set<Pair<Direction, Cell<T>>> =
        Direction.entries.mapNotNull { direction ->
            val neighborCell = data.find { cell -> currentCell.x + direction.xOffset == cell.x && currentCell.y + direction.yOffset == cell.y }
            if (neighborCell == null) {
                null
            } else {
                direction to neighborCell
            }
        }.toSet()

    fun diagonalNeighborsWithDirection(currentCell: Cell<T>): Set<Pair<Direction, Cell<T>>> =
        Direction.entries.filter { it.diagonally }.mapNotNull { direction ->
            val neighborCell = data.find { cell -> currentCell.x + direction.xOffset == cell.x && currentCell.y + direction.yOffset == cell.y }
            if (neighborCell == null) {
                null
            } else {
                direction to neighborCell
            }
        }.toSet()

    override fun toString(): String {
        val builder = StringBuilder()
        for (y in 0..<size.second) {
            for (x in 0..<size.first) {
                val cell = data.find { it.x == x && it.y == y }
                builder.append(if (cell?.enabled == true) cell.value.toString() else ".")
            }
            builder.append('\n')
        }

        return builder.toString()
    }
}

private enum class Direction(val xOffset: Int, val yOffset: Int, val diagonally: Boolean = false) {
    TOP(0, -1),
    BOTTOM(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    TOP_LEFT(-1, -1, true),
    TOP_RIGHT(1, -1, true),
    BOTTOM_LEFT(-1, 1, true),
    BOTTOM_RIGHT(1, 1, true);

    fun opposite() = when (this) {
        TOP -> BOTTOM
        BOTTOM -> TOP
        LEFT -> RIGHT
        RIGHT -> LEFT
        TOP_LEFT -> BOTTOM_RIGHT
        TOP_RIGHT -> BOTTOM_LEFT
        BOTTOM_LEFT -> TOP_RIGHT
        BOTTOM_RIGHT -> TOP_LEFT
    }
}

private data class Cell<T>(val x: Int, val y: Int, val value: T, val enabled: Boolean = true)