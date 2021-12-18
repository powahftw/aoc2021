package days

import Day
import readlinesFromDay
import kotlin.math.abs
import kotlin.math.roundToInt

class Day07 : Day() {

    fun distance(x: Int, y: Int): Int {
        var distance = 0
        for (k in 1..abs(x - y)) {
            distance += k
        }
        return distance
    }

    val crabPositions = readlinesFromDay(7)[0].split(",").map { it -> it.toInt() }.sortedBy { it }
    val size = crabPositions.size
    val median = (crabPositions[size / 2] + crabPositions[(size - 1) / 2]) / 2
    val avg = crabPositions.average()
    val linearDistance = crabPositions.sumOf { it -> abs(it - median) }
    val squaredDistance = crabPositions.sumOf { it -> distance(it, avg.roundToInt()) }

    override fun solveP1() {
        println(linearDistance)
    }

    override fun solveP2() {
        println(squaredDistance)
    }
}