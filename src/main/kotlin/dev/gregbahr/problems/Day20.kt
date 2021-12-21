package dev.gregbahr.problems

class Day20(private val data: List<String>) {

    private val algorithm = data[0].replace(".", "0").replace("#", "1").toCharArray().map { it.digitToInt() }
    private val lightInputs = data.slice(2 until data.size).flatMapIndexed { y: Int, line: String -> line.mapIndexed { x, c -> if (c == '#') Point(x, y) else null }.filterNotNull() }.toSet()

    private fun enhance(litPixels: Set<Point>, times: Int = 2): Set<Point> {
        var enhanced = mutableSetOf<Point>()
        enhanced.addAll(litPixels)
        val x1 = litPixels.minOf { it.x }
        val x2 = litPixels.maxOf { it.x }
        val y1 = litPixels.minOf { it.y }
        val y2 = litPixels.maxOf { it.y }

        for (time in 1..times) {
            val lit = enhanced
            enhanced = mutableSetOf()
            fun isInInfiniteLit(pt: Point): Boolean {
                return time % 2 == 0 && (pt.x < x1 - time + 1 || pt.x > x2 + time - 1 || pt.y < y1 - time + 1 || pt.y > y2 + time - 1)
            }

            for (i in (x1-time)..(x2+time)) {
                for (j in (y1-time)..(y2+time)) {
                    val idx = Point(i, j).squarePoints().map { if (it in lit || isInInfiniteLit(it)) 1 else 0 }.reduce { acc, num -> (acc shl 1) or num }
                    if (algorithm[idx] == 1) {
                        enhanced.add(Point(i, j))
                    }
                }
            }
        }

        return enhanced
    }

    fun part1(): Int {
        return enhance(lightInputs, 2).size
    }

    fun part2(): Int {
        return enhance(lightInputs, 50).size
    }
}
