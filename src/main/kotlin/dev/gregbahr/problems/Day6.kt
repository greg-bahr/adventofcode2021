package dev.gregbahr.problems

class Day6(private val data: String) {

    fun solve(daysToSimulate: Int): Long {
        val nums = data.split(",").map { it.toInt() }
        val map = nums.groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap().withDefault { 0L }.apply {
            for (i in (1 until daysToSimulate)) {
                val zeroes = getValue(i)

                this[i] = 0L
                this[i + 7] = getValue(i + 7) + zeroes
                this[i + 9] = getValue(i + 9) + zeroes
            }
        }

        return map.values.sum()
    }
}
