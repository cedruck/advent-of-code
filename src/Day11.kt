fun main() {
    fun part1(input: List<String>): Int {

        var count = 0
        var matrix =
            Matrix(input.size, input[0].length, input.flatMap { line -> line.toCharArray().map { Octo(it.digitToInt()) } })

        (0 until 100).forEach {
            // step
            matrix = matrix.findAndUpdate({ true }) { it.plus() }
            var flashingOctopuses: Set<Point<Octo>>
            while (matrix.allPoints { it.isAboutToFlash }.also { flashingOctopuses = it }.isNotEmpty()) {
                count += flashingOctopuses.size
                val neighbours = flashingOctopuses.findNeighbours(matrix)
                matrix = matrix.update(neighbours) { it.plus() }
                matrix = matrix.update(flashingOctopuses) { it.flashes() }
            }
            matrix = matrix.findAndUpdate({ true }) { it.unflash() }
        }
        println(matrix)

        return count
    }

    fun part2(input: List<String>): Int {
        var count = 0
        var matrix =
            Matrix(input.size, input[0].length, input.flatMap { line -> line.toCharArray().map { Octo(it.digitToInt()) } })

        do {
            count++
            // step
            matrix = matrix.findAndUpdate({ true }) { it.unflash() }
            matrix = matrix.findAndUpdate({ true }) { it.plus() }
            var flashingOctopuses: Set<Point<Octo>>
            while (matrix.allPoints { it.isAboutToFlash }.also { flashingOctopuses = it }.isNotEmpty()) {
                val neighbours = flashingOctopuses.findNeighbours(matrix)
                matrix = matrix.update(neighbours) { it.plus() }
                matrix = matrix.update(flashingOctopuses) { it.flashes() }
            }
        } while (matrix.all { !it.hasFlashed }.isNotEmpty())
        println(matrix)

        return count
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 1656)
    val part2 = part2(testInput)
    println(part2)
    check(part2 == 195)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

data class Octo(val age: Int, val hasFlashed: Boolean = false) {

    val isAboutToFlash = age >= 10 && !hasFlashed

    fun plus(): Octo = this.copy(age = age+1)

    fun flashes(): Octo = Octo(age = 0, true)

    override fun toString(): String {
        return "$age" + if (hasFlashed) "*" else " "
    }

    fun unflash() = this.copy(hasFlashed = false)

}

private fun Set<Point<Octo>>.findNeighbours(matrix: Matrix<Octo>): List<Point<Octo>> {
    return this.flatMap { point ->
        (-1..1).flatMap { line ->
            (-1..1).mapNotNull {
                val octoPoint = matrix.point(point.line + line, point.column + it)
                if (octoPoint?.value?.hasFlashed == true) {
                    null
                } else octoPoint
            }
        }
    }
}
