package dev.gregbahr.problems

class Day14(private val data: List<String>) {

    private val initialPairs = data.take(1)[0].windowed(2)
    private val rules = data.takeLastWhile { it.isNotEmpty() }.associateBy({it.split(" -> ")[0]}, {it.split(" -> ")[1]})
    private val elements = rules.flatMap { it.key.toList() }.toMutableSet().map(Char::toString) + rules.values

    fun part1(): Int {
        val pairs = initialPairs.associateWith { 1 }.toMutableMap()
        val elementCount = elements.associateWith { if (initialPairs.any { p -> it in p }) 1 else 0 }.toMutableMap()

        for (i in 1..10) {
            for ((pair, count) in pairs.toMutableMap()) {
                if (pair in rules.keys && count > 0) {
                    pairs[pair] = pairs[pair]!! - count
                    pairs["${pair.first()}${rules[pair]}"] = (pairs["${pair.first()}${rules[pair]}"] ?: 0) + count
                    pairs["${rules[pair]}${pair.last()}"] = (pairs["${rules[pair]}${pair.last()}"] ?: 0) + count
                    elementCount["${rules[pair]}"] = (elementCount["${rules[pair]}"] ?: 0) + count
                }
            }
        }

        return elementCount.maxOf { it.value } - elementCount.minOf { it.value }
    }

    fun part2(): Long {
        val pairs = initialPairs.associateWith { 1L }.toMutableMap()
        val elementCount = elements.associateWith { if (initialPairs.any { p -> it in p }) 1L else 0L }.toMutableMap()

        for (i in 1..40) {
            for ((pair, count) in pairs.toMutableMap()) {
                if (pair in rules.keys && count > 0) {
                    pairs[pair] = pairs[pair]!! - count
                    pairs["${pair.first()}${rules[pair]}"] = (pairs["${pair.first()}${rules[pair]}"] ?: 0) + count
                    pairs["${rules[pair]}${pair.last()}"] = (pairs["${rules[pair]}${pair.last()}"] ?: 0) + count
                    elementCount["${rules[pair]}"] = (elementCount["${rules[pair]}"] ?: 0) + count
                }
            }
        }

        return elementCount.maxOf { it.value } - elementCount.minOf { it.value }
    }
}
