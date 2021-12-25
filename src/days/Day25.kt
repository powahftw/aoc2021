package days

import Day
import readlinesFromDay

typealias String2DGrid = Array<Array<String>>


class Day25 : Day() {

    val inp = readlinesFromDay(25).map { it.toList().map { it.toString() } }
    val maxR = inp.size
    val maxC = inp[0].size
    var curr = Array(maxR) { r -> Array(maxC) { c -> inp[r][c] } }

    fun String2DGrid.nextState(checkHorizontalMove: Boolean): String2DGrid {
        val next = Array(maxR) { r -> Array(maxC) { c -> this[r][c] } }
        val checkFor = if (checkHorizontalMove) ">" else "v"
        val incR = if (checkHorizontalMove) 0 else 1
        val incC = if (checkHorizontalMove) 1 else 0
        for (idxR in this.indices) {
            for (idxC in this[idxR].indices) {
                if (this[idxR][idxC] == checkFor) {
                    val nextIdxC = (idxC + incC) % maxC
                    val nextIdxR = (idxR + incR) % maxR
                    if (this[nextIdxR][nextIdxC] == ".") {
                        next[idxR][idxC] = "."
                        next[nextIdxR][nextIdxC] = checkFor
                    } else {
                        next[idxR][idxC] = checkFor
                    }
                }
            }
        }
        return next
    }

    fun String2DGrid.to1DString(): String {
        return this.map {
            it.joinToString("") { it }
        }.joinToString("\n") { it }
    }

    fun String2DGrid.anyChange(other: String2DGrid): Boolean {
        return this.to1DString() != other.to1DString()
    }

    override fun solveP1() {
        var it = 0
        do {
            val afterHorizontalMove = curr.nextState(true)
            val afterVerticalMove = afterHorizontalMove.nextState(false)
            val anyChanges = afterVerticalMove.anyChange(curr)
            curr = afterVerticalMove
            it++
        } while (anyChanges)
        println(it)
    }

    override fun solveP2() {
        println(" * MAGIC  * ")
    }
}