package days

import Day
import readlinesFromDay
import kotlin.math.max
import kotlin.math.min

class Day05 : Day() {

    fun String.parseCoords(): List<Int> {
        return this.split(" -> ", ",").map { it -> it.toInt() }
    }

    fun solve(ignoreDiagonals: Boolean): Int {
        val sizeGrid = 1000
        val grid = Array(sizeGrid) { Array(sizeGrid) { 0 } }
        for (line in input) {
            val (x1, y1, x2, y2) = line.parseCoords()
            if (x1 == x2) {
                for (idx in min(y1, y2)..max(y1, y2)) {
                    grid[idx][x1] += 1
                }
            } else if (y1 == y2) {
                for (idx in min(x1, x2)..max(x1, x2)) {
                    grid[y1][idx] += 1
                }
            } else if (!ignoreDiagonals) {
                val initialY = if (x1 < x2) y1 else y2
                val finalY = if (x1 < x2) y2 else y1
                val slope = if (initialY < finalY) 1 else -1
                for ((it, idx_x) in (min(x1, x2)..max(x1, x2)).withIndex()) {
                    grid[initialY + (slope * it)][idx_x] += 1
                }
            }
        }
        var overlap = 0
        for (line in grid) {
            for (number in line) {
                if (number >= 2) {
                    overlap += 1
                }
            }
        }
        return overlap
    }

    val input: List<String> = readlinesFromDay(5)

    override fun solveP1() {
        println(solve(true))
    }

    override fun solveP2() {
        println(solve(false))
    }
}