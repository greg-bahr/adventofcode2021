package dev.gregbahr.problems

import kotlin.math.abs

class Day13(private val data: List<String>) {

    fun part1(): Int {
        var points = data.chunked(800)[0].filter { it.isNotEmpty() }.map { it.split(",") }.map { Point(it[0].toInt(), it[1].toInt()) }.toSet()
        val instructions = data.chunked(800)[1].map { it.split("=") }.map { Pair(it[0].last(), it[1].toInt()) }

        val (axis, value) = instructions[0]
        points = points.fold(mutableSetOf()) { folded, point ->
            when (axis) {
                'x' -> folded.add(Point(value - abs(point.x - value), point.y))
                'y' -> folded.add(Point(point.x, value - abs(point.y - value)))
            }

            folded
        }

        return points.size
    }

    fun part2() {
        var points = data.chunked(800)[0].filter { it.isNotEmpty() }.map { it.split(",") }.map { Point(it[0].toInt(), it[1].toInt()) }.toSet()
        val instructions = data.chunked(800)[1].map { it.split("=") }.map { Pair(it[0].last(), it[1].toInt()) }

        for ((axis, value) in instructions) {
            points = points.fold(mutableSetOf()) { folded, point ->
                when (axis) {
                    'x' -> folded.add(Point(value - abs(point.x - value), point.y))
                    'y' -> folded.add(Point(point.x, value - abs(point.y - value)))
                }

                folded
            }
        }

        (0..points.maxOf { it.y }).forEach { y ->
            println(points.filter { it.y == y }.sortedBy { it.x }.fold("") { row, point ->
                row.padEnd(point.x, ' ') + "#"
            })
        }
    }
}
