package dev.gregbahr.problems

import kotlin.math.abs

class Day19(private val data: List<String>) {

    private fun parseInput(): List<List<Point3D>> {
        val scanners = mutableListOf<List<Point3D>>()

        var currentScanner: MutableList<Point3D>? = null
        for (line in data) {
            if (line.isEmpty()) {
                continue
            } else if (line.contains("scanner")) {
                currentScanner = mutableListOf()
                scanners.add(currentScanner)
            } else {
                currentScanner?.add(line.split(",").map { it.toInt() }.let { Point3D(it[0], it[1], it[2]) })
            }
        }
        return scanners
    }

    data class Point3D(val x: Int, val y: Int, val z: Int) {
        companion object {
            fun roll(point: Point3D): Point3D {
                return Point3D(point.x, point.z, -point.y)
            }

            fun turn(point: Point3D): Point3D {
                return Point3D(-point.y, point.x, point.z)
            }
        }

        fun orientations(): List<Point3D> {
            val orientations = mutableListOf<Point3D>()

            // My hero... https://stackoverflow.com/questions/16452383/how-to-get-all-24-rotations-of-a-3-dimensional-array
            var rotated = this
            for (i in 1..2) {
                for (j in 1..3) {
                    rotated = roll(rotated)
                    orientations.add(rotated)
                    for (k in 1..3) {
                        rotated = turn(rotated)
                        orientations.add(rotated)
                    }
                }
                rotated = roll(turn(roll(rotated)))
            }
            return orientations
        }

        operator fun plus(other: Point3D): Point3D {
            return Point3D(x + other.x, y + other.y, z + other.z)
        }

        operator fun minus(other: Point3D): Point3D {
            return Point3D(x - other.x, y - other.y, z - other.z)
        }

        infix fun distanceBetween(other: Point3D): Int {
            return abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
        }

        override fun toString(): String {
            return "$x,$y,$z"
        }
    }

    private fun List<Point3D>.orientations(): List<List<Point3D>> {
        val separatedOrientations = (1..this[0].orientations().size).map { mutableListOf<Point3D>() }

        for (point in this) {
            val pointList = point.orientations()
            for (i in pointList.indices) {
                separatedOrientations[i].add(pointList[i])
            }
        }

        return separatedOrientations
    }

    fun solve() {
        val scanners = parseInput()
        val foundScanners = mutableSetOf(scanners[0])
        val foundBeacons = mutableSetOf<Point3D>().apply { addAll(scanners[0]) }
        val scannerLocations = mutableSetOf(Point3D(0, 0, 0))

        while (foundScanners.size != scanners.size) {
            for (scanner in scanners) {
                if (scanner in foundScanners) {
                    continue
                }

                for (orientation in scanner.orientations()) {
                    for (absoluteBeacon in foundBeacons) {
                        for (testBeacon in orientation) {
                            val delta = absoluteBeacon - testBeacon
                            val candidateBeacons = orientation.map { it + delta }.toSet()
                            if (foundBeacons.intersect(candidateBeacons).size >= 12) {
                                foundScanners += scanner
                                scannerLocations += delta
                                foundBeacons.addAll(candidateBeacons)
                                break
                            }
                        }
                        if (scanner in foundScanners) {
                            break
                        }
                    }
                    if (scanner in foundScanners) {
                        break
                    }
                }
            }
        }

        val maxDistance = sequence { scannerLocations.forEach { a -> scannerLocations.forEach { b -> if (a != b) yield(a to b) } } }.maxOf { it.first distanceBetween it.second }

        println("part 1: ${foundBeacons.size}")
        println("part 2: $maxDistance")
    }
}
