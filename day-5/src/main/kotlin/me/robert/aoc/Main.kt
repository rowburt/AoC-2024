package me.robert.aoc

import java.io.File
import kotlin.math.floor

typealias Update = List<Int>

typealias Order = Pair<Int, Int>

fun main() {
    val contents = File("input.txt").readText().lines()
    val ordering =
            contents.takeWhile(String::isNotBlank).map {
                val split = it.split("|")
                return@map Pair(split.first().toInt(), split.last().toInt())
            }
    val updates =
            contents.asReversed().takeWhile(String::isNotBlank).asReversed().map {
                return@map it.split(",").map(String::toInt)
            }
    println("Page ordering rules $ordering")
    println("Numbers in update $updates")

    println("Sum of middle value of valid updates: ${partOne(ordering, updates)}")
}

fun isInOrder(ordering: List<Order>, update: Update): Boolean {
    for (i in 0 until update.size) {
        val current = update[i]

        for (j in 0 until update.size) {
            if (i == j) continue
            val test = update[j]

            val relevant = getRelevantOrders(ordering, listOf(current, test))
            if (relevant.isEmpty()) continue
            assert(relevant.size == 1) {
                println("Too many orders given for $current and $test: $relevant")
            }

            val expectFirst = relevant.first().first
            val ordered = current == expectFirst && i < j || test == expectFirst && j < i
            if (!ordered) return false
        }
    }

    return true
}

fun getRelevantOrders(ordering: List<Order>, update: Update) =
        ordering.filter {
            return@filter update.contains(it.first) && update.contains(it.second)
        }

fun partOne(ordering: List<Order>, updates: List<Update>) =
        updates.sumOf {
            val relevant = getRelevantOrders(ordering, it)
            
            if (!isInOrder(relevant, it)) return@sumOf 0
            return@sumOf it.get(floor(it.size / 2.0).toInt())
        }
