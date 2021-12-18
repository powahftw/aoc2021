package days

import Day
import readlinesFromDay
import kotlin.math.max

typealias IntGrid = Array<Array<Int>>

class Day13 : Day() {

    fun IntGrid.fold(alongX: Boolean, value: Int): IntGrid {
        val newSizeR = if (alongX) this.size else this.size - value
        val newSizeC = if (alongX) value else this[0].size
        val newGrid = Array(newSizeR) { Array(newSizeC) { 0 } }
        for (idx_r in this.indices) {
            for (idx_c in this[idx_r].indices) {
                // Don't copy over elements on the fold.
                if (!alongX && idx_r == value || alongX && idx_c == value) {
                    continue
                }
                // No point in copying empty elements.
                else if (this[idx_r][idx_c] == 0) {
                    continue
                } else if (!alongX && idx_r > value) {
                    newGrid[2 * value - idx_r][idx_c] = 1
                } else if (alongX && idx_c > value) {
                    newGrid[idx_r][2 * value - idx_c] = 1
                } else {
                    newGrid[idx_r][idx_c] = max(this[idx_r][idx_c], newGrid[idx_r][idx_c])
                }
            }
        }
        return newGrid
    }

    fun IntGrid.count(): Int {
        return this.sumOf { row -> row.sumOf { it } }
    }

    fun IntGrid.displayCode(): Unit {
        for (line in this.slice(0..6)) {
            println(line.joinToString("").replace("1", "##").replace("0", "  "))
        }
    }


    val sizeGrid = 1400
    var grid = Array(sizeGrid) { Array(sizeGrid) { 0 } }
    var parseCord = true
    var points = mutableListOf<Int>()

    init {
        readlinesFromDay(13).forEach {
            if (it.isEmpty()) {
                parseCord = false
            } else if (parseCord) {
                var (x, y) = it.split(",").map { it.toInt() }
                grid[y][x] = 1
            } else {
                var (command, cord) = it.split("=")
                grid = grid.fold(command.contains("x"), cord.toInt())
                points.add(grid.count())
            }
        }
    }

    override fun solveP1() {
        println(points.first())

    }

    override fun solveP2() {
        grid.displayCode()

    }
}