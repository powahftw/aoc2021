package days

import Cord
import Day
import neighbours
import plus
import readlinesFromDay
import java.util.*


data class PQItem(val el: Cord, val cost: Int)


class Day15 : Day() {

    fun getAdjPos(curr: Cord, maxR: Int, maxC: Int): List<Cord> {
        return neighbours.map { curr + it }.filter { it.x >= 0 && it.y >= 0 && it.x < maxR && it.y < maxC }
    }

    fun dijkstraCost(grid: List<List<Int>>): Int {
        val (maxR, maxC) = listOf(grid.size, grid[0].size)
        val visited = mutableSetOf<Cord>()
        val nodeCost = mutableMapOf<Cord, Int>().withDefault { Int.MAX_VALUE }

        val compareByCost: Comparator<PQItem> = compareBy { it.cost }
        val pq = PriorityQueue(compareByCost)

        val start = PQItem(Cord(0, 0), 0)
        nodeCost[start.el] = 0
        pq.add(start)

        while (!pq.isEmpty()) {
            var head = pq.remove()
            visited.add(head.el)

            for (nearby in getAdjPos(head.el, maxR, maxC)) {
                if (visited.contains(nearby)) {
                    continue
                }
                var newCost = nodeCost.getValue(head.el) + grid[nearby.y][nearby.x]
                if (nodeCost.getValue(nearby) > newCost) {
                    nodeCost[nearby] = newCost
                    pq.add(PQItem(nearby, newCost))
                }
            }
        }
        return nodeCost.getValue(Cord(maxR - 1, maxC - 1))
    }

    val grid = readlinesFromDay(15).map { it.map { line -> line.toString().toInt() } }

    override fun solveP1() {
        println(dijkstraCost(grid))
    }

    override fun solveP2() {
        val timesToExpand = 5
        val gridExpanded = List(grid.size * timesToExpand) { r ->
            List(grid[0].size * timesToExpand) { c ->
                val originalElement = grid[r % grid.size][c % grid[0].size]
                val increaseBy = (r / grid.size) + (c / grid[0].size)
                (originalElement + increaseBy - 1) % 9 + 1
            }
        }
        println(dijkstraCost(gridExpanded))
    }
}