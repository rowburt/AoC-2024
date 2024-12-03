package me.robert.aoc

import java.io.File
import kotlin.sequences.toList

const val MULTIPLY_REGEX = """mul\((\d+),(\d+)\)"""
const val DO_DONT_REGEX = """don't\(\)[\s\S]+?(?:do\(\)|$)"""

fun main() {
    val contents = File("input.txt").readText()

    println("Total value of sums: ${partOne(contents)}")
    println("Total value of do dont sums: ${partTwo(contents)}")
}

fun partOne(contents: String): Int {
    val regex = Regex(MULTIPLY_REGEX)
    val matches = regex.findAll(contents).toList()

    return matches.sumOf {
        val (_, first, second) = it.groupValues
        return@sumOf first.toInt() * second.toInt()
    }
}

fun partTwo(contents: String): Int {
    val regex = Regex(DO_DONT_REGEX)
    val filtered = contents.replace(regex, "")
    return partOne(filtered)
}
