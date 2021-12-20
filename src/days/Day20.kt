package days

import Cell
import Day
import readlinesFromDay
import java.lang.Integer.max
import java.lang.Integer.min

class Day20 : Day() {


    val cellsInOrder = listOf(
        Cell(-1, -1),
        Cell(-1, 0),
        Cell(-1, 1),
        Cell(0, -1),
        Cell(0, 0),
        Cell(0, 1),
        Cell(1, -1),
        Cell(1, 0),
        Cell(1, 1),
    )

    fun Set<Cell>.getIdxFromOrderedCell(r: Int, c: Int, shouldContain: Boolean): Int {
        return cellsInOrder.map { this.contains(Cell(r + it.r, c + it.c)) == shouldContain }
            .map { if (it) '1' else '0' }
            .joinToString("")
            .toInt(2)
    }

    fun Set<Cell>.getBoundingBox(): Pair<Cell, Cell> {
        var (minC, minR) = listOf(Int.MAX_VALUE, Int.MAX_VALUE)
        var (maxC, maxR) = listOf(Int.MIN_VALUE, Int.MIN_VALUE)

        this.forEach {
            minC = min(minC, it.c)
            minR = min(minR, it.r)
            maxC = max(maxC, it.c)
            maxR = max(maxR, it.r)
        }

        return Pair(Cell(minR - 2, minC - 2), Cell(maxR + 2, maxC + 2))
    }

    val input = readlinesFromDay(20)
    val imageEnhanceAlgorithm = input[0]
    val pattern = input.slice(2 until input.size)
    val initialPixels = mutableSetOf<Cell>()

    init {
        for ((idx_r, row) in pattern.withIndex())
            for ((idx_c, el) in row.withIndex())
                if (el == '#') {
                    initialPixels.add(Cell(idx_r, idx_c))
                }
    }

    fun solve(nIt: Int): Int {
        var pixels = initialPixels.toMutableSet()
        repeat(nIt) {
            val nextPixels = mutableSetOf<Cell>()
            val boundingBox = pixels.getBoundingBox()
            val arePixelsOn = it % 2 == 0
            for (r in boundingBox.first.r until boundingBox.second.r) {
                for (c in boundingBox.first.c until boundingBox.second.c) {
                    val enhanceIdx = pixels.getIdxFromOrderedCell(r, c, arePixelsOn)
                    if ((imageEnhanceAlgorithm[enhanceIdx] == '#') != arePixelsOn) {
                        nextPixels.add(Cell(r, c))
                    }
                }
            }
            pixels = nextPixels
        }
        return pixels.size
    }

    override fun solveP1() {
        println(solve(2))

    }

    override fun solveP2() {
        println(solve(50))
    }
}