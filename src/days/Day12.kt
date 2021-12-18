package days

import Day
import readlinesFromDay

class Day12() : Day() {

    fun String.isSmallCave(): Boolean {
        return this.all { it.isLowerCase() }
    }
    
    fun visitCave(
        currPos: String,
        grid: MutableMap<String, MutableSet<String>>,
        visitedSoFar: MutableList<String>,
        usedDoubleVisit: Boolean
    ): Set<String> {
        if (currPos == "end") {
            return setOf(visitedSoFar.joinToString(",").plus(",end"))
        }
        var willUseDoubleVisit = false
        if (currPos.isSmallCave() && visitedSoFar.contains(currPos)) {
            if (usedDoubleVisit || currPos == "start") {
                return setOf()
            } else {
                willUseDoubleVisit = true
            }
        }
        val solutionsFromHere: MutableSet<String> = mutableSetOf()
        val visited = visitedSoFar.toMutableList()
        visited.add(currPos)
        for (moveTo in grid[currPos]!!) {
            solutionsFromHere.addAll(visitCave(moveTo, grid, visited, usedDoubleVisit || willUseDoubleVisit))
        }
        return solutionsFromHere
    }

    val grid: MutableMap<String, MutableSet<String>> = mutableMapOf()

    init {
        readlinesFromDay(12).map { line -> line.split("-") }.forEach { (from, to) ->
            if (!grid.contains(from)) grid[from] = mutableSetOf()
            if (!grid.contains(to)) grid[to] = mutableSetOf()
            grid[from]?.add(to)
            grid[to]?.add(from)
        }
    }

    override fun solveP1() {
        val p1 = visitCave("start", grid, mutableListOf(), true)
        println(p1.size)
    }

    override fun solveP2() {
        val p2 = visitCave("start", grid, mutableListOf(), false)
        println(p2.size)
    }
}