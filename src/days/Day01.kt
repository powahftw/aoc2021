package days

import Day
import readlinesAsIntFromDay

class Day01 : Day() {

    val inp: List<Int> = readlinesAsIntFromDay(1)

    fun helper(depths: List<Int>, slidingWindow: Int): Int {
        var prevVal: Int? = null
        var incCount = 0
        for ((idx, _) in depths.withIndex()) {
            if (idx > depths.size - slidingWindow) {
                continue
            }
            val currAvg = depths.subList(idx, idx + slidingWindow).sum()
            if (prevVal != null && currAvg > prevVal) {
                incCount++
            }
            prevVal = currAvg
        }
        return incCount
    }

    override fun solveP1() {
        println(helper(inp, 1))
    }

    override fun solveP2() {
        println(helper(inp, 3))
    }

}