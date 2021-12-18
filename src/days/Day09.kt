package days

import Day
import readlinesFromDay

class Day09 : Day() {

    val moves = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))
    val grid = readlinesFromDay(9).map { s -> s.map { it.digitToInt() }.toMutableList() }.toMutableList()
    val lowPoints: MutableSet<Pair<Int, Int>> = mutableSetOf()

    override fun solveP1() {
        var sumOfRiskLevels = 0
        for (idxR in grid.indices) {
            for (idxC in grid[idxR].indices) {
                var lowest = true
                for ((dx, dy) in moves) {
                    val newR = idxR + dy
                    val newC = idxC + dx
                    val isOutOfBounds = newR < 0 || newR >= grid.size || newC < 0 || newC >= grid[idxR].size
                    if (!isOutOfBounds && grid[newR][newC] <= grid[idxR][idxC]) lowest = false
                }
                if (lowest) {
                    lowPoints.add(Pair(idxR, idxC))
                    sumOfRiskLevels += grid[idxR][idxC] + 1
                }
            }
        }
        println(sumOfRiskLevels)
    }

    override fun solveP2() {
        fun exploreBasin(grid: MutableList<MutableList<Int>>, newR: Int, newC: Int): Int {
            if (newR < 0 || newR >= grid.size || newC < 0 || newC >= grid[0].size) return 0
            if (grid[newR][newC] == -1 || grid[newR][newC] == 9) return 0
            grid[newR][newC] = -1
            return 1 + moves.sumOf { (dx, dy) -> exploreBasin(grid, newR + dy, newC + dx) }
        }

        val basinToSize: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
        for (lowPoint in lowPoints) {
            basinToSize[lowPoint] = exploreBasin(grid, lowPoint.first, lowPoint.second)
        }
        val sizeOfBiggest3 = basinToSize.values.toList().sortedDescending().slice(0..2)
        println(sizeOfBiggest3.reduce { acc, next -> acc * next })
    }
}