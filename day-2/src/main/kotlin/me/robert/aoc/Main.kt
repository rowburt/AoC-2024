package me.robert.aoc

import java.io.File
import me.robert.aoc.rules.*

typealias Report = List<Int>

fun main() {
    // Load reports from file
    val reports =
            File("input.txt").readLines().map {
                return@map it.split(" ").map(String::toInt)
            }

    println("Amount of safe reports: ${partOne(reports)}")
    println("Amount of safe reports: ${partTwo(reports)}")
}

fun partOne(reports: List<Report>): Int {
    val rules = listOf(IncreasingRule(), StepRule())

    return reports.count { report ->
        return@count rules.all { it.invoke(report) }
    }
}

fun partTwo(reports: List<Report>): Int {
    val rules = listOf(IncreasingRule(), StepRule())

    return reports.count { report ->
        if (rules.all { it.invoke(report) }) return@count true

        // Bruteforce all possible options of removing one element
        for (i in 0 until report.size) {
            val copy = report.toMutableList()
            copy.removeAt(i)

            if (rules.all { it.invoke(copy) }) return@count true
        }

        return@count false
    }
}
