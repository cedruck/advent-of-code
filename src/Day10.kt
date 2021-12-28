import java.util.*

fun main() {

    fun findSyntaxError(line: List<Ch>): Ch? {
        val deque: Deque<Ch> = ArrayDeque()
        line.forEach {
            if (it.isOpener) {
                deque.add(it)
            } else {
                if (it.isCloserFor(deque.last)) {
                    deque.removeLast()
                } else return it
            }
        }
        return null
    }

    fun part1(input: List<String>): Int {

        return input.map {
            it.map { Ch.from(it) }
        }.mapNotNull {
            findSyntaxError(it)
        }.map {
            it.syntaxCheckerPoints()
        }.reduce { acc, i -> acc + i }
    }

    fun part2(input: List<String>): Long {
        val scores = input.map {
            it.map { Ch.from(it) }
        }.filter {
            findSyntaxError(it) == null
        }.map { line ->
            // Returns remaning openers
            val deque: Deque<Ch> = ArrayDeque()
            line.forEach {
                if (it.isOpener) {
                    deque.add(it)
                } else {
                    if (it.isCloserFor(deque.last)) {
                        deque.removeLast()
                    } else throw IllegalStateException("Shouldn't be here after sanitizing")
                }
            }
            deque
        }.map { deque ->
            // guesses closers
            deque.descendingIterator().asSequence()
                .map {
                    it.toCloser()
                }
        }.map { seq ->
            // calculate points
            seq.map {
                it.autocompletePoints()
            }.fold(0L) { acc, next ->
                acc * 5L + next
            }
        }.sorted()

        return scores[(scores.size-1)/2]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val part1 = part1(testInput)
    println(part1)
    check(part1 == 26397)
    val part2 = part2(testInput)
    println(part2)
    check(part2 == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

enum class Ch(val c: Char, val isOpener: Boolean) {
    PAR_O('(', true),
    BRA_O('[', true),
    ACC_O('{', true),
    CHE_O('<', true),
    PAR_C(')', false),
    BRA_C(']', false),
    ACC_C('}', false),
    CHE_C('>', false),
    ;

    public val isCloser = !isOpener

    companion object {
        fun from(c: Char) = values().first { it.c == c }
    }

    fun isCloserFor(ch: Ch): Boolean {
        return when (this) {
            PAR_C -> PAR_O == ch
            BRA_C -> BRA_O == ch
            ACC_C -> ACC_O == ch
            CHE_C -> CHE_O == ch
            else -> false // its an opener, duh
        }
    }

    fun syntaxCheckerPoints(): Int {
        return when (this) {
            PAR_C -> 3
            BRA_C -> 57
            ACC_C -> 1197
            CHE_C -> 25137
            else -> 0 // its an opener, duh
        }
    }

    fun autocompletePoints(): Long {
        return when (this) {
            PAR_C -> 1
            BRA_C -> 2
            ACC_C -> 3
            CHE_C -> 4
            else -> throw IllegalStateException()
        }
    }

    fun toCloser(): Ch {
        return when (this) {
            PAR_O -> PAR_C
            BRA_O -> BRA_C
            ACC_O -> ACC_C
            CHE_O -> CHE_C
            else -> throw IllegalStateException()
        }
    }
}
