package dev.gregbahr.problems

class Day4(private val data: List<String>) {

    private fun getBoards(): List<List<List<Int>>> {
        val boards = ArrayList<List<List<Int>>>()
        for (i in 2 until data.size step 6) {
            val board = ArrayList<List<Int>>()

            for (j in 0..4) {
                board.add(data[i + j].trim().split("\\s+".toRegex()).map { it.toInt() })
            }

            boards.add(board)
        }

        return boards
    }

    private fun drawsToWin(board: List<List<Int>>, draws: List<Int>): Int {
        val drawn = HashSet<Int>()

        for (draw in draws) {
            drawn.add(draw)

            if (isSolved(board, drawn)) {
                return drawn.size
            }
        }

        return drawn.size
    }

    private fun isSolved(board: List<List<Int>>, drawn: java.util.HashSet<Int>): Boolean {
        for (col in 0..4) {
            val column = board.map { it[col] }

            if (drawn.containsAll(column)) {
                return true
            }
        }

        for (row in 0..4) {
            if (drawn.containsAll(board[row])) {
                return true
            }
        }

        return false
    }

    fun part1(): Int {
        val draws = data[0].split(",").map { it.toInt() }
        val boards = getBoards()
        val solved = boards.map { drawsToWin(it, draws) }

        val minDraws = solved.minOrNull() ?: throw IllegalArgumentException()
        val minBoard = boards[solved.indexOf(minDraws)]

        val drawn = draws.slice(0 until minDraws).toHashSet()
        return minBoard.flatten().filter { !drawn.contains(it) }.sum() * draws[minDraws - 1]
    }

    fun part2(): Int {
        val draws = data[0].split(",").map { it.toInt() }
        val boards = getBoards()
        val solved = boards.map { drawsToWin(it, draws) }

        val maxDraws = solved.maxOrNull() ?: throw IllegalArgumentException()
        val maxBoard = boards[solved.indexOf(maxDraws)]

        val drawn = draws.slice(0 until maxDraws).toHashSet()
        return maxBoard.flatten().filter { !drawn.contains(it) }.sum() * draws[maxDraws - 1]
    }
}
