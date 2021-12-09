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
    internal lateinit var table: List<T>

    constructor(rows: Int, columns: Int, values: List<T>) : this(rows, columns) {
        table = values.toList()
    }

    constructor(rows: Int, columns: Int, default: T) : this(rows, columns) {
        table = (0..columns-1 + columns*(rows-1)).map { default }.toList()
    }

    private fun updateAt(index: Int, update: (T) -> T): Matrix<T> {
        val tempList = table.toMutableList()
        tempList[index] = table[index].let { update.invoke(it) }
        return Matrix<T>(rows, columns, tempList)
    }

    fun findAndUpdate(test: (T) -> Boolean, update: (T)->T): Matrix<T> {
        val index = this.table.indexOfFirst { test.invoke(it) }
        if (index == -1) {
            return this
        }
        return updateAt(index, update)
    }

    fun all(test: (T) -> Boolean): List<T> = table.filter { test.invoke(it) }

    fun valuesForColumn(col: Int): List<T> {
        return (0 until rows).map { row ->
            table[col + columns * row]
        }
    }

    fun valuesForLine(line: Int): List<T> {
        return table.windowed(columns, columns)[line]
    }

    fun filterLinesBy(filter: (List<T>) -> Boolean): Matrix<T> {
        var newtable = table.windowed(columns, columns)
            .filter(filter)
        return Matrix(newtable.size, columns, newtable.flatten())
    }
}
