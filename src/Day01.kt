fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 1)
    val part2 = part2(testInput)
    println(part2)
    check(part2 == 1)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
