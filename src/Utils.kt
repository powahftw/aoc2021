import java.io.File

abstract class Day {

    abstract fun solveP1()

    abstract fun solveP2()
}

data class Cord(val x: Int, val y: Int)

operator fun Cord.plus(other: Cord): Cord {
    return Cord(this.x + other.x, this.y + other.y)
}

data class Cell(val r: Int, val c: Int)

operator fun Cell.plus(other: Cell): Cell {
    return Cell(this.r + other.r, this.c + other.c)
}

val neighbours = listOf(Cord(1, 0), Cord(0, 1), Cord(-1, 0), Cord(0, -1))

val neighboursWithDiagonals = neighbours + listOf(Cord(1, 1), Cord(-1, -1), Cord(-1, 1), Cord(1, -1))

fun readlinesFromDay(day: Int): List<String> {
    val file = File("src", "in/${day}.txt")
    if (!file.exists()) {
        throw Exception("Missing input file for day $day")
    }
    return file.readLines()
}

fun readlinesAsIntFromDay(day: Int): List<Int> {
    return readlinesFromDay(day).map { it.toInt() }
}

