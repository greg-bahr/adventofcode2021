package dev.gregbahr.problems

import kotlin.math.max
import kotlin.math.min

class Day22(private val data: List<String>) {

    private val steps = data.map { line ->
        line.split(" ").let { split ->
            val ranges = split[1].split(",").map {
                val splitRanges = it.split("..")
                (splitRanges[0].slice(2 until splitRanges[0].length).toInt())..splitRanges[1].toInt()
            }
            Cuboid.of(split[0] == "on", ranges)
        }
    }

    data class Cuboid(val bottom: Point3D, val top: Point3D, val on: Boolean) {
        val area = (if (on) 1 else -1) * (top.x - bottom.x + 1L) * (top.y - bottom.y + 1L) * (top.z - bottom.z + 1L)

        companion object {
            fun of(on: Boolean, ranges: List<IntRange>): Cuboid {
                return Cuboid(Point3D(ranges[0].first, ranges[1].first, ranges[2].first), Point3D(ranges[0].last, ranges[1].last, ranges[2].last), on)
            }
        }

        private infix fun intersects(other: Cuboid): Boolean {
            return bottom.x <= other.top.x && top.x >= other.bottom.x && top.y >= other.bottom.y && bottom.y <= other.top.y && top.z >= other.bottom.z && bottom.z <= other.top.z
        }

        fun intersectingCuboid(other: Cuboid): Cuboid? {
            if (!(this intersects other)) return null
            return Cuboid(Point3D(max(bottom.x, other.bottom.x), max(bottom.y, other.bottom.y), max(bottom.z, other.bottom.z)), Point3D(min(top.x, other.top.x), min(top.y, other.top.y), min(top.z, other.top.z)), !on)
        }
    }

    private fun addCuboid(cuboid: Cuboid, cuboids: MutableList<Cuboid>) {
        val intersections = cuboids.mapNotNull { it.intersectingCuboid(cuboid) }

        if (cuboid.on) {
            cuboids += cuboid
        }
        cuboids.addAll(intersections)
    }

    private fun List<Cuboid>.sumArea(): Long {
        return this.fold(0L) { acc, cuboid -> acc + cuboid.area }
    }

    fun part1(): Long {
        val results = mutableListOf<Cuboid>()
        return steps.take(20).forEach { addCuboid(it, results) }.let { results.sumArea() }
    }

    fun part2(): Long {
        val results = mutableListOf<Cuboid>()
        return steps.forEach { addCuboid(it, results) }.let { results.sumArea() }
    }
}
