fun main() {

    fun readBoards(input: List<String>) = input.drop(2).windowed(5, 6)
        .map { board ->
            board.flatMap { line ->
                line.split(" ").filter { it != "" }
            }.map { BingoNumber(it.toInt()) }
        }

    fun part1(input: List<String>): Int {
        val drawnNumbers = input[0].split(',').map { it.toInt() }

        val boards = readBoards(input).map { Matrix(5, 5, it) }
            .toMutableList()

        var win: Int? = null

        for (draw in drawnNumbers) {
            for ((index, board) in boards.withIndex()) {
                val newBoard = board.findAndUpdateOne({ it.value == draw }, { it.pick() })
                boards[index] = newBoard
                if (newBoard.hasCompleteColumn() || newBoard.hasCompleteLine()) {
                    win = draw * newBoard.all { !it.hasBeenPicked }.sumOf { it.value }
                    break
                }
            }
            if (win != null) {
                break
            }
        }

        return win ?: -1
    }

    data class WinableBoard(val board: Matrix<BingoNumber>, val hasWon: Boolean = false)

    fun part2(input: List<String>): Int {
        val drawnNumbers = input[0].split(',').map { it.toInt() }

        val boards = readBoards(input).map { WinableBoard(Matrix(5, 5, it)) }
            .toMutableList()

        var win: Int? = null

        val drawnIterator = drawnNumbers.iterator()
        do {
            val draw = drawnIterator.next()
            for ((index, board) in boards.withIndex()) {
                val newBoard = board.board.findAndUpdateOne({ it.value == draw }, { it.pick() })
                boards[index] = WinableBoard(newBoard)
                if (newBoard.hasCompleteColumn() || newBoard.hasCompleteLine()) {
                    boards[index] = WinableBoard(newBoard, true)
                    win = draw * newBoard.all { !it.hasBeenPicked }.sumOf { it.value }
                }
            }
            boards.removeIf { it.hasWon }
        } while (boards.size > 0)

        return win ?: -1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

data class BingoNumber(val value: Int, var hasBeenPicked: Boolean = false) {
    fun pick(): BingoNumber {
        return BingoNumber(value, true)
    }
}

private fun Matrix<BingoNumber>.hasCompleteLine(): Boolean {
    return this.table.windowed(this.columns, this.columns).any { lines ->
        lines.map { it.value }.all { it.hasBeenPicked }
    }
}

private fun Matrix<BingoNumber>.hasCompleteColumn(): Boolean {
    return (0 until columns)
        .any { col ->
            valuesForColumn(col).all {
                it.hasBeenPicked
            }
        }
}

