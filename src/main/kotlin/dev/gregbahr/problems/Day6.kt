package dev.gregbahr.problems

import java.math.BigInteger

class Day6(private val data: String) {

    fun solve(daysToSimulate: Int): BigInteger {
        val nums = data.split(",").map { it.toInt() }
        val map = nums.groupingBy { it }.eachCount().mapValues { BigInteger.valueOf(it.value.toLong()) }.toMutableMap()
            .withDefault { BigInteger.ZERO }.apply {
            for (i in (1 until daysToSimulate)) {
                val zeroes = getValue(i)

                this[i] = BigInteger.ZERO
                this[i + 7] = getValue(i + 7) + zeroes
                this[i + 9] = getValue(i + 9) + zeroes
            }
        }

        return map.values.sum()
    }

    private fun MutableCollection<BigInteger>.sum(): BigInteger {
        return this.reduce { acc, bigInteger -> acc + bigInteger }
    }
}
