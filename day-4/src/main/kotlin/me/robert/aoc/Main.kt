package me.robert.aoc

import java.io.File
import kotlin.sequences.toList
import kotlin.math.floor

const val PART_ONE_QUERY = "XMAS"
const val PART_TWO_QUERY = "MAS"

fun Pair<Int, Int>.add(other: Pair<Int, Int>) =
        Pair(this.first + other.first, this.second + other.second)

enum class Direction(val offset: Pair<Int, Int>) {
    UpLeft(Pair(-1, -1)),
    Up(Pair(0, -1)),
    UpRight(Pair(1, -1)),
    Left(Pair(-1, 0)),
    Right(Pair(1, 0)),
    DownLeft(Pair(-1, 1)),
    Down(Pair(0, 1)),
    DownRight(Pair(1, 1));

    companion object {
        val DIAGONALS = listOf(UpLeft, UpRight, DownLeft, DownRight)
    }

    fun opposite() = when(this) {
        UpLeft -> DownRight
        Up -> Down
        UpRight -> DownLeft
        Left -> Right
        Right -> Left
        DownLeft -> UpRight
        Down -> Up
        DownRight -> UpLeft
    }
}

data class Tile private constructor(val character: Char, val position: Pair<Int, Int>) {
    val bordering = arrayOfNulls<Tile?>(Direction.values().size)

    companion object {

        fun buildMap(scene: List<List<Char>>): List<Tile> {
            val tiles = mutableListOf<Tile>()

            // Initialize all tiles
            for (i in 0 until scene.size) {
                val row = scene[i]

                for (j in 0 until row.size)
                    tiles += Tile(scene[i][j], Pair(j, i))
            }

            // Add neighboring tile for all tiles
            for (tile in tiles) {
                for (direction in Direction.values()) {
                    val neighbor = tiles.find { it.position == tile.position.add(direction.offset) } ?: continue
                    tile.bordering[direction.ordinal] = neighbor
                }
            }

            return tiles.toList()
        }
    }

    fun visit(word: String): List<Direction> {
        // Check if this is the correct starting point of a sequence
        if (word.isBlank()) return emptyList()
        val matches = mutableListOf<Direction>()

        // Do a word search in every direction
        for (direction in Direction.values()) {
            // Add to matches if a match wa.s found in this direction
            if (!checkFollowing(word, direction)) continue
            matches += direction
        }

        return matches.toList()
    }

    fun visitX(word: String): Boolean {
        if (word.length != 3) return false

        // Check if this tile contains the middle character of the word we are searching
        if(character != word.get(floor(word.length / 2.0).toInt())) return false

        // Check diagonal of each corner bordering this tile
        val diagonals = Direction.DIAGONALS.filter {
            val tile = bordering[it.ordinal] ?: return false
            return@filter tile.checkFollowing(word, it.opposite())
        }

        return diagonals.size == 2
    }

    private fun checkFollowing(word: String, direction: Direction): Boolean {
        // Check whether we are the expected character
        if (word.firstOrNull() != character) return false

        // Check if we reached the end of the word
        if (word.length <= 1) return true

        // Keep recursing in the direction we are searching
        val next = bordering[direction.ordinal] ?: return false
        return next.checkFollowing(word.substring(1), direction)
    }
}

fun main() {
    val contents = File("input.txt").readText().lines().map(CharSequence::toMutableList).toMutableList()
    val tiles = Tile.buildMap(contents)

    println("Amount of $PART_ONE_QUERY matches: ${partOne(tiles)}")
    println("Amount of $PART_TWO_QUERY crosses: ${partTwo(tiles)}")
}

fun partOne(tiles: List<Tile>) =
    tiles.sumOf { return@sumOf it.visit(PART_ONE_QUERY).size }

fun partTwo(tiles: List<Tile>) =
    tiles.count { return@count it.visitX(PART_TWO_QUERY) }