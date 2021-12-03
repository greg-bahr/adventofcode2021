package dev.gregbahr.problems

import dev.gregbahr.helpers.Resources

@ExperimentalStdlibApi
class Day3(private val data: List<String>) {

    private fun mostCommonBit(position: Int, nums: List<String>): Int {
        val groups = nums.map { it[position].digitToInt() }.groupingBy { it }.eachCount()
        return if (groups[1]!! >= groups[0]!!) 1 else 0
    }

    fun part1(): Int {
        val mostCommonBits = data[0].indices.map { mostCommonBit(it, data) }

        val gamma = mostCommonBits.joinToString("").toInt(2)
        val epsilon = mostCommonBits.map { it xor 1 }.joinToString("").toInt(2)

        return gamma * epsilon
    }

    fun part2(): Int {
        var nums = data.toList()
        var oxygen = 0
        var co2 = 0

        for (i in data[0].indices) {
            val bit = mostCommonBit(i, nums)

            nums = nums.filter {
                it[i].digitToInt() == bit
            }

            if (nums.size == 1) {
                oxygen = nums[0].toInt(2)
                break
            }
        }

        nums = data.toList()
        for (i in data[0].indices) {
            val bit = mostCommonBit(i, nums) xor 1

            nums = nums.filter {
                it[i].digitToInt() == bit
            }

            if (nums.size == 1) {
                co2 = nums[0].toInt(2)
                break
            }
        }

        return oxygen * co2
    }
}

@ExperimentalStdlibApi
fun main() {
    Day3(Resources.resourceAsList("inputs/day3.txt")).apply {
        println(part1())
        println(part2())
    }
}
