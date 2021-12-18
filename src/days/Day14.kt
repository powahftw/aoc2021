package days

import Day
import readlinesFromDay
import java.math.BigInteger

class Day14 : Day() {

    fun solve(startingFrequencyMap: MutableMap<String, Int>, times: Int): BigInteger {
        var pairFrequency = startingFrequencyMap.mapValues { it.value.toBigInteger() }
        repeat(times) {
            val newPairFrequency = mutableMapOf<String, BigInteger>()
            for ((pair, value) in pairFrequency) {
                val newElement = replaceMap[pair]
                val firstNewPolymer = "${pair[0]}${newElement}"
                val secondNewPolymer = "${newElement}${pair[1]}"
                newPairFrequency[firstNewPolymer] =
                    newPairFrequency.getOrDefault(firstNewPolymer, BigInteger.ZERO) + value
                newPairFrequency[secondNewPolymer] =
                    newPairFrequency.getOrDefault(secondNewPolymer, BigInteger.ZERO) + value
            }
            pairFrequency = newPairFrequency
        }
        val counter = mutableMapOf<Char, BigInteger>()
        for ((key, repeat) in pairFrequency) {
            counter[key[1]] = counter.getOrDefault(key[1], BigInteger.ZERO) + repeat
        }
        val firstPair = pairFrequency.keys.first()
        counter[firstPair[0]] = counter.getOrDefault(firstPair[0], BigInteger.ZERO) + BigInteger.ONE
        return (counter.maxOf { it.value } - counter.minOf { it.value })
    }

    val lines = readlinesFromDay(14)
    val polymer = lines.first()
    val replaceMap = lines.slice(2 until lines.size).map { it.split(" -> ") }.associate { (from, too) -> from to too }
    val startingPolymerFrequency =
        polymer.dropLast(1).zip(polymer.drop(1)).map { (f, s) -> "${f}${s}" }.groupingBy { it }.eachCount()
            .toMutableMap()


    override fun solveP1() {
        println(solve(startingPolymerFrequency, 10))
    }

    override fun solveP2() {
        println(solve(startingPolymerFrequency, 40))
    }
}