package days

import Day
import readlinesFromDay
import java.util.Collections.min

class Day21 : Day() {

    val initialPos = readlinesFromDay(21).map { it.split(": ")[1] }.map { it.toInt() }.toList()

    override fun solveP1() {
        val pos = initialPos.toMutableList()
        val scores = mutableListOf(0, 0)
        var currPlayer = 0
        var dieRoll = 0
        var currRoll = 1
        while (scores.all { it < 1000 }) {
            for (roll in 0..2) {
                pos[currPlayer] += currRoll
                dieRoll += 1
                currRoll = (currRoll % 100) + 1
            }
            pos[currPlayer] = ((pos[currPlayer] - 1) % 10) + 1
            scores[currPlayer] += pos[currPlayer]
            currPlayer = if (currPlayer == 1) 0 else 1

        }
        println(min(scores) * dieRoll)
    }

    override fun solveP2() {
        fun Pair<Long, Long>.sum(other: Pair<Long, Long>): Pair<Long, Long> {
            return Pair(this.first + other.first, this.second + other.second)
        }

        val cache = mutableMapOf<Triple<Pair<Int, Int>, Pair<Int, Int>, Int>, Pair<Long, Long>>()

        fun helper(scores: Pair<Int, Int>, pos: Pair<Int, Int>, turn: Int): Pair<Long, Long> {
            if (scores.first >= 21) {
                return Pair(1L, 0)
            }
            if (scores.second >= 21) {
                return Pair(0, 1L)
            }
            val key = Triple(scores, pos, turn)
            if (cache.contains(key)) {
                return cache.getValue(key)
            }

            var winFromHere = Pair(0L, 0L)
            var newPos: Pair<Int, Int>
            var newScores: Pair<Int, Int>
            for (qd1 in 1..3) {
                for (qd2 in 1..3) {
                    for (qd3 in 1..3) {
                        val qd = qd1 + qd2 + qd3
                        if (turn == 0) {
                            newPos = Pair((pos.first + qd - 1) % 10 + 1, pos.second)
                            newScores = Pair(scores.first + newPos.first, scores.second)
                        } else {
                            newPos = Pair(pos.first, (pos.second + qd - 1) % 10 + 1)
                            newScores = Pair(scores.first, scores.second + newPos.second)
                        }
                        val subWin = helper(newScores, newPos, if (turn == 1) 0 else 1)
                        winFromHere = winFromHere.sum(subWin)
                    }
                }
            }
            cache[key] = winFromHere
            return winFromHere
        }

        println(helper(Pair(0, 0), Pair(initialPos[0], initialPos[1]), 0).toList().maxOf { it })
    }
}