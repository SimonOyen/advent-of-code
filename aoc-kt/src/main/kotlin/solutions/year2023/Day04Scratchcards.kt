package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.tools.Day
import io.github.simonoyen.tools.parseNumbersToIntList
import kotlin.math.min
import kotlin.math.pow

object Day04Scratchcards : Day {
    override fun partA(input: String): Number = input.lines().sumOf { 2.0.pow(numberOfWins(it) - 1).toInt() }

    override fun partB(input: String): Number {
        val lines = input.lines()
        val winsPerCard = lines.map(::numberOfWins)
        val cards = mutableMapOf<Int, Int>()

        for (index: Int in winsPerCard.indices.reversed()) {
            val wins = winsPerCard[index]
            val remaining = winsPerCard.lastIndex - index
            val range = IntRange(index, index + min(remaining, wins))
            cards[index] = 1 + range.sumOf { cards[it] ?: 0 }
        }

        return cards.map { it.value }.sum()
    }


    private fun numberOfWins(cardLine: String): Int {
        val (_, numbers) = cardLine.split(":")               // _:"Card 1" numbers:"41 48 83 86 17 | 83 86  6 31 17  9 48 53"
        val (winning, hand) = numbers.split(" | ")    // winning:"41 48 83 86 17" hand:"83 86  6 31 17  9 48 53"
        val winningNumbers = parseNumbersToIntList(winning).toSet()   // [41, 48, 83, 86, 17]
        val actualNumbers = parseNumbersToIntList(hand)               // [83, 86, 6, 31, 17, 9, 48, 53]
        val intersection = actualNumbers.intersect(winningNumbers) // [83, 86, 17, 48]
        return intersection.size
    }

    /*
    fun partBRecursive(input: String): Int {
        val lines = input.lines()
        val winsPerCard = lines.map(::numberOfWins)
        val cards = mutableMapOf<Int, RecCard>()
        for (index: Int in winsPerCard.indices.reversed()) {
            val wins = winsPerCard[index]
            val remaining = winsPerCard.lastIndex - index
            val range = IntRange(index, index + min(remaining, wins))
            cards[index] = RecCard(range.map { cards[it] }.toList().filterNotNull())
        }

        return cards.map { it.value.generatesCards() }.sum()
    }
     */

    /*
    class RecCard(private val wins: List<RecCard>) {
        fun generatesCards(): Int {
            return if (wins.isEmpty()) {
                1
            } else {
                1 + wins.sumOf {
                    it.generatesCards()
                }
            }
        }

        override fun toString(): String {
            return wins.size.toString()
        }
    }
     */
}
