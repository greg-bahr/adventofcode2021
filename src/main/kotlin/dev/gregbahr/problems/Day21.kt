package dev.gregbahr.problems

import kotlin.math.abs

class Day21 {
    companion object {
        const val PLAYER_ONE_START = 8
        const val PLAYER_TWO_START = 6
    }

    class Player(private val location: Int, val score: Int) {
        fun move(roll: Int): Player {
            var newLocation = location + roll
            if (newLocation > 10) {
                newLocation -= 10
            }
            val newScore = score + newLocation

            return Player(newLocation, newScore)
        }
    }

    fun part1(): Int {
        val players = mutableListOf(Player(PLAYER_ONE_START, 0), Player(PLAYER_TWO_START, 0))

        var turn = 0
        while (players.all { it.score < 1000 }) {
            turn++
            val roll = (((turn * 3) - 2)..(turn * 3)).map { if (it % 100 == 0) 100 else it % 100 }.sum() % 10
            players[abs((turn % 2) - 1)] = players[abs((turn % 2) - 1)].move(roll)
        }

        return (turn*3) * players.minOf { it.score }
    }

    fun part2(): Long {
        val possibleTurns = (1..3).flatMap { x -> (1..3).flatMap { y -> (1..3).map { z -> x+y+z } } }.groupingBy { it }.eachCount()

        fun quantumRoll(player1: Player, player2: Player, turn: Int = 1): Map<Int, Long> {
            val currentPlayer = if (turn % 2 == 1) player1 else player2
            val wins = mutableMapOf(0 to 0L, 1 to 0L)

            for ((roll, universesCreated) in possibleTurns) {
                val updated = currentPlayer.move(roll)

                if (updated.score >= 21) {
                    wins[turn % 2] = wins[turn % 2]!! + universesCreated
                } else {
                    val laterWins = if (turn % 2 == 1) {
                        quantumRoll(updated, player2, turn + 1)
                    } else {
                        quantumRoll(player1, updated, turn + 1)
                    }
                    wins[0] = wins[0]!! + (laterWins[0]!! * universesCreated)
                    wins[1] = wins[1]!! + (laterWins[1]!! * universesCreated)
                }
            }

            return wins
        }

        return quantumRoll(Player(PLAYER_ONE_START, 0), Player(PLAYER_TWO_START, 0)).maxOf { it.value }
    }
}
