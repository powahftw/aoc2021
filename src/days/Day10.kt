package days

import Day
import readlinesFromDay
import java.math.BigInteger
import java.util.*

class Day10 : Day() {

    val errorScore = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)
    val incompleteScore = mapOf(')' to 1, ']' to 2, '}' to 3, '>' to 4)
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

    val inp: List<String> = readlinesFromDay(10)

    override fun solveP1() {
        val totSyntaxErrorScore =
            inp.filter { line -> !line.isLineIncomplete() }.sumOf { line -> line.syntaxErrorScore() }
        println(totSyntaxErrorScore)
    }

    override fun solveP2() {
        val incompleteLinesScores =
            inp.filter { line -> line.isLineIncomplete() }.map { line -> line.incompleteScore() }
                .sortedBy { it }
        println(incompleteLinesScores[incompleteLinesScores.size / 2])

    }
}