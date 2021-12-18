import days.*

var days = listOf(
    Day01(),
    Day02(),
    Day03(),
    Day04(),
    Day05(),
    Day06(),
    Day07(),
    Day08(),
    Day09(),
    Day10(),
    Day11(),
    Day12(),
    Day13(),
    Day14(),
    Day15(),
    Day16(),
    Day17(),
)

fun solveAll() {
    days.forEachIndexed { idx, it ->
        println(" - - Solving Day${idx + 1} - -")
        it.solveP1()
        it.solveP2()
    }
}

fun solveLast() {
    days.last().solveP1()
    days.last().solveP2()
}

fun solveDay(day: Int) {
    days.get(day - 1).solveP1()
    days.get(day - 1).solveP2()
}

fun main() {
    solveAll()
}