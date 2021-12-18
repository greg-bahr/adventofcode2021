package dev.gregbahr.problems

import kotlin.math.ceil
import kotlin.math.floor

class Day18(private val data: List<String>) {

    class Node(private var parent: Node?) {
        private var value = 0
        var left: Node? = null
        var right: Node? = null

        constructor(parent: Node?, value: Int) : this(parent) {
            this.value = value
        }

        constructor(left: Node, right: Node) : this(null) {
            this.left = left
            this.right = right
        }

        private fun isPair(): Boolean {
            return value == 0 && left != null && right != null
        }

        operator fun plus(other: Node): Node {
            val node = Node(this.copy(), other.copy()).apply {
                left!!.parent = this
                right!!.parent = this
            }

            return node.reduce()
        }

        private fun copy(): Node {
            val new = Node(parent)
            new.left = if (left!!.isPair()) left!!.copy().apply { parent = new } else Node(new, left!!.value)
            new.right = if (right!!.isPair()) right!!.copy().apply { parent = new } else Node(new, right!!.value)

            return new
        }

        private fun reduce(): Node {
            do {
                val nestedPair = findNestedPair()
                val splitNumber = findNumberToSplit()
                if (nestedPair != null) {
                    nestedPair.explode()
                } else if (splitNumber != null) {
                    splitNumber.split()
                }
            } while (nestedPair != null || splitNumber != null)
            return this
        }

        private fun explode() {
            if (isPair()) {
                val left = findLeft()
                val right = findRight()

                if (left != null) {
                    left.value += this.left!!.value
                }
                if (right != null) {
                    right.value += this.right!!.value
                }
                this.left = null
                this.right = null
            }
        }

        private fun findRight(): Node? {
            var right: Node? = null
            val visited = mutableSetOf<Node>()
            visited.add(this)
            var next = parent
            while (next != null) {
                if (next.right !in visited) {
                    right = next.right
                    break
                } else {
                    visited.add(next)
                    next = next.parent
                }
            }

            while (right != null && right.isPair()) {
                right = right.left
            }

            return right
        }

        private fun findLeft(): Node? {
            var left: Node? = null
            val visited = mutableSetOf<Node>()
            visited.add(this)
            var next = parent
            while (next != null) {
                if (next.left !in visited) {
                    left = next.left
                    break
                } else {
                    visited.add(next)
                    next = next.parent
                }
            }

            while (left != null && left.isPair()) {
                left = left.right
            }

            return left
        }

        private fun split() {
            if (!isPair()) {
                left = Node(this, floor(value / 2.0).toInt())
                right = Node(this, ceil(value / 2.0).toInt())
                value = 0
            }
        }

        private fun findNumberToSplit(): Node? {
            val stack = ArrayDeque<Node>()
            stack.add(this)
            while (stack.isNotEmpty()) {
                val node = stack.removeLast()
                if (node.isPair()) {
                    stack.add(node.right!!)
                    stack.add(node.left!!)
                } else if (node.value >= 10) {
                    return node
                }
            }
            return null
        }

        private fun depth(): Int {
            var depth = 0
            var node = parent
            while (node != null) {
                depth++
                node = node.parent
            }
            return depth
        }

        private fun findNestedPair(): Node? {
            val stack = ArrayDeque<Node>()
            stack.add(this)
            while (stack.isNotEmpty()) {
                val node = stack.removeLast()
                if (node.isPair()) {
                    stack.add(node.right!!)
                    stack.add(node.left!!)
                } else if (node.parent!!.depth() >= 4) {
                    return node.parent
                }
            }
            return null
        }

        fun magnitude(): Int {
            val leftMagnitude = if (left!!.isPair()) left!!.magnitude() else left!!.value
            val rightMagnitude = if (right!!.isPair()) right!!.magnitude() else right!!.value

            return (leftMagnitude * 3) + (rightMagnitude * 2)
        }

        override fun toString(): String {
            return if (isPair()) "[$left, $right]" else "$value"
        }
    }

    private fun parseInput(): List<Node> {
        return data.map {
            val stack = ArrayDeque<Node>()
            for (c in it) {
                when (c) {
                    '[' -> {
                        val parent = stack.lastOrNull()
                        val node = Node(parent)
                        if (parent != null && parent.left == null) {
                            parent.left = node
                        } else if (parent != null) {
                            parent.right = node
                        }
                        stack.add(node)
                    }
                    ']' -> {
                        if (stack.size > 1) {
                            stack.removeLast()
                        }
                    }
                    else -> {
                        if (c.isDigit()) {
                            val node = stack.last()
                            if (node.left == null) {
                                node.left = Node(node, c.digitToInt())
                            } else {
                                node.right = Node(node, c.digitToInt())
                            }
                        }
                    }
                }
            }

            stack.removeLast()
        }
    }

    fun part1(): Int {
        return parseInput().reduce { acc, node -> acc + node }.magnitude()
    }

    fun part2(): Int {
        val sums = mutableSetOf<Int>()
        val nums = parseInput()

        for (i in nums.indices) {
            for (j in nums.indices) {
                if (i != j) {
                    sums += (nums[i] + nums[j]).magnitude()
                }
            }
        }

        return sums.maxOrNull()!!
    }
}
