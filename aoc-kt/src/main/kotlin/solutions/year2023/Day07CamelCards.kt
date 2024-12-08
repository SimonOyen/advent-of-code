package io.github.simonoyen.solutions.year2023

import io.github.simonoyen.tools.Day

object Day07CamelCards : Day {
    override fun partA(input: String): Number {
        val hands = input.lines().map(Hand::fromInput)
        val handsByType = hands.groupBy { it.type }

        val sortedHands = handsByType.map { it.key to it.value.sorted() }.toMap()
        val ranks = sortedHands.toSortedMap().flatMap { (_, v) -> v.map { hand -> hand.bid } }
        return ranks.foldIndexed(0) { index, acc, bid -> acc + ((index + 1) * bid) }
    }

    // 251668199 too high
    // 251571597 too high
    override fun partB(input: String): Number {
        val hands = input.lines().map(Hand::fromInputWithJokers)
        val handsByType = hands.groupBy { it.type }

        val sortedHands = handsByType.map { it.key to it.value.sorted() }.toMap()
        val ranks = sortedHands.toSortedMap().flatMap { (_, v) -> v }
        val result = ranks.foldIndexed(0) { index, acc, hand -> acc + ((index + 1) * hand.bid) }
        return result
    }


    class Hand(private val cards: String, val bid: Int, val type: HandType, private val cardStrengths: List<Int>) :
        Comparable<Hand> {
        override fun compareTo(other: Hand): Int {
            if (this.cards == other.cards) {
                return 0
            }

            for (index: Int in this.cardStrengths.indices) {
                if (this.cardStrengths[index] == other.cardStrengths[index]) {
                    continue
                }
                return this.cardStrengths[index] - other.cardStrengths[index]
            }

            return 0
        }

        override fun toString(): String {
            return "$cards -> $type ($bid)"
        }

        companion object {
            fun fromInput(input: String): Hand {
                val (cards: String, bidString: String) = input.split(" ")

                return Hand(
                    cards,
                    bidString.toInt(),
                    determineHandTypeWithoutJokers(cards),
                    calculateHandStrength(cards)
                )
            }

            fun fromInputWithJokers(input: String): Hand {
                val (cards: String, bidString: String) = input.split(" ")

                return Hand(
                    cards,
                    bidString.toInt(),
                    determineHandTypeWithJokers(cards),
                    calculateHandStrengthWithJokers(cards)
                )
            }

            fun determineHandTypeWithoutJokers(cards: String): HandType {
                val grouped = cards.groupBy { it }
                return when {
                    grouped.size == 1 -> HandType.FIVE
                    grouped.any { it.value.size == 4 } -> HandType.FOUR
                    grouped.any { it.value.size == 3 } ->
                        when (grouped.size) {
                            2 -> HandType.FULL_HOUSE
                            else -> HandType.THREE
                        }

                    grouped.any { it.value.size == 2 } ->
                        when (grouped.size) {
                            3 -> HandType.TWO_PAIRS
                            else -> HandType.ONE_PAIR
                        }

                    else -> return HandType.HIGH_CARD
                }
            }

            fun determineHandTypeWithJokers(cards: String): HandType {
                if (cards.all { cards.first() == it }) {
                    return HandType.FIVE
                }

                val grouped = cards.groupBy { it }
                val groupedWithoutJokers = cards.filter { it != 'J' }.groupBy { it }
                val amountOfJokers = grouped.getOrDefault('J', emptyList()).size

                val typeBeforeUpgrades = when {
                    groupedWithoutJokers.any { it.value.size == 4 } -> HandType.FOUR
                    groupedWithoutJokers.any { it.value.size == 3 } ->
                        when {
                            groupedWithoutJokers.size == 2 && amountOfJokers == 0 -> HandType.FULL_HOUSE
                            else -> HandType.THREE
                        }

                    groupedWithoutJokers.any { it.value.size == 2 } ->
                        when {
                            groupedWithoutJokers.size == 3 && amountOfJokers == 0 -> HandType.TWO_PAIRS
                            groupedWithoutJokers.size == 2 && amountOfJokers == 1 -> HandType.TWO_PAIRS
                            else -> HandType.ONE_PAIR
                        }

                    else -> HandType.HIGH_CARD
                }

                val type = upgrade(typeBeforeUpgrades, amountOfJokers)

                return type
            }

            private val cardStrengthMapWithoutJokers = mapOf(
                'A' to 20,
                'K' to 19,
                'Q' to 18,
                'J' to 17,
                'T' to 10,
            )

            private val cardStrengthMapWithJokers = mapOf(
                'A' to 20,
                'K' to 19,
                'Q' to 18,
                'J' to 1,
                'T' to 10,
            )

            private val upgrades = mapOf(
                HandType.FOUR to HandType.FIVE,
                HandType.THREE to HandType.FOUR,
                HandType.TWO_PAIRS to HandType.FULL_HOUSE,
                HandType.ONE_PAIR to HandType.THREE,
                HandType.HIGH_CARD to HandType.ONE_PAIR
            )

            private fun upgrade(type: HandType, times: Int): HandType {
                if (times == 0) {
                    return type
                }

                return upgrade(upgrades[type]!!, times - 1)
            }

            fun calculateHandStrength(cards: String): List<Int> =
                cards.map { cardStrengthMapWithoutJokers[it] ?: it.digitToInt() }

            fun calculateHandStrengthWithJokers(cards: String): List<Int> =
                cards.map { cardStrengthMapWithJokers[it] ?: it.digitToInt() }
        }
    }

    enum class HandType {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIRS,
        THREE,
        FULL_HOUSE,
        FOUR,
        FIVE,
    }
}
