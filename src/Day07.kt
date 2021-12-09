import kotlin.math.absoluteValue

fun main() {
    fun part1(input: List<String>): Int {
        val crabs = input[0].split(",")
            .map { MotorizedCrab(it.toInt()) }

        val minDist = (0 until crabs.maxOf { it.distance })
            .minOf {dist -> crabs.map { it.toPosition(dist) }.sum() }
        return minDist
    }

    fun part2(input: List<String>): Int {
        val crabs = input[0].split(",")
            .map { MotorizedCrab(it.toInt()) }

        val minDist = (0 until crabs.maxOf { it.distance })
            .minOf {dist -> crabs.map { it.toPositionWithCrabsConsumption(dist) }.sum() }
        return minDist
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 37)
    val part2 = part2(testInput)
    println(part2)
    check(part2 == 168)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

class MotorizedCrab(val distance: Int) {
    fun toPosition(dist: Int) = (distance - dist).absoluteValue
    fun toPositionWithCrabsConsumption(dist: Int): Int {
        // 1 = 1
        // 2 = 1 + 2
        // 3 = 1 + 2 + 3
        val n = (distance - dist).absoluteValue
        return (n * (n+1))/2
    }
}
