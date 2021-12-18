package days

import Day
import readlinesFromDay
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

class Day07 : Day() {

    fun distance(dist: Double): Double {
        return dist * (dist + 1) / 2
    }

    val crabPositions = readlinesFromDay(7)[0].split(",").map { it -> it.toInt() }.sortedBy { it }

    override fun solveP1() {
        val size = crabPositions.size
        val median = (crabPositions[size / 2] + crabPositions[(size - 1) / 2]) / 2
        val linearDistance = crabPositions.sumOf { abs(it - median) }
        println(linearDistance)
    }

    override fun solveP2() {
        val avg = crabPositions.average()

        val squaredDistance =
            listOf(floor(avg), ceil(avg)).map { crabPositions.sumOf { crab -> distance(abs(crab - it)) } }.minOf { it }
                .roundToInt()
        println(squaredDistance)
    }
}