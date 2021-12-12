package dev.gregbahr.problems

class Day12(private val data: List<String>) {

    private fun parseInput(): Map<String, List<String>> {
        val graph = mutableMapOf<String, MutableList<String>>()

        data.forEach {
            val edge = it.split("-")
            graph.getOrPut(edge[0]) { mutableListOf() } += edge[1]
            graph.getOrPut(edge[1]) { mutableListOf() } += edge[0]
        }

        return graph
    }

    private fun String.isLowerCase(): Boolean {
        return this.toLowerCase() == this
    }

    fun part1(): Int {
        val graph = parseInput()
        val paths = mutableSetOf<List<String>>()

        fun findPaths(currentPath: MutableList<String>) {
            if (currentPath.last() == "end") {
                paths.add(currentPath)
                return
            }

            val neighbors = graph[currentPath.last()] ?: mutableListOf()
            for (neighbor in neighbors) {
                if (neighbor.isLowerCase() && neighbor in currentPath) {
                    continue
                }

                val updated = currentPath.toMutableList()
                updated.add(neighbor)
                findPaths(updated)
            }
        }

        findPaths(mutableListOf("start"))
        return paths.size
    }

    private fun hasDuplicate(path: List<String>): Boolean {
        return path.filter { it.isLowerCase() }.groupingBy { it }.eachCount().any { it.value > 1 }
    }

    fun part2(): Int {
        val graph = parseInput()
        val paths = mutableSetOf<List<String>>()

        fun findPaths(currentPath: MutableList<String>) {
            if (currentPath.last() == "end") {
                paths.add(currentPath)
                return
            }

            val neighbors = graph[currentPath.last()]?.filter { it != "start" } ?: mutableListOf()
            for (neighbor in neighbors) {
                if (neighbor.isLowerCase() && hasDuplicate(currentPath) && neighbor in currentPath) {
                    continue
                }

                val updated = currentPath.toMutableList()
                updated.add(neighbor)
                findPaths(updated)
            }
        }

        findPaths(mutableListOf("start"))
        return paths.size
    }
}
