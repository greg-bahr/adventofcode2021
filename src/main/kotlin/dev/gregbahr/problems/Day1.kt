package dev.gregbahr.problems

class Day1(private val data: List<String>) {

    fun part1(): Int {
        val input = data.map { it.toInt() }

        return input.windowed(size = 2, step = 1).count {
            it[1] > it[0]
        }
    }

    fun part2(): Int {
        val input = data.map { it.toInt() }

        return input.windowed(size = 3, step = 1).map { it.sum() }.windowed(size = 2, step = 1).count {
            it[1] > it[0]
        }
    }
}
