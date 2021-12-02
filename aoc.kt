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

fun day2() {
    
    class ParseLine(line: String) {
        val command = line.split(" ").first()
        val amount = line.split(" ").last().toInt()
        operator fun component1(): String = command
        operator fun component2(): Int = amount
    }

    val input = readlinesFromDay(2);
    
    // Part 1    
    var pos: Pair<Int, Int> = Pair(0, 0)

    for (line: String in input) {
        var (command, amount) = ParseLine(line)
        when (command) {
            "forward" -> pos = pos.copy(first = pos.first + amount)
            "down" -> pos = pos.copy(second = pos.second + amount)
            "up" -> pos = pos.copy(second = pos.second - amount)
        }
    }

    println(pos.first * pos.second)
    // Part 2
    pos = Pair(0, 0)
    var aim: Int = 0

    for (line: String in input) {
        var (command, amount) = ParseLine(line)
        when (command) {
            "forward" -> pos = Pair(pos.first + amount, pos.second + (aim * amount))
            "down" -> aim += amount
            "up" -> aim -= amount
        }
    }
    
    println(pos.first * pos.second)
}

fun main() {      
    // day1();                  
    day2();
}