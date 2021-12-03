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

fun day3() {

    fun convertAndMultiply(first: String, other: String): Int {
        return listOf(first, other).map { it -> Integer.parseInt(it, 2)}.reduce { acc, next -> acc * next }
    }

    fun commonBit(candidates: List<String>, idx: Int, preferHigh: Boolean): Char {
        var count: Map<Char, Int> = candidates.map { it -> it.get(idx) }. groupingBy { it }.eachCount()
        if (count.getOrDefault('1', 0) == count.getOrDefault('0', 0)) { return if (preferHigh) '1' else '0' }
        if (count.getOrDefault('1', 0) > count.getOrDefault('0', 0)) { return if (preferHigh) '1' else '0' }
        return if (preferHigh) '0' else '1'
    }
    val input = readlinesFromDay(3);
    var gamma_rate = ""
    for (idx: Int in input[0].indices) {
        var sum = 0
        for (line: String in input) {
            sum += line[idx].toString().toInt()
        }
        gamma_rate += if ((sum.toDouble() / input.size) > 0.5) "1" else "0"
    }
    var epsilon_rate = gamma_rate. map { it -> if (it == '0') "1" else "0" }.joinToString(separator = "")
    var power = convertAndMultiply(epsilon_rate, gamma_rate)
    println(power)

    var co2_candidates: List<String> = input.toMutableList()
    var o2_candidates: List<String> = input.toMutableList()
    for (idx: Int in input[0].indices) {
        if (o2_candidates.size > 1) {
            var max_common = commonBit(o2_candidates, idx, true)
            o2_candidates = o2_candidates.filter { it -> it.get(idx) == max_common} 
        }
        if (co2_candidates.size > 1) {
            var min_common = commonBit(co2_candidates, idx, false)
            co2_candidates = co2_candidates.filter { it -> it.get(idx) == min_common} 
        }
    }
    var life_rating = convertAndMultiply(co2_candidates.get(0), o2_candidates.get(0))
    println(life_rating)
    
}

fun main() {      
    // day1();                  
    // day2();
    day3();
}