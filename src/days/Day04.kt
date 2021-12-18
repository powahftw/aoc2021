package days

import Day
import readlinesFromDay

class Day04() : Day() {

    var p1 = 0
    var p2 = 0

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

    class ParseLine(line: String) {
        val command = line.split(" ").first()
        val amount = line.split(" ").last().toInt()
        operator fun component1(): String = command
        operator fun component2(): Int = amount
    }

    fun extractAndFindWinnerSum(
        extractedNumbers: List<Int>,
        bingoCards: MutableMap<Int, MutableList<MutableList<Int>>>
    ) {
        val winners: MutableSet<Int> = mutableSetOf<Int>()
        for (extractedNumber in extractedNumbers) {
            for ((key, bingoCard) in bingoCards) {
                if (winners.contains(key)) {
                    continue
                }
                bingoCard.tickNumber(extractedNumber)
                if (bingoCard.isWinner()) {
                    winners.add(key)
                    if (winners.size == 1) {
                        p1 = bingoCard.sumM() * extractedNumber
                    } else if (winners.size == bingoCards.size) {
                        p2 = bingoCard.sumM() * extractedNumber
                    }
                    if (winners.size == bingoCards.size) {
                        return
                    }
                }
            }
        }
    }

    val input = readlinesFromDay(4)

    init {
        val extractedNumbers = input.first().split(",").map { it -> it.toInt() }
        val bingoCards: MutableMap<Int, MutableList<MutableList<Int>>> = mutableMapOf()
        var currentBingoCardN = 0
        var currentCard: MutableList<MutableList<Int>> = mutableListOf<MutableList<Int>>()
        for (line in input.slice(3 until input.size)) {
            if (line.isEmpty()) {
                currentBingoCardN++
                bingoCards[currentBingoCardN] = currentCard
                currentCard = mutableListOf()
            } else {
                val foundNumbers = line.trim().split(" +".toRegex()).map { it -> it.toInt() }.toMutableList()
                currentCard.add(foundNumbers)
            }
        }
        currentBingoCardN++
        bingoCards[currentBingoCardN] = currentCard
        extractAndFindWinnerSum(extractedNumbers, bingoCards)
    }

    override fun solveP1() {
        println(p1)
    }

    override fun solveP2() {
        println(p2)
    }
}