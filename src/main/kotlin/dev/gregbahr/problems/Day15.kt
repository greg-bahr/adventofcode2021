package dev.gregbahr.problems

import java.util.*

class Day15(data: List<String>) {
    private val cave = data.map { it.chunked(1).map(String::toInt) }

    fun part1(): Int {
        operator fun List<List<Int>>.contains(point: Point): Boolean {
            return point.x >= 0 && point.x < cave.size && point.y >= 0 && point.y < cave.size
        }

        val distance = mutableMapOf<Point, Int>()
        val points = PriorityQueue { p1: Point, p2: Point -> distance[p1]!! - distance[p2]!! }

        for (i in cave.indices) {
            for (j in cave.indices) {
                distance[Point(j, i)] = Int.MAX_VALUE
            }
        }
        distance[Point(0, 0)] = 0
        points.add(Point(0, 0))

        while (points.isNotEmpty()) {
            val point = points.poll()

            point.neighbors(false).filter { it in cave }.forEach {
                val dist = distance[point]!! + cave[it.y][it.x]
                if (dist < distance[it]!!) {
                    distance[it] = dist
                    points.add(it)
                }
            }
        }

        return distance[Point(cave.size-1, cave.size-1)]!!
    }

    private fun risk(point: Point): Int {
        val x = point.x / cave.size
        val y = point.y / cave.size
        val riskLevel = cave[point.y % cave.size][point.x % cave.size] + x + y

        return if (riskLevel > 9) riskLevel - 9 else riskLevel
    }

    fun part2(): Int {
        operator fun List<List<Int>>.contains(point: Point): Boolean {
            return point.x >= 0 && point.x < cave.size*5 && point.y >= 0 && point.y < cave.size*5
        }

        val distance = mutableMapOf<Point, Int>()
        val points = PriorityQueue { p1: Point, p2: Point -> distance[p1]!! - distance[p2]!! }

        for (i in 0 until cave.size*5) {
            for (j in 0 until cave.size*5) {
                distance[Point(j, i)] = Int.MAX_VALUE
            }
        }
        distance[Point(0, 0)] = 0
        points.add(Point(0, 0))

        while (points.isNotEmpty()) {
            val point = points.poll()

            point.neighbors(false).filter { it in cave }.forEach {
                val dist = distance[point]!! + risk(it)
                if (dist < distance[it]!!) {
                    distance[it] = dist
                    points.add(it)
                }
            }
        }

        return distance[Point((cave.size*5)-1, (cave.size*5)-1)]!!
    }
}
