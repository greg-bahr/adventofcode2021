package dev.gregbahr.problems

class Day25(private val data: List<String>) {

    enum class Direction {
        SOUTH, EAST
    }

    data class SeaCucumber(var location: Point, val type: Direction, private val length: Int) {
        fun move()  {
            location = nextLocation()
        }

        fun nextLocation(): Point = when (type) {
            Direction.SOUTH -> Point(location.x, (location.y + 1) % length)
            Direction.EAST -> Point((location.x + 1) % length, location.y)
        }
    }

    fun solve(): Int {
        val cucumbers = data.flatMapIndexed { y, line -> line.mapIndexedNotNull { x, c -> if (c == '.') null else SeaCucumber(Point(x, y), if (c == '>') Direction.EAST else Direction.SOUTH, if (c == '>') line.length else data.size) } }
        val east = cucumbers.filter { it.type == Direction.EAST }
        val south = cucumbers.filter { it.type == Direction.SOUTH }

        var step = 0
        while (true) {
            step++
            var moves = 0
            var locations = cucumbers.map { it.location }.toSet()
            for (cucumber in east) {
                if (cucumber.nextLocation() !in locations) {
                    cucumber.move()
                    moves++
                }
            }

            locations = cucumbers.map { it.location }.toSet()
            for (cucumber in south) {
                if (cucumber.nextLocation() !in locations) {
                    cucumber.move()
                    moves++
                }
            }

            if (moves == 0) {
                break
            }
        }

        return step
    }
}
