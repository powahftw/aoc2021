package aoc2021

import java.io.File

fun readlinesFromDay(day: Int): List<String> {
    val file = File("in/${day}.txt")
    if (!file.exists()) { 
        throw Exception("Missing input file for day $day")
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
        val (command, amount) = ParseLine(line)
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
        val (command, amount) = ParseLine(line)
        when (command) {
            "forward" -> pos = Pair(pos.first + amount, pos.second + (aim * amount))
            "down" -> aim += amount
            "up" -> aim -= amount
        }
    }
    
    println(pos.first * pos.second)
}

fun day3() {

    fun String.bitToInt() = Integer.parseInt(this, 2)

    fun commonBit(candidates: List<String>, idx: Int, preferHigh: Boolean): Char {
        var count: Map<Char, Int> = candidates.map { it -> it.get(idx) }. groupingBy { it }.eachCount()
        if (count.getOrDefault('1', 0) == count.getOrDefault('0', 0)) { return if (preferHigh) '1' else '0' }
        if (count.getOrDefault('1', 0) > count.getOrDefault('0', 0)) { return if (preferHigh) '1' else '0' }
        return if (preferHigh) '0' else '1'
    }
    val input = readlinesFromDay(3)
    var gammaRateS = ""
    for (idx: Int in input[0].indices) {
        gammaRateS += commonBit(input, idx, false)
    }
    var gammaRate = gammaRateS
    var epsilonRate = gammaRateS.map { it -> if (it == '0') "1" else "0" }.joinToString(separator = "")
    var power = epsilonRate.bitToInt() * gammaRate.bitToInt()
    println(power)

    var co2Candidates: List<String> = input.toMutableList()
    var o2Candidates: List<String> = input.toMutableList()
    for (idx: Int in input[0].indices) {
        if (o2Candidates.size > 1) {
            val maxCommon = commonBit(o2Candidates, idx, true)
            o2Candidates = o2Candidates.filter { it -> it[idx] == maxCommon}
        }
        if (co2Candidates.size > 1) {
            val minCommon = commonBit(co2Candidates, idx, false)
            co2Candidates = co2Candidates.filter { it -> it[idx] == minCommon}
        }
    }
    val lifeRating = co2Candidates[0].bitToInt() * o2Candidates[0].bitToInt()
    println(lifeRating)
    
}

fun day4() {
    
    fun MutableList<MutableList<Int>>.tickNumber(n: Int) {
        for (idx_r in this.indices) {
            for (idx_c in this[idx_r].indices) {
                if (this[idx_r][idx_c] == n) {
                    this[idx_r][idx_c] = -1
                    }
            }
        }
    }

    fun MutableList<MutableList<Int>>.isWinner(): Boolean {
        for (line in this) {
            if (line.sum() == -5) {
                return true
            }
        }
        
        for (idx_c in this[0].indices) {
            var sumCol = 0
            for (idx_r in this.indices) {
                sumCol += this[idx_r][idx_c]
            }
            if (sumCol == -5) {
                return true
            }
        }
        return false
    }

    fun MutableList<MutableList<Int>>.sumM(): Int {
        var totSum = 0
        for (line in this) {
            totSum += line.sumOf { it -> if (it == -1) 0 else it }
        }
        return totSum
    }

    val input = readlinesFromDay(4)
    val extractedNumbers = input.first().split(",").map { it -> it.toInt() }
    val bingoCards: MutableMap<Int, MutableList<MutableList<Int>>> = mutableMapOf<Int, MutableList<MutableList<Int>>>()
    var currentBingoCardN = 0
    var currentCard: MutableList<MutableList<Int>> = mutableListOf<MutableList<Int>>()
    var numFoundInCard: MutableMap<Int, MutableList<Int>> = mutableMapOf<Int, MutableList<Int>>()
    for (line in input.slice(3 until input.size)) {
        if (line.isEmpty()) {
            currentBingoCardN++
            bingoCards[currentBingoCardN] = currentCard
            currentCard = mutableListOf<MutableList<Int>>()
        }
        else {
            val foundNumbers = line.trim().split(" +".toRegex()).map { it -> it.toInt() }.toMutableList()
            currentCard.add(foundNumbers)
        }
    }
    currentBingoCardN++
    bingoCards[currentBingoCardN] = currentCard
    println(bingoCards)

    fun extractAndFindWinnerSum(extractedNumbers: List<Int>, bingoCards: MutableMap<Int, MutableList<MutableList<Int>>>) {
        val winners: MutableSet<Int> = mutableSetOf<Int>()
        for (extractedNumber in extractedNumbers) {
            for ((key, bingoCard) in bingoCards) {
                if (winners.contains(key)) { continue }
                bingoCard.tickNumber(extractedNumber)
                if (bingoCard.isWinner()) {
                    winners.add(key)
                    if (winners.size == 1 || winners.size == bingoCards.size) {
                        println(bingoCard.sumM() * extractedNumber)
                    }
                    if (winners.size == bingoCards.size) {
                        return
                    }
                }
            }
        }
    }

    extractAndFindWinnerSum(extractedNumbers, bingoCards)
}

fun main() {     
    // day1()              
    // day2()
    day3()
    // day4()
}