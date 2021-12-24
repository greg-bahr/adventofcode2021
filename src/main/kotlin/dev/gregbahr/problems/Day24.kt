package dev.gregbahr.problems

class Day24(private val data: List<String>) {

    private val instructions = data.map { it.split(" ") }

    data class Rule(val previousIndex: Int, val currentIndex: Int, val a: Int, val c: Int)

    private fun createRules(): List<Rule> {
        val blocks = instructions.chunked(18).map { listOf(it[5][2].toInt(), it[4][2].toInt(), it[15][2].toInt()) }
        val stack = ArrayDeque<Int>()
        val rules = mutableListOf<Rule>()
        for (i in blocks.indices) {
            val vars = blocks[i]
            if (vars[1] == 1) {
                stack.addLast(i)
            } else {
                val previousIndex = stack.removeLast()
                rules += Rule(previousIndex, i, vars[0], blocks[previousIndex][2])
            }
        }
        return rules
    }

//    fun execute(w: Int, vars: List<Int>) {
//        val x = (z % 26) + vars[0]
//        z /= vars[1]
//        if (x != w.toLong()) {
//            z = (z * 26) + w + vars[2]
//        }
//    }

    fun part1(): Long {
        val model = mutableListOf(9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9)
        val rules = createRules()

        for (rule in rules) {
            val num = rule.a + rule.c
            if (num < 0) {
                model[rule.currentIndex] = 9 + num
            } else {
                model[rule.previousIndex] = 9 - num
            }
        }

        return model.joinToString("").toLong()
    }

    fun part2(): Long {
        val model = mutableListOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1)
        val rules = createRules()

        for (rule in rules) {
            val num = rule.a + rule.c
            if (num < 0) {
                model[rule.previousIndex] = 1 - num
            } else {
                model[rule.currentIndex] = 1 + num
            }
        }

        return model.joinToString("").toLong()
    }
}
