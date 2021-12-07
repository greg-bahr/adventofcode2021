package dev.gregbahr.problems

import kotlin.math.absoluteValue

class Day7(private val data: String) {

    fun part1(): Int {
        val nums = data.split(",").map { it.toInt() }

        return (1..nums.maxOrNull()!!).map { nums.map { num -> (num-it).absoluteValue }.sum() }.minOrNull() ?: 0
    }

    fun part2(): Int {
        val nums = data.split(",").map { it.toInt() }

        return (1..nums.maxOrNull()!!).map { nums.map { num -> (1..(num-it).absoluteValue).sum() }.sum() }.minOrNull() ?: 0
    }
}
