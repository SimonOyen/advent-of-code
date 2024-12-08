package io.github.simonoyen.solutions.year2024

import io.github.simonoyen.tools.Day

object Day05PrintQueue : Day {
    override fun partA(input: String): Number {
        val (ruleSection, updateSection) = input.split("\n\n")
        val rules = setOf(*(ruleSection.split("\n").toTypedArray()))
        val updates = updateSection.split("\n").map { it.split(",").map { it.toInt() } }

        val valid = updates.filter { update ->
            for (first in 0..<update.lastIndex) {
                for (second in first + 1..update.lastIndex) {
                    val check = "${update[second]}|${update[first]}"
                    if (rules.contains(check)) {
                        return@filter false
                    }
                }
            }
            return@filter true
        }.sumOf { it[it.size / 2] }

        return valid
    }

    override fun partB(input: String): Number {
        val (ruleSection, updateSection) = input.split("\n\n")
        val rules = setOf(*(ruleSection.split("\n").toTypedArray()))
        val updates = updateSection.split("\n").map { it.split(",").map { it.toInt() } }

        var invalid = updates.findInvalidUpdates(rules)
        val previouslyInvalidUpdates =
            invalid.mapIndexedNotNull { index, (rule, _) -> if (rule != null) index else null }
        var reordered = invalid.reorderBrokenRule()

        invalid = updates.findInvalidUpdates(rules)
        while (invalid.any { it.first != null }) {
            reordered = invalid.reorderBrokenRule()
            invalid = reordered.findInvalidUpdates(rules)
        }

        val nowFixed =
            reordered.filterIndexed { index, update -> index in previouslyInvalidUpdates }.sumOf { it[it.size / 2] }
        return nowFixed
    }

    fun List<Pair<String?, List<Int>>>.reorderBrokenRule() = this.map { (brokenRule, update) ->
        if (brokenRule == null) {
            return@map update
        }
        val (firstRulePart, secondRulePart) = brokenRule.split("|").map { it.toInt() }

        update.map { if (it == firstRulePart) secondRulePart else if (it == secondRulePart) firstRulePart else it }
    }

    fun List<List<Int>>.findInvalidUpdates(rules: Set<String>): List<Pair<String?, List<Int>>> =
        this.mapNotNull { update ->
            for (first in 0..<update.lastIndex) {
                for (second in first + 1..update.lastIndex) {
                    val check = "${update[second]}|${update[first]}"
                    if (rules.contains(check)) {
                        return@mapNotNull check to update
                    }
                }
            }
            return@mapNotNull null to update
        }
}