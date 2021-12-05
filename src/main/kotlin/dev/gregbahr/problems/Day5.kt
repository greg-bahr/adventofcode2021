package dev.gregbahr.problems

import java.lang.Integer.min
import kotlin.math.max

typealias Coord = Pair<Int, Int>

class Day5(private val data: List<String>) {

    fun part1(): Int {
        return parseInput().map(CoordinateRange::coordsPart1).flatten().groupingBy { it }.eachCount()
            .count { it.value > 1 }
    }

    fun part2(): Int {
        return parseInput().map(CoordinateRange::coordsPart2).flatten().groupingBy { it }.eachCount()
            .count { it.value > 1 }
    }

    private fun parseInput(): List<CoordinateRange> {
        return data.map { line ->
            val coords = line.split(" -> ")
            val start = coords[0].split(",").let { Coord(it[0].toInt(), it[1].toInt()) }
            val end = coords[1].split(",").let { Coord(it[0].toInt(), it[1].toInt()) }

            CoordinateRange(start, end)
        }
    }

    class CoordinateRange(private val start: Coord, private val end: Coord) {
        fun coordsPart1(): List<Coord> {
            return if (start.first == end.first) {
                (min(start.second, end.second)..max(start.second, end.second)).map { Coord(start.first, it) }
            } else if (start.second == end.second) {
                (min(start.first, end.first)..max(start.first, end.first)).map { Coord(it, start.second) }
            } else {
                listOf()
            }
        }

        fun coordsPart2(): List<Coord> {
            return if (start.first == end.first || start.second == end.second) {
                coordsPart1()
            } else {
                val coords = arrayListOf(start)
                var currentCoord = start
                val x = if (start.first < end.first) 1 else -1
                val y = if (start.second < end.second) 1 else -1

                while (currentCoord != end) {
                    currentCoord = Coord(currentCoord.first + x, currentCoord.second + y)
                    coords += currentCoord
                }

                return coords
            }
        }
    }
}
