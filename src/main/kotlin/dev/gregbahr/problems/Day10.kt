package dev.gregbahr.problems

class Day10(private val data: List<String>) {

    val charMap = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

    fun part1(): Int {
        val charMap = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
        val scoreMap = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
        var score = 0

        for (line in data) {
            val stack = ArrayDeque<Char>()

            for (char in line) {
                if (char in charMap.keys) {
                    stack += char
                } else if (char != charMap[stack.removeLast()]) {
                    score += scoreMap[char]!!
                    break
                }
            }
        }

        return score
    }

    fun part2(): Long {
        val score = mutableListOf<Long>()
        val scoreMap = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)

        for (line in data) {
            val stack = ArrayDeque<Char>()
            var corrupt = false
            for (char in line) {
                if (char in charMap.keys) {
                    stack += char
                } else if (char == charMap[stack.last()]) {
                    stack.removeLast()
                } else {
                    corrupt = true
                    break
                }
            }

            if (!corrupt) {
                score += stack.reversed().fold(0) { acc, c -> (acc * 5) + scoreMap[charMap[c]]!! }
            }
        }

        return score.sorted()[score.size/2]
    }
}
