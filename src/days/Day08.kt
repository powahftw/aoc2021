package days

import Day
import readlinesFromDay

typealias Pattern = Set<Char>

class Day08 : Day() {

    fun findPattern(signalPatterns: List<Pattern>, length: Int, vararg overlapRules: Pair<Pattern, Int>): Pattern {
        return signalPatterns.first { it.size == length && overlapRules.all { rule -> rule.first.intersect(it).size == rule.second } }
    }

    val lines = readlinesFromDay(8)
    var count1478 = 0
    var outputSum = 0

    init {
        for (line in lines) {
            val (signalPatterns, output) = line.split("|").map { it.trim().split(" ") }
            count1478 += output.count { setOf(2, 3, 4, 7).contains(it.length) }
            val signalPatternsSet = signalPatterns.map { it.toCharArray().toSet() }

            val patternsMap = Array<Pattern>(10) { setOf() }
            patternsMap[1] = findPattern(signalPatternsSet, 2)
            patternsMap[4] = findPattern(signalPatternsSet, 4)
            patternsMap[7] = findPattern(signalPatternsSet, 3)
            patternsMap[8] = findPattern(signalPatternsSet, 7)

            patternsMap[2] = findPattern(signalPatternsSet, 5, Pair(patternsMap[4], 2))
            patternsMap[3] = findPattern(signalPatternsSet, 5, Pair(patternsMap[1], 2))
            patternsMap[5] = findPattern(signalPatternsSet, 5, Pair(patternsMap[1], 1), Pair(patternsMap[4], 3))

            patternsMap[0] = findPattern(signalPatternsSet, 6, Pair(patternsMap[1], 2), Pair(patternsMap[4], 3))
            patternsMap[6] = findPattern(signalPatternsSet, 6, Pair(patternsMap[1], 1))
            patternsMap[9] = findPattern(signalPatternsSet, 6, Pair(patternsMap[4], 4))

            outputSum += output.map { value -> value.toCharArray().toSet() }.map { patternsMap.indexOf(it).toString() }
                .reduce { acc, it -> acc + it }.toInt()
        }
    }

    override fun solveP1() {
        println(count1478)
    }

    override fun solveP2() {
        println(outputSum)
    }
}