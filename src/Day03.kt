import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {

        val matrix = Matrix(input.size, input[0].length, input.flatMap { it.toCharArray().toList() })

        val gamma = (0 until matrix.columns).map { col ->
            matrix.valuesForColumn(col)
                .groupBy { it }
                .maxByOrNull { it.value.size }!!.key.digitToInt()
        }

        val epsilon = gamma.map { it xor 1 }

        return gamma.binaryToInt() * epsilon.binaryToInt()
    }

    fun getMax(
        matrix: Matrix<Char>,
    ): Int {
        var decreasingMatrix = matrix
        var index = 0
        do {
            val digit = decreasingMatrix.valuesForColumn(index)
                .groupBy { it }
                .map { Pair(it.key, it.value.size) }
                .sortedBy { it.first }
                .reduce { p1, p2 ->
                    if (p1.second > p2.second) {
                        p1
                    } else {
                        p2
                    }
                }.first

            decreasingMatrix = decreasingMatrix.filterLinesBy { it[index] == digit }
            index++
        } while (decreasingMatrix.rows > 1)
        val binaryToInt = decreasingMatrix.valuesForLine(0).map { it.digitToInt() }.binaryToInt()
        return binaryToInt
    }

    fun getMin(
        matrix: Matrix<Char>,
    ): Int {
        var decreasingMatrix = matrix
        var index = 0
        do {
            val digit = decreasingMatrix.valuesForColumn(index)
                .groupBy { it }
                .map { Pair(it.key, it.value.size) }
                .sortedBy { it.first }.reversed()
                .reduce { p1, p2 ->
                    if (p1.second < p2.second) {
                        p1
                    } else {
                        p2
                    }
                }.first

            decreasingMatrix = decreasingMatrix.filterLinesBy { it[index] == digit }
            index++
        } while (decreasingMatrix.rows > 1)
        val binaryToInt = decreasingMatrix.valuesForLine(0).map { it.digitToInt() }.binaryToInt()
        return binaryToInt
    }

    fun part2(input: List<String>): Int {

        val matrix = Matrix(input.size, input[0].length, input.flatMap { it.toCharArray().toList() })

        val o2 = getMax(matrix)
        val co2 = getMin(matrix)
        return o2 * co2
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198) { "Part1 : got ${part1(testInput)}" }
    check(part2(testInput) == 230) { "Part2: got ${part2(testInput)}" }

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun List<Int>.binaryToInt() = this.reversed()
    .mapIndexed { index, int ->
        (int * 2.0.pow(index)).toInt()
    }.sum()
