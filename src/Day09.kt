fun main() {
    fun part1(input: List<String>): Int {
        val matrix =
            Matrix(input.size, input[0].length, input.flatMap { line -> line.toCharArray().map { it.digitToInt() } })

        val lowerPoints = (0 until matrix.rows).flatMap { row ->
            matrix.pointsForLine(row).filter { point ->
                val surroundings = matrix.surroundings(point.line, point.column)
                point.value < surroundings
            }.map { it.value }
        }

        return lowerPoints.map {
            it + 1
        }.reduce { acc, i ->
            acc + i
        }
    }

    fun part2(input: List<String>): Int {
        val matrix =
            Matrix(input.size, input[0].length, input.flatMap { line -> line.toCharArray().map { it.digitToInt() } })

        var lowerBasins = (0 until matrix.rows).flatMap { row ->
            matrix.pointsForLine(row).filter { point ->
                val surroundings = matrix.surroundings(point.line, point.column)
                point.value < surroundings
            }.map { setOf(it) }
        }

        // While basins are size changing, continue to search wider
        do {
            val basinsLengh = lowerBasins.flatten().size

            lowerBasins = lowerBasins.map { basin ->
                // chaque point du bassin peut cherche ses voisins avec 1 d'écart
                basin.flatMap {
                    matrix.surroundingPoints(it.line, it.column)
                        .filter { point -> point.value < 9 } + it
                }.toSet()
            }
        } while (basinsLengh < lowerBasins.flatten().size)


        return lowerBasins.map { it.size }.sorted().reversed().filterIndexed { index, _ -> index < 3 }.reduce { acc, i -> acc * i }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 15)
    val part2 = part2(testInput)
    println(part2)
    check(part2 == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

private operator fun Int.compareTo(surroundings: Set<Int>): Int {
    return if (surroundings.all { it > this }) {
        -1
    } else if (surroundings.all { it < this }) {
        1
    } else 0
}

private fun Matrix<Int>.surroundings(line: Int, column: Int): Set<Int> {

    return (listOf(line to column - 1, line to column + 1) +
            listOf(line - 1 to column, line + 1 to column)).mapNotNull { (line, column) ->
        this.value(line, column)
    }.toSet()
}

private fun Matrix<Int>.surroundingPoints(line: Int, column: Int): Set<Point<Int>> {

    return (listOf(line to column - 1, line to column + 1) +
            listOf(line - 1 to column, line + 1 to column)).mapNotNull { (line, column) ->
        this.point(line, column)
    }.toSet()
}
