import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

data class Matrix<T>(val rows: Int, val columns: Int) {
    internal lateinit var table: List<Point<T>>

    constructor(rows: Int, columns: Int, values: List<T>) : this(rows, columns) {
        table = values.mapIndexed { index, t -> Point(index.floorDiv(columns), index % columns, t) }
    }

    private fun updateAt(index: Int, update: (T) -> T): Matrix<T> {
        val tempList = table.map { it.value }.toMutableList()
        tempList[index] = table[index].let { update.invoke(it.value) }
        return Matrix(rows, columns, tempList)
    }

    fun findAndUpdateOne(test: (T) -> Boolean, update: (T)->T): Matrix<T> {
        val index = this.table.indexOfFirst { test.invoke(it.value) }
        if (index == -1) {
            return this
        }
        return updateAt(index, update)
    }

    fun update(vararg points: Point<T>, update: (T)->T): Matrix<T> {
        return update(points.toList(),update)
    }

    fun update(points: Collection<Point<T>>, update: (T)->T): Matrix<T> {
        var newMatrix = this
        points.forEach {
            newMatrix = newMatrix.updateAt(it.column + (it.line*columns), update)
        }
        return newMatrix
    }

    fun findAndUpdate(test: (T) -> Boolean, update: (T)->T): Matrix<T> {
        val indexes = this.table.mapIndexed { index, t -> index to t }.filter { test.invoke(it.second.value) }.map { it.first }
        var newMatrix = this
        indexes.forEach {
            newMatrix = newMatrix.updateAt(it, update)
        }
        return newMatrix
    }

    fun allPoints(test: (T) -> Boolean): Set<Point<T>> = table.filter { test.invoke(it.value) }.toSet()

    fun all(test: (T) -> Boolean): List<T> = this.allPoints(test).map { it.value }

    fun valuesForColumn(col: Int): List<T> {
        return pointsForColumn(col).map { it.value }
    }

    fun pointsForColumn(col: Int): List<Point<T>> {
        return (0 until rows).map { row ->
            table[col + columns * row]
        }
    }

    fun valuesForLine(line: Int): List<T> {
        return pointsForLine(line).map { it.value }
    }

    fun pointsForLine(line: Int): List<Point<T>> {
        return table.windowed(columns, columns)[line]
    }

    fun value(line: Int, column: Int): T? {
        return point(line, column)?.value
    }

    fun point(line: Int, column: Int): Point<T>? {
        if (line < 0 || line >= rows || column < 0 || column >= columns) {
            return null
        }
        return table.windowed(columns, columns)[line][column]
    }

    fun filterLinesBy(filter: (List<T>) -> Boolean): Matrix<T> {
        val newtable = table.windowed(columns, columns)
            .map { points -> points.map { it.value } }
            .filter(filter)
        return Matrix(newtable.size, columns, newtable.flatten())
    }


    override fun toString(): String {
        return buildString {
            appendLine("$rows*$columns")
            (0 until rows).forEach {
                val s: String = pointsForLine(it).joinToString(" ") { it.value.toString() }
                appendLine(s)
            }
        }
    }

    fun toPointString(): String {
        return buildString {
            appendLine("$rows*$columns")
            (0 until rows).forEach {
                val s: String = pointsForLine(it).joinToString(" ")
                appendLine(s)
            }
        }
    }
}

data class Point<T>(val line: Int, val column: Int, val value: T)
