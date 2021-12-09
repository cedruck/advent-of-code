fun main() {
    fun part1(input: List<String>): Int {
        var lanternFishs = input[0].split(",").map {
            LanternFish(it.toInt())
        }.sortedBy { it.daysUntilReproduction }

        (0 until 80).forEach { _ ->
            lanternFishs = lanternFishs.flatMap { it.oneMoreDay() }.sortedBy { it.daysUntilReproduction }
        }
        return lanternFishs.size
    }

    fun part2(input: List<String>): Long {
        var lanternFishs = input[0].split(",").map {
            LanternFish(it.toInt())
        }.groupBy { it.daysUntilReproduction }
            .map { SameAgeLanternFish(it.key, it.value.size.toLong()) }

        (0 until 256).forEach { _ ->
            lanternFishs = lanternFishs.flatMap { it.oneMoreDay() }
                .groupBy{
                    it.daysUntilReproduction
                }.map { SameAgeLanternFish(it.key, it.value.map { it.qte }.reduce { p1, p2 -> p1 + p2 }) }
                .sortedBy { it.daysUntilReproduction }
        }
        return lanternFishs.map { it.qte }.reduce { q1,q2 ->
            q1 + q2
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 5934)
    val part2 = part2(testInput)
    println(part2)
    check(part2 == 26984457539)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

data class LanternFish(val daysUntilReproduction: Int) {
    fun oneMoreDay(): List<LanternFish> {
        if (daysUntilReproduction == 0) {
            return listOf(LanternFish(6), LanternFish(8))
        }
        return listOf(LanternFish(daysUntilReproduction - 1))
    }
}

data class SameAgeLanternFish(val daysUntilReproduction: Int, val qte: Long) {
    fun oneMoreDay(): List<SameAgeLanternFish> {
        if (daysUntilReproduction == 0) {
            return listOf(SameAgeLanternFish(6, qte), SameAgeLanternFish(8, qte))
        }
        return listOf(SameAgeLanternFish(daysUntilReproduction - 1, qte))
    }
}
