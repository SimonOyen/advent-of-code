package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.solutions.year2023.Day08HauntedWasteland.Instruction.LEFT
import io.github.simonoyen.solutions.year2023.Day08HauntedWasteland.Instruction.RIGHT
import io.github.simonoyen.tools.Day
import kotlin.math.abs

class Day08HauntedWasteland : Day {

    override fun partA(input: String): Number {
        val (instructions, map) = input.split("\n\n")

        val start = Crossing.createMap(map)["AAA"]!!

        val person = Person(instructions, start)
        var moves = 0

        while (!person.atGoal) {
            moves = person.move()
            // println("Moved to ${person.currentLocation}")
        }

        // println("Arrived at ${person.currentLocation}!")

        return moves
    }

    // 195199225 too low
    // 721683698 too low
    override fun partB(input: String): Number {
        val (instructions, map) = input.split("\n\n")

        val starts = Crossing.createMap(map).filter { it.key.endsWith("A") }.values.toList()
        val ghost = Ghost(instructions, starts)
        ghost.move()

        val circleSizes = starts.map {
            val start = it.name
            val circle = mutableListOf<Crossing>()
            var circleSize = 0
            val firstInstruction = ghost.getInstruction(1)
            val firstStep = it.getDirection(firstInstruction)
            var current = it

            while (!current.name.endsWith("Z")) {
                current = current.getDirection(ghost.getInstruction(circleSize))!!
                circle.add(current)
                circleSize++
            }
            println("$start: $circleSize\tGoal: $current\tFirst Step: $firstStep")
            circleSize
        }

        do {
            ghost.move()
            // println("Moved to ${person.currentLocation}")
        } while (ghost.notAtGoalCount() > 0)

        // println("Arrived at ${person.currentLocation}!")

        val result = ghost.atGoalAfter.fold(1) { a, b -> lcm(a, b) }
        return result
    }

    fun lcm(a: Int, b: Int): Int {
        return abs(a * b) / gcd(a, b)
    }

    fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    class Crossing(
        val name: String,
        private var left: Crossing?,
        private var right: Crossing?,
        val isGoal: Boolean,
        var isDummy: Boolean = false
    ) {
        override fun toString(): String = "$name = (${left?.name}, ${right?.name})"
        fun getDirection(instruction: Instruction): Crossing? = if (instruction == LEFT) left else right

        companion object {
            private val crossings = mutableMapOf<String, Crossing>()

            fun createMap(map: String): MutableMap<String, Crossing> {
                crossings.clear()
                val crossingStrings = map.lines()

                crossingStrings.forEach {
                    val (name, directions) = it.split(" = ")
                    // print("Now at: $it, ")

                    if (!crossings.containsKey(name)) {
                        // println("not yet created....")
                        crossings[name] = create(name, directions)
                    } else {
                        // println("already exists.... (${crossings[name]?.name} dummy=${crossings[name]?.isDummy})")
                        val (leftString, rightString) = getDirections(directions)
                        val left = crossings.getOrElse(leftString) { createDummy(leftString) }
                        val right = crossings.getOrElse(rightString) { createDummy(rightString) }
                        val crossing = crossings[name]!!
                        crossing.left = left
                        crossing.right = right
                        crossing.isDummy = false
                    }
                }

                val doneAfterFirstPass = crossings.count { !it.value.isDummy }

                println("$doneAfterFirstPass/${crossings.size}")

                return crossings
            }

            fun create(name: String, directions: String): Crossing {
                val (leftString, rightString) = getDirections(directions)

                val left = crossings.getOrElse(leftString) { createDummy(leftString) }
                val right = crossings.getOrElse(rightString) { createDummy(rightString) }

                return Crossing(name, left, right, name == "ZZZ")
            }

            fun createDummy(name: String): Crossing {
                val dummy = Crossing(name, null, null, name == "ZZZ", true)
                crossings[name] = dummy
                return dummy
            }

            private fun getDirections(directions: String) = directions
                .replace("(", "")
                .replace(")", "")
                .split(", ")

        }
    }

    enum class Instruction {
        LEFT,
        RIGHT
    }

    class Person(private val instructions: String, startingLocation: Crossing) {
        private var movementCounter = 0
        var currentLocation = startingLocation
        var atGoal = startingLocation.isGoal

        fun move(): Int {
            currentLocation = currentLocation.getDirection(getNextInstruction())!!
            atGoal = currentLocation.isGoal
            return ++movementCounter
        }

        private fun getNextInstruction(): Instruction {
            val instruction = when (instructions[movementCounter % instructions.length]) {
                'L' -> LEFT
                else -> RIGHT
            }
            return instruction
        }

    }

    class Ghost(private val instructions: String, startingLocations: List<Crossing>) {
        private var movementCounter = 0
        var currentLocations = startingLocations
        var atGoalAfter = mutableListOf<Int>()
        fun notAtGoalCount(): Int = currentLocations.size

        fun move(): Int {
            val notAtGoalCount = notAtGoalCount()
            if (notAtGoalCount > 0) {
                println("Not at Goal: $notAtGoalCount/${currentLocations.size}")
                val instruction = getNextInstruction()
                currentLocations = currentLocations.map { it.getDirection(instruction)!! }
                currentLocations.filter { it.name.endsWith("Z") }.forEach { atGoalAfter.add(movementCounter + 1) }
                currentLocations = currentLocations.filter { !it.name.endsWith("Z") }
                movementCounter++
            }
            return movementCounter
        }

        fun getNextInstruction(): Instruction = getInstruction(movementCounter)

        fun getInstruction(index: Int): Instruction {
            return when (instructions[index % instructions.length]) {
                'L' -> LEFT
                else -> RIGHT
            }
        }

    }
}
