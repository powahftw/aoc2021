package days

import Day
import readlinesFromDay

class Day03 : Day() {

    fun String.bitToInt() = Integer.parseInt(this, 2)

    fun commonBit(candidates: List<String>, idx: Int, preferHigh: Boolean): Char {
        var count: Map<Char, Int> = candidates.map { it -> it.get(idx) }.groupingBy { it }.eachCount()
        if (count.getOrDefault('1', 0) == count.getOrDefault('0', 0)) {
            return if (preferHigh) '1' else '0'
        }
        if (count.getOrDefault('1', 0) > count.getOrDefault('0', 0)) {
            return if (preferHigh) '1' else '0'
        }
        return if (preferHigh) '0' else '1'
    }

    val input = readlinesFromDay(3)

    override fun solveP1() {
        var gammaRateS = ""
        for (idx: Int in input[0].indices) {
            gammaRateS += commonBit(input, idx, false)
        }
        var gammaRate = gammaRateS
        var epsilonRate = gammaRateS.map { it -> if (it == '0') "1" else "0" }.joinToString(separator = "")
        var power = epsilonRate.bitToInt() * gammaRate.bitToInt()
        println(power)
    }

    override fun solveP2() {
        var co2Candidates: List<String> = input.toMutableList()
        var o2Candidates: List<String> = input.toMutableList()
        for (idx: Int in input[0].indices) {
            if (o2Candidates.size > 1) {
                val maxCommon = commonBit(o2Candidates, idx, true)
                o2Candidates = o2Candidates.filter { it -> it[idx] == maxCommon }
            }
            if (co2Candidates.size > 1) {
                val minCommon = commonBit(co2Candidates, idx, false)
                co2Candidates = co2Candidates.filter { it -> it[idx] == minCommon }
            }
        }
        val lifeRating = co2Candidates[0].bitToInt() * o2Candidates[0].bitToInt()
        println(lifeRating)
    }
}