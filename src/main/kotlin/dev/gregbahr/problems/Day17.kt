package dev.gregbahr.problems

import kotlin.math.abs

class Day17 {

    private val targetArea = (MIN_X..MAX_X).flatMap { x -> (MIN_Y..MAX_Y).map { y -> Point(x, y) } }

    companion object {
        const val MIN_X = 102
        const val MAX_X = 157
        const val MIN_Y = -146
        const val MAX_Y = -90
    }

    private fun hitsTarget(startingVelocity: Point): Boolean {
        var location = Point(0, 0)
        var velocity = startingVelocity

        while(true) {
            location += velocity
            velocity = Point(velocity.x - (if (velocity.x == 0) 0 else 1), velocity.y - 1)

            if (location in targetArea) {
                return true
            } else if (location.x > MAX_X || location.y < MIN_Y) {
                return false
            }
        }
    }

    fun part1(): Int {
        val validPoints = mutableListOf<Point>()
        for (y in 0..abs(MIN_Y)) {
            for (x in 0..MIN_X) {
                Point(x, y).apply {
                    if (hitsTarget(this)) {
                        validPoints += this
                    }
                }
            }
        }

        return validPoints.maxOf { (it.y * (it.y + 1)) / 2 }
    }

    fun part2(): Int {
        var count = 0
        for (y in MIN_Y..abs(MIN_Y)) {
            for (x in 0..MAX_X) {
                if (hitsTarget(Point(x, y))) {
                    count++
                }
            }
        }

        return count
    }
}
