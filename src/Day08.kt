fun main() {
    fun isUnique(number: String): Boolean {
        return listOf(2, 3, 4, 7).contains(number.length)
    }

    fun part1(input: List<String>): Int {
        val numbers = input.map {
            it.split(" | ")[1]
        }.flatMap {
            it.split(" ")
                .filter { number -> isUnique(number) }
        }.size

        return numbers
    }

    fun part2(input: List<String>): Int {
        return input.map { inputLine ->
            val (first, second) = inputLine.extractNumbers()
            val allNumbers = first + second
            val one = allNumbers.first { it.length == 2 }.toSet()
            val seven = allNumbers.first { it.length == 3 }.toSet()
            val four = allNumbers.first { it.length == 4 }.toSet()
            val eight = allNumbers.first { it.length == 7 }.toSet()

            val top = seven.subtract(one)
            var topRight = one
            var bottomRight = topRight
            var topLeft = four.subtract(one)
            var middle = topLeft
            var bottomLeft = eight.subtract(seven).subtract(four)
            var bottom = bottomLeft

            // give me a three (5 lines and two in common with $one)
            val three = allNumbers.filter { it.length == 5 }
                .first { it.toList().containsAll(one) }.toSet()
            middle = middle.intersect(three)
            topLeft = topLeft.subtract(middle)
            bottom = three.subtract(four).subtract(top)
            bottomLeft = bottomLeft.subtract(bottom)

            // give me a six (6 lines and just one in common with $one)
            val six = allNumbers.filter {
                it.length == 6
            }.map { it.toSet() }.first { !it.containsAll(one) }
            topRight = topRight.subtract(six)
            bottomRight = bottomRight.subtract(topRight)

            val two = top + topRight + middle + bottomLeft + bottom
            val five = top + topLeft + middle + bottomRight + bottom
            val nine = top + topLeft + topRight + middle + bottomRight + bottom
            val zero = top + topLeft + topRight + bottomLeft + bottomRight + bottom

            val resultNumber = second.map {
                it.toList()
            }.map {
                if (one.containsAll(it)) {
                    1
                } else if (seven.containsAll(it)) {
                    7
                } else if (four.containsAll(it)) {
                    4
                } else if (two.containsAll(it)) {
                    2
                } else if (three.containsAll(it)) {
                    3
                } else if (five.containsAll(it)) {
                    5
                } else if (six.containsAll(it)) {
                    6
                } else if (nine.containsAll(it)) {
                    9
                } else if (zero.containsAll(it)) {
                    0
                } else {
                    8
                }
            }.mapIndexed { index, i ->
                when (index) {
                    0 -> i * 1000
                    1 -> i * 100
                    2 -> i * 10
                    3 -> i
                    else -> 0
                }
            }.reduce { acc, i -> acc + i }
            resultNumber
        }.reduce { acc, i ->
            acc + i
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 26)
    val part2 = part2(testInput)
    println(part2)
     check(part2 == 61229)


    /**
     * example
     * one = [gf] -> topRight = bottomRight = gf
     * seven = [gcf] -> top = c
     * four = [gaef] -> topLeft = middle = ae
     * eight = [dcaebfg] -> bottomLeft = bottom = bd
     *
     * three = [gcafb, ecagb, fdbac, cfgab, bagce] U $one = [gcafb] -> middle = a, topLeft = e, bottom = b, bottomLeft = d
     * six = !([abcdeg, cafbge, fegbdc] U $one) = [abcdeg] -> topRight = f, bottomRight = g
     *
     * bagce -> c
     *       ->e
     *       -> a
     *       ->   g
     *       -> b
      */

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

private fun String.extractNumbers(): Pair<List<String>, List<String>> {
    val map = this.split(" | ")
        .map { it.split(" ") }
    return map[0] to map[1]

}
