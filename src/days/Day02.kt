package days

import Day
import readlinesFromDay

class Day02 : Day() {

    class ParseLine(line: String) {
        val command = line.split(" ").first()
        val amount = line.split(" ").last().toInt()
        operator fun component1(): String = command
        operator fun component2(): Int = amount
    }

    val inp: List<String> = readlinesFromDay(2)

    override fun solveP1() {
        var pos: Pair<Int, Int> = Pair(0, 0)
        for (line: String in inp) {
            val (command, amount) = ParseLine(line)
            when (command) {
                "forward" -> pos = pos.copy(first = pos.first + amount)
                "down" -> pos = pos.copy(second = pos.second + amount)
                "up" -> pos = pos.copy(second = pos.second - amount)
            }
        }
        println(pos.first * pos.second)
    }

    override fun solveP2() {
        var pos = Pair(0, 0)
        var aim: Int = 0

        for (line: String in inp) {
            val (command, amount) = ParseLine(line)
            when (command) {
                "forward" -> pos = Pair(pos.first + amount, pos.second + (aim * amount))
                "down" -> aim += amount
                "up" -> aim -= amount
            }
        }

        println(pos.first * pos.second)
    }
}