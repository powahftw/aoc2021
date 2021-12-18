package days

import Cord
import Day
import neighboursWithDiagonals
import readlinesFromDay
import java.util.*


typealias Grid = MutableList<MutableList<Int>>

class Day11 : Day() {

    fun getNeighboursCords(grid: Grid, idx_r: Int, idx_c: Int): List<Cord> {
        return neighboursWithDiagonals.map { (x, y) -> Cord(idx_r + x, idx_c + y) }
            .filter { (x, y) -> !(x < 0 || x >= grid.size || y < 0 || y >= grid[x].size) }
    }

    fun Grid.increase() {
        val flashedToCheck = ArrayDeque<Cord>()
        for (idx_r in this.indices) {
            for (idx_c in this[idx_r].indices) {
                this[idx_r][idx_c] += 1
                if (this[idx_r][idx_c] > 9) {
                    flashedToCheck.add(Cord(idx_r, idx_c))
                }
            }
        }
        while (!flashedToCheck.isEmpty()) {
            val (x, y) = flashedToCheck.pop()
            for ((new_x, new_y) in getNeighboursCords(this, x, y)) {
                if (this[new_x][new_y] == 9) flashedToCheck.add(Cord(new_x, new_y))
                this[new_x][new_y]++
            }
        }
    }

    fun Grid.countAndResetOctopuses(): Int {
        var count = 0
        for (idx_r in this.indices) {
            for (idx_c in this[idx_r].indices) {
                if (this[idx_r][idx_c] > 9) {
                    this[idx_r][idx_c] = 0
                    count++
                }
            }
        }
        return count
    }

    fun Grid.increaseCountAndReset(): Int {
        this.increase()
        return this.countAndResetOctopuses()
    }


    override fun solveP1() {
        var map = readlinesFromDay(11).map { s -> s.map { it.digitToInt() }.toMutableList() }.toMutableList()
        val totFlashes = (1..100).map { map.increaseCountAndReset() }.reduce { acc, it -> acc + it }
        println(totFlashes)
    }

    override fun solveP2() {
        var map = readlinesFromDay(11).map { s -> s.map { it.digitToInt() }.toMutableList() }.toMutableList()
        val synchronizedFlashStep = (1..1000).first { map.increaseCountAndReset() == (map.size * map[0].size) }
        println(synchronizedFlashStep)
    }
}