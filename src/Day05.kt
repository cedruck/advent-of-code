import kotlin.math.max

fun main() {

    data class Vector(
        val x1: Int,
        val y1: Int,
        val x2: Int,
        val y2: Int,
    ) {
        fun isHorizontal() = y1 == y2
        fun isVertical() = x1 == x2
        fun isDiagonal() = horizontalRange.count() == verticalRange.count()

        fun straightValues(): List<Pair<Int, Int>> {
            if (isHorizontal()) {
                return horizontalRange.map { Pair(y1, it) }
            }
            if (isVertical()) {
                return verticalRange.map { Pair(it, x1) }
            }
            return emptyList()
        }

        fun allValues(): List<Pair<Int, Int>> {
            if (isDiagonal()) {
                val verticalRange = verticalRange.toList()
                val horizontalRange = horizontalRange.toList()
                return horizontalRange.mapIndexed { index, _ ->
                    Pair(verticalRange[index],
                        horizontalRange[index])
                }
            }
            return straightValues()
        }

        val horizontalRange = if (x1 < x2) { (x1..x2) } else { (x1 downTo x2) }
        val verticalRange = if (y1 < y2) { (y1..y2) } else { (y1 downTo y2) }
    }

    fun horizontalSize(vectors: List<Vector>) = vectors.maxOf { max(it.x1, it.x2) }
    fun verticalSize(vectors: List<Vector>) = vectors.maxOf { max(it.y1, it.y2) }

    fun countClouds(map: List<List<Int>>): Int {
        return map.map { line ->
            line.count { it >= 2 }
        }.sum()
    }

    fun extractVectors(input: List<String>) = input
        .map { point -> point.split(" -> ").flatMap { it.split(",") } }
        .map {
                Vector(it[0].toInt(), it[1].toInt(), it[2].toInt(), it[3].toInt())
            }

    fun part1(input: List<String>): Int {
        val vectors = extractVectors(input)

        val map = (0..verticalSize(vectors)).map {
            (0..horizontalSize(vectors)).map {
                0
            }.toMutableList()
        }.toMutableList()

        vectors.forEach {
            it.straightValues().forEach {
                    pair -> map[pair.first][pair.second] += 1
            }
        }

        return countClouds(map)
        }

    fun part2(input: List<String>): Int {
        val vectors = extractVectors(input)

        val map = (0..verticalSize(vectors)).map {
            (0..horizontalSize(vectors)).map {
                0
            }.toMutableList()
        }.toMutableList()

        vectors.forEach {
            it.allValues().forEach {
                    pair -> map[pair.first][pair.second] += 1
            }
        }

        //map.forEach { println(it) }
        return countClouds(map)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}
