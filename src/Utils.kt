import java.io.File

abstract class Day {

    abstract fun solveP1()

    abstract fun solveP2()
}

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

