package aoc2021

import java.io.File
import java.math.BigInteger
import java.util.*
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.max
import kotlin.math.roundToInt

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
    val bingoCards: MutableMap<Int, MutableList<MutableList<Int>>> = mutableMapOf()
    var currentBingoCardN = 0
    var currentCard: MutableList<MutableList<Int>> = mutableListOf<MutableList<Int>>()
    for (line in input.slice(3 until input.size)) {
        if (line.isEmpty()) {
            currentBingoCardN++
            bingoCards[currentBingoCardN] = currentCard
            currentCard = mutableListOf()
        }
        else {
            val foundNumbers = line.trim().split(" +".toRegex()).map { it -> it.toInt() }.toMutableList()
            currentCard.add(foundNumbers)
        }
    }
    currentBingoCardN++
    bingoCards[currentBingoCardN] = currentCard

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

fun day5() {
    fun String.parseCoords(): List<Int> {
        return this.split(" -> ", ",").map {it -> it.toInt()}
    }

    val input = readlinesFromDay(5)

    fun solve(ignoreDiagonals: Boolean): Int {
        val sizeGrid = 1000
        val grid = Array(sizeGrid) { Array(sizeGrid) { 0 } }
        for (line in input) {
            val (x1, y1, x2, y2) = line.parseCoords()
            if (x1 == x2) {
                for (idx in min(y1, y2)..max(y1, y2)) {
                    grid[idx][x1] += 1
                }
            }
            else if (y1 == y2) {
                for (idx in min(x1, x2)..max(x1, x2)) {
                    grid[y1][idx] += 1
                }
            }
            else if (!ignoreDiagonals) {
                val initialY = if (x1 < x2) y1 else y2
                val finalY = if (x1 < x2) y2 else y1
                val slope = if (initialY < finalY) 1 else -1
                for ((it, idx_x) in (min(x1, x2)..max(x1, x2)).withIndex()) {
                    grid[initialY + (slope * it)][idx_x] += 1
                }
            }
        }
        var overlap = 0
        for (line in grid) {
            for (number in line) {
                if (number >= 2) {
                    overlap += 1
                }
            }
        }
        return overlap
    }
    println(solve(true))
    println(solve(false))
}

fun day6() {

    val input = readlinesFromDay(6)

    fun solve(days: Int): BigInteger {
        val fishesCount = input[0].split(",").map { it -> it.toInt() }.groupingBy { it }.eachCount()
        val fishes = Array<BigInteger>(9) { BigInteger.valueOf(fishesCount.getOrDefault(it, 0).toLong()) }
        for (step in 1..days) {
            val willSpawn = fishes[0]
            for (idx in 0..7) {
                fishes[idx] = fishes[idx + 1]
            }
            fishes[6] += willSpawn
            fishes[8] = willSpawn
        }
        return fishes.fold(BigInteger.ZERO) { acc, e -> acc + e }
    }

    println(solve(80))
    println(solve(256))
}

fun day7() {

    fun distance(x: Int, y: Int): Int {
        var distance = 0
        for (k in 1..abs(x - y)) {
            distance += k
        }
        return distance
    }

    val crabPositions = readlinesFromDay(7)[0].split(",").map { it -> it.toInt() }.sortedBy { it }
    val size = crabPositions.size
    val median = (crabPositions[size / 2] + crabPositions[(size - 1)/ 2]) / 2
    val avg = crabPositions.average()
    val linearDistance = crabPositions.sumOf { it -> abs(it - median) }
    val squaredDistance = crabPositions.sumOf { it -> distance(it, avg.roundToInt()) }
    println(linearDistance)
    println(squaredDistance)
    }

typealias Pattern = Set<Char>
fun day8() {

    fun findPattern(signalPatterns: List<Pattern>, length: Int, vararg overlapRules: Pair<Pattern, Int>): Pattern {
        return signalPatterns.first { it.size == length && overlapRules.all { rule -> rule.first.intersect(it).size == rule.second }}
    }

    val lines = readlinesFromDay(8)
    var count1478 = 0
    var outputSum = 0
    for (line in lines) {
        val (signalPatterns, output) = line.split("|").map { it.trim().split(" ") }
        count1478 += output.count { setOf(2, 3, 4, 7).contains(it.length) }
        val signalPatternsSet = signalPatterns.map { it.toCharArray().toSet() }

        val patternsMap = Array<Pattern>(10){setOf()}
        patternsMap[1] = findPattern(signalPatternsSet,2)
        patternsMap[4] = findPattern(signalPatternsSet,4)
        patternsMap[7] = findPattern(signalPatternsSet, 3)
        patternsMap[8] = findPattern(signalPatternsSet, 7)

        patternsMap[2] = findPattern(signalPatternsSet, 5, Pair(patternsMap[4], 2))
        patternsMap[3] = findPattern(signalPatternsSet, 5, Pair(patternsMap[1], 2))
        patternsMap[5] = findPattern(signalPatternsSet, 5, Pair(patternsMap[1], 1), Pair(patternsMap[4], 3))

        patternsMap[0] = findPattern(signalPatternsSet, 6, Pair(patternsMap[1], 2), Pair(patternsMap[4], 3))
        patternsMap[6] = findPattern(signalPatternsSet, 6, Pair(patternsMap[1], 1))
        patternsMap[9] = findPattern(signalPatternsSet, 6, Pair(patternsMap[4], 4))

        outputSum += output.map { value -> value.toCharArray().toSet() }.map { patternsMap.indexOf(it).toString() }.reduce { acc, it -> acc + it}.toInt()
    }
    println(count1478)
    print(outputSum)
}

fun day9() {
    val moves = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1))
    val grid = readlinesFromDay(9).map { s -> s.map { it.digitToInt() }.toMutableList() }.toMutableList()
    var sumOfRiskLevels = 0
    val lowPoints: MutableSet<Pair<Int, Int>> = mutableSetOf()
    for (idxR in grid.indices) {
        for (idxC in grid[idxR].indices) {
            var lowest = true
            for ((dx, dy) in moves) {
                val newR = idxR + dy
                val newC = idxC + dx
                val isOutOfBounds = newR < 0 || newR >= grid.size || newC < 0 || newC >= grid[idxR].size
                if (!isOutOfBounds && grid[newR][newC] <= grid[idxR][idxC]) lowest = false
            }
            if (lowest) {
                lowPoints.add(Pair(idxR, idxC))
                sumOfRiskLevels += grid[idxR][idxC] + 1
            }
        }
    }

    println(sumOfRiskLevels)

    fun exploreBasin(grid: MutableList<MutableList<Int>>, newR: Int, newC: Int): Int {
        if (newR < 0 || newR >= grid.size || newC < 0 || newC >= grid[0].size) return 0
        if (grid[newR][newC] == -1 || grid[newR][newC] == 9) return 0
        grid[newR][newC] = -1
        return 1 + moves.sumOf { (dx, dy) -> exploreBasin(grid, newR + dy, newC + dx) }
    }

    val basinToSize: MutableMap<Pair<Int, Int>, Int> = mutableMapOf()
    for (lowPoint in lowPoints) {
        basinToSize[lowPoint] = exploreBasin(grid, lowPoint.first, lowPoint.second)
    }
    val sizeOfBiggest3 = basinToSize.values.toList().sortedDescending().slice(0..2)
    println(sizeOfBiggest3.reduce { acc, next -> acc * next })
}

fun day10() {

    val errorScore = mapOf(')' to 3 , ']' to 57, '}' to 1197, '>' to 25137)
    val incompleteScore = mapOf(')' to 1 , ']' to 2, '}' to 3, '>' to 4)
    val openToClose = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')

    fun String.isLineIncomplete(): Boolean {
        val stack = ArrayDeque<Char>()
        this.forEach { symbol ->
            if (openToClose.contains(symbol)) stack.push(symbol)
            else if (stack.size > 0 && openToClose[stack.peek()] == symbol) stack.pop()
            else return false
        }
        return !stack.isEmpty()
    }

    fun String.syntaxErrorScore(): Int {
        val stack = ArrayDeque<Char>()
        this.forEach { symbol ->
            if (openToClose.contains(symbol)) stack.push(symbol)
            else if (stack.size > 0 && openToClose[stack.peek()] == symbol) stack.pop()
            else return errorScore[symbol]!!
        }
        return 0
    }

    fun String.incompleteScore(): BigInteger {
        val stack = ArrayDeque<Char>()
        this.forEach { symbol ->
            if (openToClose.contains(symbol)) stack.push(symbol)
            else if (stack.size > 0 && openToClose[stack.peek()] == symbol) stack.pop()
        }
        var score = BigInteger.ZERO
        while (!stack.isEmpty()) {
            score = score * 5.toBigInteger() + incompleteScore[openToClose[stack.pop()]]!!.toBigInteger()
        }
        return score
    }

    val totSyntaxErrorScore = readlinesFromDay(10).filter { line -> !line.isLineIncomplete() }.sumOf { line -> line.syntaxErrorScore() }
    val incompleteLinesScores = readlinesFromDay(10).filter { line -> line.isLineIncomplete()}.map { line -> line.incompleteScore() }.sortedBy { it }

    println(totSyntaxErrorScore)
    println(incompleteLinesScores[incompleteLinesScores.size / 2])


}

data class Cord(val x: Int, val y: Int)
typealias Grid = MutableList<MutableList<Int>>

fun day11() {
    val moves  = listOf(Pair(1, 0), Pair(0, 1), Pair(-1, 0), Pair(0, -1), Pair(1, 1), Pair(-1, -1), Pair(-1, 1), Pair(1, -1))

    fun getNeighboursCords(grid: Grid, idx_r: Int, idx_c: Int): List<Cord> {
        return moves.map { (f, s) -> Cord(idx_r + f, idx_c + s) }.filter { (x, y) ->  !(x < 0 || x >= grid.size || y < 0 || y >= grid[x].size) }
    }

    fun Grid.increase() {
        val flashedToCheck = ArrayDeque<Cord>()
        for (idx_r in this.indices) {
            for (idx_c in this[idx_r].indices) {
                this[idx_r][idx_c] += 1
                if (this[idx_r][idx_c] > 9) { flashedToCheck.add(Cord(idx_r, idx_c))}
            }
        }
        while (!flashedToCheck.isEmpty()) {
            val (x, y) = flashedToCheck.pop()
            for ((new_x, new_y) in getNeighboursCords(this, x, y)) {
                if (this[new_x][new_y] == 9) flashedToCheck.add(Cord(new_x, new_y))
                this[new_x][new_y]++
            }
        }
    }

    fun Grid.countAndResetOctopuses(): Int {
        var count = 0
        for (idx_r in this.indices) {
            for (idx_c in this[idx_r].indices) {
                if (this[idx_r][idx_c] > 9) {
                    this[idx_r][idx_c] = 0
                    count++
                }
            }
        }
        return count
    }

    fun Grid.increaseCountAndReset(): Int {
        this.increase()
        return this.countAndResetOctopuses()
    }

    var map = readlinesFromDay(11).map { s -> s.map { it.digitToInt() }.toMutableList() }.toMutableList()
    val totFlashes = (1..100).map { map.increaseCountAndReset() }.reduce { acc, it -> acc + it}
    println(totFlashes)

    map = readlinesFromDay(11).map { s -> s.map { it.digitToInt() }.toMutableList() }.toMutableList()
    val synchronizedFlashStep = (1..1000).first { map.increaseCountAndReset() == map.size * map[0].size }
    println(synchronizedFlashStep)
}


fun day12() {
    fun String.isSmallCave(): Boolean {
        return this.all { it.isLowerCase() }
    }

    val grid: MutableMap<String, MutableSet<String>> = mutableMapOf()
    readlinesFromDay(12).map { line -> line.split("-") }.forEach { (from, to) ->
        if (!grid.contains(from)) grid[from] = mutableSetOf()
        if (!grid.contains(to)) grid[to] = mutableSetOf()
        grid[from]?.add(to)
        grid[to]?.add(from)
    }

    fun visitCave(currPos: String, grid: MutableMap<String, MutableSet<String>>, visitedSoFar: MutableList<String>, usedDoubleVisit: Boolean): Set<String> {
        if (currPos == "end") { return setOf(visitedSoFar.joinToString(",").plus(",end")) }
        var willUseDoubleVisit = false
        if (currPos.isSmallCave() && visitedSoFar.contains(currPos)) {
            if (usedDoubleVisit || currPos == "start") { return setOf() }
            else { willUseDoubleVisit = true }
        }
        val solutionsFromHere: MutableSet<String> = mutableSetOf()
        val visited = visitedSoFar.toMutableList()
        visited.add(currPos)
        for (moveTo in grid[currPos]!!) {
            solutionsFromHere.addAll(visitCave(moveTo, grid, visited, usedDoubleVisit || willUseDoubleVisit))
        }
        return solutionsFromHere
    }

    val p1 = visitCave("start", grid, mutableListOf(), true)
    println(p1.size)
    val p2 = visitCave("start", grid, mutableListOf(), false)
    println(p2.size)
}

typealias IntGrid = Array<Array<Int>>

fun day13() {

    fun IntGrid.fold(alongX: Boolean, value: Int): IntGrid {
        val newSizeR = if (alongX) this.size else this.size - value
        val newSizeC = if (alongX) value else this[0].size
        val newGrid = Array(newSizeR) { Array(newSizeC) { 0 } }
        for (idx_r in this.indices) {
            for (idx_c in this[idx_r].indices) {
                // Don't copy over elements on the fold.
                if (!alongX && idx_r == value || alongX && idx_c == value) { continue }
                // No point in copying empty elements.
                else if (this[idx_r][idx_c] == 0) { continue }
                else if (!alongX && idx_r > value) { newGrid[2 * value - idx_r][idx_c] = 1 }
                else if (alongX && idx_c > value)  { newGrid[idx_r][2 * value - idx_c] = 1 }
                else { newGrid[idx_r][idx_c] = max(this[idx_r][idx_c], newGrid[idx_r][idx_c]) }
            }
        }
        return newGrid
    }

    fun IntGrid.count(): Int {
        return this.sumOf { row -> row.sumOf { it } }
    }

    fun IntGrid.displayCode(): Unit {
        for (line in this.slice(0..8)) {
            println(line.joinToString("").replace("1", "##").replace("0", "  "))
        }
    }

    val sizeGrid = 1400
    var grid = Array(sizeGrid) { Array(sizeGrid) { 0 } }
    var parseCord = true
    var points = mutableListOf<Int>()
    readlinesFromDay(13).forEach {
        if (it.isEmpty()) { parseCord = false }
        else if (parseCord) {
            var (x, y) = it.split(",").map { it.toInt() }
            grid[y][x] = 1
        } else {
            var (command, cord) = it.split("=")
            grid = grid.fold(command.contains("x"), cord.toInt())
            points.add(grid.count())
        }
    }
    println(points.first())
    grid.displayCode()
}

fun day14() {
    val lines = readlinesFromDay(14)
    val polymer = lines.first()
    val replaceMap = lines.slice(2 until lines.size).map { it.split(" -> ") }.associate { (from, too) -> from to too }
    val startingPolymerFrequency =
        polymer.dropLast(1).zip(polymer.drop(1)).map { (f, s) -> "${f}${s}"}.groupingBy { it }.eachCount().toMutableMap()

    fun solve(startingFrequencyMap: MutableMap<String, Int>, times: Int): BigInteger {
        var pairFrequency = startingFrequencyMap.mapValues { it.value.toBigInteger() }
        repeat(times) {
            val newPairFrequency = mutableMapOf<String, BigInteger>()
            for ((pair, value) in pairFrequency) {
                val newElement = replaceMap[pair]
                val firstNewPolymer = "${pair[0]}${newElement}"
                val secondNewPolymer = "${newElement}${pair[1]}"
                newPairFrequency[firstNewPolymer] = newPairFrequency.getOrDefault(firstNewPolymer, BigInteger.ZERO) + value
                newPairFrequency[secondNewPolymer] = newPairFrequency.getOrDefault(secondNewPolymer, BigInteger.ZERO) + value
            }
            pairFrequency = newPairFrequency
        }
        val counter = mutableMapOf<Char, BigInteger>()
        for ((key, repeat) in pairFrequency) {
            counter[key[1]] = counter.getOrDefault(key[1], BigInteger.ZERO) + repeat
        }
        val firstPair = pairFrequency.keys.first()
        counter[firstPair[0]] = counter.getOrDefault(firstPair[0], BigInteger.ZERO) + BigInteger.ONE
        return (counter.maxOf{ it.value } - counter.minOf { it.value })
    }

    println(solve(startingPolymerFrequency, 10))
    println(solve(startingPolymerFrequency, 40))
}

fun main() {
    // day1()
    // day2()
    // day3()
    // day4()
    // day5()
    // day6()
    // day7()
    // day8()
    // day9()
    // day10()
    // day11()
    // day12()
    // day13()
    day14()

}