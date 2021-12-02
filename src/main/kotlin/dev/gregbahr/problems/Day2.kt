package dev.gregbahr.problems

class Day2(private val data: List<String>) {

    fun part1(): Int {
        var depth = 0
        var position = 0

        data.map { it.split(" ") }.forEach {
            val num = it[1].toInt()

            when (it[0]) {
                "forward" -> position += num
                "down" -> depth += num
                "up" -> depth -= num
            }
        }

        return depth * position
    }

    fun part2(): Int {
        var depth = 0
        var position = 0
        var aim = 0

        data.map { it.split(" ") }.forEach {
            val num = it[1].toInt()

            when (it[0]) {
                "forward" -> {
                    position += num; depth += aim * num
                }
                "down" -> aim += num
                "up" -> aim -= num
            }
        }

        return depth * position
    }
}
