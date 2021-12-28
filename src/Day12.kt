fun main() {
    fun part1(input: List<String>): Int {
        val caveNodes = mutableMapOf<String, CaveNode>()

        input.forEach {
            val (cave1, cave2) = it.split("-")
            val caveNode1 = caveNodes[cave1] ?: CaveNode(name = cave1, isBig = cave1.lowercase() != cave1)
            caveNodes[caveNode1.name] = caveNode1.copy(linkedTo = caveNode1.linkedTo + cave2)
            val caveNode2 = caveNodes[cave2] ?: CaveNode(name = cave2, isBig = cave2.lowercase() != cave2)
            caveNodes[caveNode2.name] = caveNode2.copy(linkedTo = caveNode2.linkedTo + cave1)
        }

        val startCave = caveNodes["start"]!!

        var paths = startCave.linkedTo.map {
            val currentCave = caveNodes[it]!!
            Path(currentCave, setOf(startCave, currentCave))
        }

        do {
            paths = paths.flatMap {
                it.nextPaths(caveNodes)
            }
        } while (paths.any { !it.isEnded() })

        return paths.filter { it.currentCave.isEnd() }.size
    }

    fun part2(input: List<String>): Int {

        val caveNodes = mutableMapOf<String, CaveNode>()

        input.forEach {
            val (cave1, cave2) = it.split("-")
            val caveNode1 = caveNodes[cave1] ?: CaveNode(name = cave1, isBig = cave1.lowercase() != cave1)
            caveNodes[caveNode1.name] = caveNode1.copy(linkedTo = caveNode1.linkedTo + cave2)
            val caveNode2 = caveNodes[cave2] ?: CaveNode(name = cave2, isBig = cave2.lowercase() != cave2)
            caveNodes[caveNode2.name] = caveNode2.copy(linkedTo = caveNode2.linkedTo + cave1)
        }

        val startCave = caveNodes["start"]!!

        var paths = startCave.linkedTo.map {
            val currentCave = caveNodes[it]!!
            MiniCaveTwicePath(currentCave, listOf(startCave, currentCave))
        }

        do {
            paths = paths.flatMap {
                it.nextPaths(caveNodes)
            }
        } while (paths.any { !it.isEnded() })

        return paths.filter { it.currentCave.isEnd() }.size
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("Day12_test1")
    var part1 = part1(testInput)
    println(part1)
    check(part1 == 10)
    var part2 = part2(testInput)
    println(part2)
    check(part2 == 36)
    testInput = readInput("Day12_test2")
    part1 = part1(testInput)
    println(part1)
    check(part1 == 19)
    part2 = part2(testInput)
    println(part2)
    check(part2 == 103)
    testInput = readInput("Day12_test3")
    part1 = part1(testInput)
    println(part1)
    check(part1 == 226)
    part2 = part2(testInput)
    println(part2)
    check(part2 == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

data class CaveNode(val name: String, val linkedTo: Set<String> = emptySet(), val isBig: Boolean = false) {
    fun isEnd(): Boolean {
        return name == "end"
    }
}

data class Path(val currentCave: CaveNode, val breadcrumb: Set<CaveNode>) {
    fun next(caveNode: CaveNode): Path {
        return Path(caveNode, breadcrumb + caveNode)
    }

    override fun toString(): String {
        return buildString {
            appendLine()
            appendLine("$currentCave")
            appendLine(breadcrumb.joinToString(",") { it.name })
        }
    }

    fun nextPaths(caveNodes: Map<String, CaveNode>): Set<Path> {
        if (!currentCave.isEnd()) {
            val paths = nextCaves().map {
                next(caveNodes[it]!!)
            }
            if (paths.isNotEmpty()) {
                return paths.toSet()
            }
        }
        return setOf(this)
    }

    fun isEnded(): Boolean {
        if (currentCave.isEnd()) {
            return true
        }
        return nextCaves().isEmpty()
    }

    private fun nextCaves() = (currentCave.linkedTo - breadcrumb.filter { !it.isBig }.map { it.name }.toSet())
}

data class MiniCaveTwicePath(val currentCave: CaveNode, val breadcrumb: List<CaveNode>) {
    fun next(caveNode: CaveNode): MiniCaveTwicePath {
        return MiniCaveTwicePath(caveNode, breadcrumb + caveNode)
    }

    override fun toString(): String {
        return buildString {
            appendLine()
            appendLine("$currentCave")
            appendLine(breadcrumb.joinToString(",") { it.name })
        }
    }

    fun nextPaths(caveNodes: Map<String, CaveNode>): Set<MiniCaveTwicePath> {
        if (!currentCave.isEnd()) {
            val paths = availableCaves().map {
                next(caveNodes[it]!!)
            }
            if (paths.isNotEmpty()) {
                return paths.toSet()
            }
        }
        return setOf(this)
    }

    fun isEnded(): Boolean {
        if (currentCave.isEnd()) {
            return true
        }
        return availableCaves().isEmpty()
    }

    private fun availableCaves(): Set<String> {
        val hasAlreadyVisitedTwiceACave =
            breadcrumb.asSequence().filter { !it.isBig }.groupBy { it.name }.any { it.value.size == 2 }
        return if (!hasAlreadyVisitedTwiceACave)
            currentCave.linkedTo - "start"
        else
            (currentCave.linkedTo - breadcrumb.filter { !it.isBig }.map { it.name }.toSet())
    }
}
