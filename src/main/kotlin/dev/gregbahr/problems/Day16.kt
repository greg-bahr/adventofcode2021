package dev.gregbahr.problems

import kotlin.properties.Delegates

class Day16(data: String) {

    private val input = data.chunked(1).flatMap { String.format("%4s", Integer.toBinaryString(it.toInt(16))).replace(" ", "0").chunked(1).map(String::toInt) }

    open class Packet {
        var version by Delegates.notNull<Int>()
        var length by Delegates.notNull<Int>()
        var value by Delegates.notNull<Long>()

        companion object {
            fun parse(bits: List<Int>): Packet {
                val version = bits.take(3).joinToString("").toInt(2)

                return when (val typeId = bits.slice(3 until 6).joinToString("").toInt(2)) {
                    4 -> LiteralPacket.parse(version, bits.slice(6 until bits.size))
                    else -> OperatorPacket.parse(version, typeId, bits.slice(6 until bits.size))
                }
            }
        }
    }

    class LiteralPacket : Packet() {
        companion object {
            fun parse(version: Int, bits: List<Int>): LiteralPacket {
                return LiteralPacket().apply {
                    this.version = version
                    val chunks = mutableListOf<String>()

                    var i = 0
                    while (true) {
                        val chunk = bits.slice((i*5)..((i*5)+4))
                        chunks += chunk.takeLast(4).joinToString("")

                        if (chunk[0] == 0) {
                            break
                        }
                        i++
                    }
                    value = chunks.joinToString("").toLong(2)
                    length = 11 + i * 5
                }
            }
        }

        override fun toString(): String {
            return "LiteralPacket(version = $version, value = $value, length = $length)"
        }
    }

    class OperatorPacket : Packet() {
        var subPackets by Delegates.notNull<List<Packet>>()

        companion object {
            private const val LENGTH_L_0 = 15
            private const val LENGTH_L_1 = 11

            fun parse(version: Int, typeId: Int, bits: List<Int>): OperatorPacket {
                return OperatorPacket().apply {
                    this.version = version
                    val lengthBits = if (bits[0] == 0) bits.slice(1..LENGTH_L_0) else bits.slice(1..LENGTH_L_1)
                    val length = lengthBits.joinToString("").toInt(2)

                    this.subPackets = when (bits[0]) {
                        0 -> parseTypeZero(length, bits.drop(1 + LENGTH_L_0))
                        1 -> parseTypeOne(length, bits.drop(1 + LENGTH_L_1))
                        else -> throw IllegalArgumentException()
                    }
                    this.length = 7 + lengthBits.size + subPackets.sumOf { it.length }
                    this.value = when (typeId) {
                        0 -> subPackets.sumOf { it.value }
                        1 -> subPackets.fold(1) { acc, packet -> acc * packet.value }
                        2 -> subPackets.minOf { it.value }
                        3 -> subPackets.maxOf { it.value }
                        5 -> if (subPackets[0].value > subPackets[1].value) 1 else 0
                        6 -> if (subPackets[0].value < subPackets[1].value) 1 else 0
                        7 -> if (subPackets[0].value == subPackets[1].value) 1 else 0
                        else -> throw IllegalArgumentException("Unable to parse operator packet, invalid type ID.")
                    }
                }
            }

            private fun parseTypeZero(length: Int, bits: List<Int>): List<Packet> {
                val packets = mutableListOf<Packet>()
                val packetData = bits.take(length)

                var i = 0
                while (i < length) {
                    val packet = parse(packetData.drop(i))

                    packets += packet
                    i += packet.length
                }

                return packets
            }

            private fun parseTypeOne(packetCount: Int, bits: List<Int>): List<Packet> {
                val packets = mutableListOf<Packet>()

                var i = 0
                while (packets.size < packetCount) {
                    val packet = parse(bits.drop(i))

                    packets += packet
                    i += packet.length
                }

                return packets
            }
        }

        override fun toString(): String {
            return "OperatorPacket(version = $version, value = $value, length = $length, subPackets=$subPackets)"
        }
    }

    private fun versionSum(packet: Packet): Int {
        return if (packet is OperatorPacket) packet.version + packet.subPackets.sumOf { versionSum(it) } else packet.version
    }

    fun part1(): Int {
        return versionSum(Packet.parse(input))
    }

    fun part2(): Long {
        return Packet.parse(input).value
    }
}
