package days

import Day
import readlinesFromDay
import kotlin.math.max
import kotlin.math.min

typealias XYZRange = Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>


fun Pair<Int, Int>.isOverlapping2D(other: Pair<Int, Int>): Boolean {
    return !(this.first > other.second || other.first > this.second)
}

fun Pair<Int, Int>.overlappingRange2D(other: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(max(this.first, other.first), min(this.second, other.second))
}

fun XYZRange.isOverlapping3D(other: XYZRange): Boolean {
    return this.toList().mapIndexed { idx, pair -> pair.isOverlapping2D(other.toList()[idx]) }.all { it }
}

fun XYZRange.overlappingRange3D(other: XYZRange): XYZRange {
    val overlappingRanges = this.toList().mapIndexed { idx, pair -> pair.overlappingRange2D(other.toList()[idx]) }
    return Triple(overlappingRanges[0], overlappingRanges[1], overlappingRanges[2])
}

fun List<String>.parseLine(): Pair<Int, XYZRange> {
    val turnOn = if (this[0].trim() == "on") 1 else -1
    val commandRanges = this.drop(1).map { it.split("..") }.map { Pair(it[0].toInt(), it[1].toInt()) }
    val commandRegion = Triple(commandRanges[0], commandRanges[1], commandRanges[2])
    return Pair(turnOn, commandRegion)
}

fun XYZRange.getNPoint(): Long {
    return (this.first.second - this.first.first + 1) *
            (this.second.second - this.second.first + 1) *
            (this.third.second - this.third.first + 1).toLong()
}

class Day22 : Day() {

    val rebootSteps = readlinesFromDay(22).map { it.replace(",", "").split("x=", "y=", "z=") }
    val initialArea = Triple(Pair(-50, 50), Pair(-50, 50), Pair(-50, 50))

    fun solve(constrainToInitialArea: Boolean): Long {
        var previouslyConsideredRegions = mapOf<XYZRange, Long>().withDefault { 0L }
        for (line in rebootSteps) {
            val parsedLine = line.parseLine()
            val turnOn = parsedLine.first
            val commandRegion = parsedLine.second

            if (!constrainToInitialArea || commandRegion.isOverlapping3D(initialArea)) {
                val currentOverlaps = mutableMapOf<XYZRange, Long>().withDefault { 0L }
                for ((previouslyConsideredRegion, count) in previouslyConsideredRegions) {
                    if (commandRegion.isOverlapping3D(previouslyConsideredRegion)) {
                        val overlap = commandRegion.overlappingRange3D(previouslyConsideredRegion)
                        currentOverlaps[overlap] = currentOverlaps.getValue(overlap) - count
                    }
                }
                if (turnOn == 1) {
                    currentOverlaps[commandRegion] = currentOverlaps.getValue(commandRegion) + 1L
                }
                previouslyConsideredRegions =
                    (currentOverlaps.keys + previouslyConsideredRegions.keys).associateBy(
                        { it }, {
                            currentOverlaps.getOrDefault(it, 0L) + previouslyConsideredRegions.getOrDefault(it, 0L)
                        }
                    )
            }
        }
        return previouslyConsideredRegions.map { (region, v) -> region.getNPoint() * v }.sumOf { it }
    }

    override fun solveP1() {
        println(solve(true))
    }

    override fun solveP2() {
        println(solve(false))
    }
}