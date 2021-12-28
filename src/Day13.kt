fun main() {
    fun printMap(transparentMap: MutableMap<Int, MutableSet<Int>>) {
        // limits
        val xMax = transparentMap.keys.sorted().last()
        val yMax = transparentMap.flatMap { it.value }.sorted().last()

        println(buildString {
            appendLine("---")
            (0..xMax).forEach { x ->
                (0..yMax).forEach { y ->
                    if (transparentMap[x]?.contains(y) == true)
                        append("#")
                    else
                        append(" ")
                }
                appendLine()
            }
            append("---")
        })
    }

    fun part1(input: List<String>): Int {

        val transparentMap = mutableMapOf<Int, MutableSet<Int>>()

        input.filter { it.isNotEmpty() }.filter { !it.startsWith("fold") }.forEach { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            val values = transparentMap.computeIfAbsent(y) { mutableSetOf() }
            values.add(x)
            transparentMap[y] = values
        }

        val foldInstructions = mutableListOf<Pair<Dir, Int>>()
        input.filter { it.isNotEmpty() }.filter { it.startsWith("fold") }.forEach { line ->
            val (dir, coord) = line.substring(11).split("=")
            foldInstructions.add(Dir.valueOf(dir) to coord.toInt())
        }

        val firstInstruction = foldInstructions[0]

        when (firstInstruction.first) {
            Dir.x -> transparentMap.toMap().forEach { entry ->
                transparentMap[entry.key] = entry.value.mapNotNull {
                    if (it == firstInstruction.second) {
                        null
                    } else if (it < firstInstruction.second) {
                        it
                    } else {
                        it - (it - firstInstruction.second) * 2
                    }
                }.toMutableSet()
            }
            Dir.y -> transparentMap.toMap().forEach { entry ->
                if (entry.key == firstInstruction.second) {
                    transparentMap.remove(entry.key)
                } else if (entry.key > firstInstruction.second) {
                    val newX = (entry.key - (entry.key - firstInstruction.second) * 2)
                    val yValues = transparentMap[newX] ?: mutableSetOf()
                    yValues.addAll(entry.value)
                    transparentMap[newX] = yValues
                    transparentMap.remove(entry.key)
                }
            }
        }

        return transparentMap.flatMap { it.value }.count()
    }

    fun part2(input: List<String>): Int {
        val transparentMap = mutableMapOf<Int, MutableSet<Int>>()

        input.filter { it.isNotEmpty() }.filter { !it.startsWith("fold") }.forEach { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            val values = transparentMap.computeIfAbsent(y) { mutableSetOf() }
            values.add(x)
            transparentMap[y] = values
        }

        val foldInstructions = mutableListOf<Pair<Dir, Int>>()
        input.filter { it.isNotEmpty() }.filter { it.startsWith("fold") }.forEach { line ->
            val (dir, coord) = line.substring(11).split("=")
            foldInstructions.add(Dir.valueOf(dir) to coord.toInt())
        }

        foldInstructions.forEach { firstInstruction ->

            when (firstInstruction.first) {
                Dir.x -> transparentMap.toMap().forEach { entry ->
                    transparentMap[entry.key] = entry.value.mapNotNull {
                        if (it == firstInstruction.second) {
                            null
                        } else if (it < firstInstruction.second) {
                            it
                        } else {
                            it - (it - firstInstruction.second) * 2
                        }
                    }.toMutableSet()
                }
                Dir.y -> transparentMap.toMap().forEach { entry ->
                    if (entry.key == firstInstruction.second) {
                        transparentMap.remove(entry.key)
                    } else if (entry.key > firstInstruction.second) {
                        val newX = (entry.key - (entry.key - firstInstruction.second) * 2)
                        val yValues = transparentMap[newX] ?: mutableSetOf()
                        yValues.addAll(entry.value)
                        transparentMap[newX] = yValues
                        transparentMap.remove(entry.key)
                    }
                }
            }
        }
        printMap(transparentMap)

        return transparentMap.flatMap { it.value }.count()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 17)
    val part2 = part2(testInput)
    println(part2)
//    check(part2 == 1)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

enum class Dir {
    x, y
}
