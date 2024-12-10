package me.robert.aoc

import java.io.File

typealias Line = CharArray

typealias Map = List<Line>

const val EMPTY = '.'
const val VISITED = 'X'
const val BLOCKADE = '#'

data class Pos(val x: Int, val y: Int) {

    fun add(other: Pos) = Pos(x + other.x, y + other.y)

    fun isInBounds(map: Map) = x >= 0 && y >= 0 && y < map.size && x < map[y].size
}

fun Map.get(pos: Pos) = this[pos.y][pos.x]

fun MutableList<Line>.set(pos: Pos, value: Char) {
    this[pos.y][pos.x] = value
}

enum class Guard(val character: Char, val offset: Pos) {
    UP('^', Pos(0, -1)),
    RIGHT('>', Pos(1, 0)),
    DOWN('v', Pos(0, 1)),
    LEFT('<', Pos(-1, 0));

    companion object {

        fun isGuard(char: Char) = Guard.values().any { it.character == char }
    }

    fun next() = Guard.values()[(ordinal + 1) % Guard.values().size]
}

fun main() {
    val map = File("input.txt").readLines().map(String::toCharArray)
    
    println("Amount of visited tiles: ${partOne(map)}")
}

fun findGuard(map: Map): Pos? {
    for (i in 0 until map.size) {
        val current = map[i]
        val index = current.indexOfFirst(Guard::isGuard)

        if (index == -1) continue
        return Pos(index, i)
    }

    return null
}

fun tick(guardPos: Pos, map: Map): Map {
    val character = map.get(guardPos)
    val guard = Guard.values().find { return@find it.character == character } ?: throw Exception("No guard char found")

    val mutable = map.toMutableList()
    val next = guardPos.add(guard.offset)
    mutable.set(guardPos, VISITED)
    if(!next.isInBounds(mutable)) return mutable.toList()

    if (mutable.get(next) == BLOCKADE) {
        mutable.set(guardPos, guard.next().character)
        return mutable.toList()
    }

    mutable.set(next, character)
    return mutable.toList()
}

fun partOne(map: Map): Int {
    var updatingMap = map
    
    while (true) {
        val guard = findGuard(updatingMap) ?: break
        updatingMap = tick(guard, updatingMap)
    }
    
    return updatingMap.sumOf { return@sumOf it.filter{ return@filter it == VISITED }.size }
}
