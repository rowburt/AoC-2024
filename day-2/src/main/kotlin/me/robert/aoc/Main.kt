package me.robert.aoc

import java.io.File
import kotlin.math.abs

import me.robert.aoc.rules.*

typealias Report = List<Int>

fun main() {
    // Load reports from file
    val reports =
            File("input.txt").readLines().map {
                return@map it.split(" ").map(String::toInt)
            }

    println("Amount of safe reports: ${partOne(reports)}")
}

fun partOne(reports: List<Report>): Int {
    val rules = listOf(IncreasingRule(), StepRule())
    
    return reports.count {
        for (rule in rules)
            if(!rule.invoke(it)) return@count false
        println(it)
        return@count true
    }
}
