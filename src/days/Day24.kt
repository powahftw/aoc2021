package days

import Day

class Day24 : Day() {

    // Rules:
    var values = listOf(10, 5, 12, 12, 6, 4, 15, 3, 7, 11, 2, 12, 4, 11)
    var sgn = listOf(10, 13, 15, -12, 14, -2, 13, -12, 15, 11, -3, -13, -12, -13)

    fun printRules() {
        var stack = mutableListOf<String>()
        values.zip(sgn).forEachIndexed { idx, (v, s) ->
            if (s > 0) {
                stack.add("in[${idx}] + $v")
            } else {
                var el = stack.removeLast()
                println("in[${idx}] == $el $s")
            }
        }
    }
//    in[3] == in[2] + 12 - 12
//    in[5] == in[4] + 6 - 2
//    in[7] == in[6] + 15 - 12
//    in[10] == in[9] + 11 - 3
//    in[11] == in[8] + 7 - 13
//    in[12] == in[1] + 5 - 12
//    in[13] == in[0] + 10 - 13
//    ==
//    in[3] == in[2]
//    in[5] == in[4] + 4
//    in[7] == in[6] + 3
//    in[10] == in[9] + 8
//    in[11] == in[8] - 6
//    in[12] == in[1] - 7
//    in[13] == in[0] - 3

    override fun solveP1() {
        //printRules()
        // Maximize
        // in[0] = 9 -> in[13] = 6
        // in[1] = 9 -> in[12] = 2
        // in[2] = 9 -> in[3] = 9
        // in[4] = 5 -> in[5] = 9
        // in[6] = 6 -> in[7] = 9
        // in[8] = 9 -> in[11] = 3
        // in[9] = 1 -> in[10] = 9
        println(99995969919326)
    }

    override fun solveP2() {
        // printRules()
        // Minimize
        // in[0] = 4 -> in[13] = 1
        // in[1] = 8 -> in[12] = 1
        // in[2] = 1 -> in[3] = 1
        // in[4] = 1 -> in[5] = 5
        // in[6] = 1 -> in[7] = 4
        // in[8] = 7 -> in[11] = 1
        // in[9] = 1 -> in[10] = 9
        println(48111514719111)
    }
}