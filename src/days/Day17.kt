package days

import Cord
import Day
import kotlin.math.max

class Day17 : Day() {

    fun Cord.isInBounds(downLeft: Cord, upRight: Cord): Boolean {
        return (downLeft.x <= this.x && this.x <= upRight.x) && (downLeft.y <= this.y && this.y <= upRight.y)
    }

    fun Cord.isOvershoot(downLeft: Cord, upRight: Cord): Boolean {
        return this.x > upRight.x || this.y < downLeft.y
    }

    var downLeft = Cord(248, -85)
    var upRight = Cord(285, -56)

    var maxHOverall = 0
    var vCandidates = mutableSetOf<Pair<Int, Int>>()

    init {
        for (vx in 0..upRight.x) {
            for (vy in downLeft.y..300) {
                var currPos = Cord(0, 0)
                var (currVx, currVy) = listOf(vx, vy)
                var maxH = 0
                while (!currPos.isOvershoot(downLeft, upRight)) {
                    currPos = Cord(currPos.x + currVx, currPos.y + currVy)
                    maxH = max(maxH, currPos.y)
                    currVx = max(0, currVx - 1)
                    currVy -= 1
                    if (currPos.isInBounds(downLeft, upRight)) {
                        maxHOverall = max(maxHOverall, maxH)
                        vCandidates.add(Pair(vx, vy))
                    }
                }
            }
        }
    }

    override fun solveP1() {
        println(maxHOverall)
    }

    override fun solveP2() {
        println(vCandidates.size)
    }
}