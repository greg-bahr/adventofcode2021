package dev.gregbahr.problems

class Day8(private val data: List<String>) {

    fun part1(): Int {
        val outputs = data.flatMap { it.split(" | ")[1].split(" ") }

        return outputs.count { it.length in listOf(3, 4, 2, 7) }
    }

    private infix fun Set<Char>.inCommon(other: Set<Char>?): Int {
        return other?.intersect(this)?.size ?: 0
    }

    fun part2(): Int {
        val input = data.map {
            val strings = it.split(" | ")

            Pair(strings[0].split(" "), strings[1].split(" "))
        }

        return input.map { pair ->
            val patterns = pair.first.map { it.toSet() }
            val output = pair.second.map { it.toSet() }

            val decoded = mutableMapOf<Int, Set<Char>>()
            decoded[1] = patterns.find { it.size == 2 }!!
            decoded[4] = patterns.find { it.size == 4 }!!
            decoded[7] = patterns.find { it.size == 3 }!!
            decoded[8] = patterns.find { it.size == 7 }!!

            decoded[0] = patterns.find {
                it.size == 6 &&
                it inCommon decoded[1] == 2 &&
                it inCommon decoded[4] == 3 &&
                it inCommon decoded[7] == 3
            }!!

            decoded[6] = patterns.find {
                it.size == 6 &&
                it inCommon decoded[1] == 1 &&
                it inCommon decoded[4] == 3 &&
                it inCommon decoded[7] == 2
            }!!

            decoded[9] = patterns.find {
                it.size == 6 &&
                it inCommon decoded[1] == 2 &&
                it inCommon decoded[4] == 4 &&
                it inCommon decoded[7] == 3
            }!!

            decoded[2] = patterns.find {
                it.size == 5 &&
                it inCommon decoded[1] == 1 &&
                it inCommon decoded[4] == 2 &&
                it inCommon decoded[7] == 2
            }!!

            decoded[3] = patterns.find {
                it.size == 5 &&
                it inCommon decoded[1] == 2 &&
                it inCommon decoded[4] == 3 &&
                it inCommon decoded[7] == 3
            }!!

            decoded[5] = patterns.find {
                it.size == 5 &&
                it inCommon decoded[1] == 1 &&
                it inCommon decoded[4] == 3 &&
                it inCommon decoded[7] == 2
            }!!

            val decodedReversed = decoded.entries.associate { (key, value) -> value to key }
            output.map { decodedReversed[it] }.joinToString("").toInt()
        }.sum()
    }
}
