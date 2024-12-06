package io.github.simonoyen.solutions.year2024

import io.github.simonoyen.tools.Day
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

class Day06GuardGallivant : Day {
    override fun partA(input: String): Number {
        val obstructions = input.lines().map { Regex("#").findAll(it).map { it.range.first }.toSet() }
            .mapIndexed { y, obstructionsInRow -> obstructionsInRow.map { Position(it, y) } }
            .flatten()

        val borders = Position(input.lines().first().length, input.lines().size)
        var guard = input.lines().map { Regex("\\^").find(it)?.range?.first }.mapIndexedNotNull { y, x -> if (x == null) null else Guard(x, y) }.first()
        val visited = mutableSetOf<Position>()

        while (guard.position.x >= 0 && guard.position.x < borders.x && guard.position.y >= 0 && guard.position.y < borders.y) {
            visited.add(guard.position)
            // look ahead
            while (obstructions.contains(guard.move().position)) {
                guard = guard.turn()
            }
            // move
            guard = guard.move()
        }
        return visited.size
    }

    // 16096 too high
    // 15787 too high
    override fun partB(input: String): Number {
        val obstructions = input.lines().map { Regex("#").findAll(it).map { it.range.first }.toSet() }
            .mapIndexed { y, obstructionsInRow -> obstructionsInRow.map { Position(it, y) } }
            .flatten()
            .toSet()

        val borders = Position(input.lines().first().length, input.lines().size)
        var guard = input.lines().map { Regex("\\^").find(it)?.range?.first }.mapIndexedNotNull { y, x -> if (x == null) null else Guard(x, y) }.first()
        val visited = mutableSetOf<Position>()

        val parallelUniverses = (0..<borders.x).map { x -> (0..borders.y).map { y -> obstructions + Position(x, y) } }.flatten().toSet()


        val alternatives: List<Boolean> = runBlocking {
            parallelUniverses.map {
                async() {
                    var myGuard = guard.copy()
                    var sizeHistory = mutableListOf(0)

                    while (myGuard.position.x >= 0 && myGuard.position.x < borders.x && myGuard.position.y >= 0 && myGuard.position.y < borders.y) {
                        visited.add(guard.position)
                        sizeHistory.add(visited.size)

                        if (sizeHistory.takeLast(10000).toSet().size == 1) {
                            println("Maybe looping?")
                            return@async true
                        }

                        sizeHistory = sizeHistory.takeLast(10000).toMutableList()
                        // look ahead
                        while (it.contains(myGuard.move().position)) {
                            myGuard = myGuard.turn()
                        }
                        // move
                        myGuard = myGuard.move()
                    }

                    println("Exited the area!")
                    false
                }
            }.awaitAll()
        }

        return alternatives.count { it }
    }
}

private enum class GuardDirection(val xOffset: Int, val yOffset: Int) {
    UP(0, -1), RIGHT(1, 0), DOWN(0, 1), LEFT(-1, 0)
}

private data class Guard(val position: Position, val direction: GuardDirection = GuardDirection.UP) {
    constructor(x: Int, y: Int) : this(Position(x, y))

    fun turn() = this.copy(direction = GuardDirection.entries[(this.direction.ordinal + 1) % 4])
    fun move() = this.copy(position = Position(x = position.x + direction.xOffset, y = position.y + direction.yOffset))
}

private data class Position(val x: Int, val y: Int) {
    override fun toString(): String = "($x, $y)"
}