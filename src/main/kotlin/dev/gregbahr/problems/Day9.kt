package dev.gregbahr.problems

class Day9(data: List<String>) {

    private val input = data.map { it.chunked(1).map(String::toInt) }

    private fun findNeighbors(x: Int, y: Int): Set<Coord> {
        val neighbors = hashSetOf<Coord>()

        if (y < input.size-1) {
            neighbors += Coord(y+1, x)
        }

        if (y > 0) {
            neighbors += Coord(y-1, x)
        }

        if (x > 0) {
            neighbors += Coord(y, x-1)
        }

        if (x < input[y].size-1) {
            neighbors += Coord(y, x+1)
        }

        return neighbors
    }

    private fun isLowPoint(x: Int, y: Int): Boolean {
        return findNeighbors(x, y).fold(true) { acc, coord -> acc && input[y][x] < input[coord.first][coord.second]}
    }

    fun part1(): Int {
        val points = mutableListOf<Int>()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (isLowPoint(j, i)) {
                    points += input[i][j]
                }
            }
        }

        return points.sum() + points.size
    }

    fun part2(): Int {
        val points = mutableListOf<Coord>()
        for (i in input.indices) {
            for (j in input[i].indices) {
                if (isLowPoint(j, i)) {
                    points += Coord(i, j)
                }
            }
        }

        val basins = mutableListOf<Int>()
        for (point in points) {
            val seen = hashSetOf<Coord>()
            val queue = ArrayDeque<Coord>().apply { add(point) }

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                val neighbors = findNeighbors(current.second, current.first)

                for (neighbor in neighbors) {
                    if (neighbor !in seen
                        && input[neighbor.first][neighbor.second] != 9
                        && input[neighbor.first][neighbor.second] > input[current.first][current.second]
                    ) {
                        queue.add(neighbor)
                    }
                }

                seen += current
            }

            basins += seen.size
        }


        return basins.sorted().takeLast(3).reduce { acc, i -> acc * i }
    }
}
