package aoc2021

import java.io.File

fun readlinesFromDay(day: Int): List<String> {
    val file = File("in/${day}.txt")
    if (!file.exists()) { 
        throw Exception("Missing input file for day ${day}")
    }
    return file.readLines()
}

fun readlinesAsIntFromDay(day: Int): List<Int> {
    return readlinesFromDay(day).map { it.toInt() }
}

fun day1() {
    val input = readlinesAsIntFromDay(1)

    fun helper(depths: List<Int>, slidingWindow: Int): Int {
        var prevVal: Int? = null
        var incCount = 0
        for ((idx, _) in depths.withIndex()) {
            if (idx > depths.size - slidingWindow) { continue }
            val currAvg = depths.subList(idx, idx + slidingWindow).sum()
            if (prevVal != null && currAvg > prevVal) { incCount++ }
            prevVal = currAvg
        }
        return incCount
    }
    println(helper(input, 1))
    println(helper(input, 3))
}

fun main() {                        
    day1();
}