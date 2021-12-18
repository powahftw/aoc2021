package days


import Day
import readlinesFromDay

class Day16 : Day() {

    class Packet(bin: String) {

        val v = bin.substring(0..2).toInt(2)
        val type = bin.substring(3..5).toInt(2)
        val innerPackets = mutableListOf<Packet>()
        var totSize = 6
        var value = 0L

        init {
            if (type == 4) {
                var binValue = ""
                for ((idx, chunk) in bin.drop(6).chunked(5).withIndex()) {
                    binValue += chunk.drop(1)
                    if (chunk[0] == '0') {
                        totSize += 5 * (idx + 1)
                        break
                    }
                }
                value = binValue.toLong(2)
            } else {
                val lengthBit = bin[6]
                var readBits = 0
                if (lengthBit == '0') {
                    val lengthOfSubPackages = bin.substring(7..21).toInt(2)
                    while (readBits < lengthOfSubPackages) {
                        readBits += parseSubPacket(bin.substring(22 + readBits))
                    }
                    totSize = readBits + 22
                } else {
                    val numOfSubpackages = bin.substring(7..17).toInt(2)
                    repeat(numOfSubpackages) { readBits += parseSubPacket(bin.substring(18 + readBits)) }
                    totSize = readBits + 18
                }
            }
        }

        fun parseSubPacket(bin: String): Int {
            val innerPacket = Packet(bin)
            innerPackets.add(innerPacket)
            return innerPacket.totSize
        }

        fun sumVersion(): Int {
            return v + innerPackets.sumOf { it.sumVersion() }
        }

        fun process(): Long {
            val innerValues = innerPackets.map { it.process() }
            return when (type) {
                0 -> innerValues.sum()
                1 -> innerValues.fold(1L) { prev, it -> prev * it }
                2 -> innerValues.minOf { it }
                3 -> innerValues.maxOf { it }
                4 -> value
                5 -> if (innerValues.first() > innerValues.last()) 1L else 0L
                6 -> if (innerValues.first() < innerValues.last()) 1L else 0L
                7 -> if (innerValues.first() == innerValues.last()) 1L else 0L
                else -> 0L
            }
        }
    }

    fun String.convertHexStringToBitString(): String {
        return this.toList().map { it.toString() }.map { it.toInt(16) }
            .joinToString("") {
                String.format("%4s", Integer.toBinaryString(it)).replace(' ', '0')
            }
    }

    val hexString = readlinesFromDay(16).first().convertHexStringToBitString()
    val nestedPacket = Packet(hexString)

    override fun solveP1() {
        println(nestedPacket.sumVersion())
    }

    override fun solveP2() {
        println(nestedPacket.process())

    }
}