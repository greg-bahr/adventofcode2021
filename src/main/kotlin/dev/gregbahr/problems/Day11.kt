package dev.gregbahr.problems

data class Point(val x: Int, val y: Int) {
    fun neighbors(): List<Point> {
        return listOf(
            Point(x, y+1),
            Point(x, y-1),
            Point(x+1, y),
            Point(x-1, y),
            Point(x+1, y+1),
            Point(x-1, y-1),
            Point(x-1, y+1),
            Point(x+1, y-1)
        )
    }
}

class Day11(private val data: List<String>) {

    private val nums = data.map { it.chunked(1).map(String::toInt) }

    private operator fun List<List<Int>>.contains(point: Point): Boolean {
        return point.y in this.indices && point.x in this[point.y].indices
    }

    private class Octopus(val location: Point, var value: Int) {
        private var lastStepReset = -1
        private val stepsFlashed = hashSetOf<Int>()
        lateinit var neighbors: List<Octopus>

        fun flash(step: Int, start: Boolean = true): Int {
            if (!start && lastStepReset != step) {
                value++
            }

            if (step !in stepsFlashed && value > 9) {
                value = 0
                lastStepReset = step
                stepsFlashed += step

                return 1 + neighbors.sumOf { it.flash(step, false) }
            }

            return 0
        }
    }

    private fun parseInput(): List<Octopus> {
        val list = arrayListOf<Octopus>()
        val map = mutableMapOf<Point, Octopus>()

        for (i in nums.indices) {
            for (j in nums[i].indices) {
                val location = Point(j, i)
                val octopus = Octopus(location, nums[i][j])

                map[location] = octopus
                list += octopus
            }
        }

        for (octopus in list) {
            octopus.neighbors = octopus.location.neighbors().filter { it in nums }.map { map[it]!! }
        }

        return list
    }

    fun part1(): Int {
        val octopuses = parseInput()
        var flashes = 0

        for (i in 1..100) {
            octopuses.forEach { it.value++ }
            octopuses.forEach { flashes += it.flash(i) }
        }

        return flashes
    }

    fun part2(): Int {
        val octopuses = parseInput()
        var step = 0

        while (true) {
            step++
            var flashes = 0

            octopuses.forEach { it.value++ }
            octopuses.forEach { flashes += it.flash(step) }

            if (flashes == octopuses.size) {
                return step
            }
        }
    }
}
