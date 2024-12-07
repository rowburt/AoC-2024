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

    println("Sum of middle value of valid updates: ${partOne(ordering, updates)}")
    println("Sum of middle value of fixed updates: ${partTwo(ordering, updates)}")
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

fun orderUpdate(ordering: List<Order>, update: Update): Update {
    val output = update.toMutableList()

    restart@while (true) {
        for (i in 0 until output.size) {
            val current = output[i]
            for (j in i until output.size) {
                if (i == j) continue
                val compare = output[j]
                val tempUpdate = listOf(current, compare)

                val relevant = getRelevantOrders(ordering, tempUpdate)
                if (relevant.isEmpty() || isInOrder(ordering, tempUpdate)) continue

                output.removeAt(j)
                output.add(i, compare)
                continue@restart
            }
        }
        break
    }

    return output.toList()
}

fun partOne(ordering: List<Order>, updates: List<Update>) =
        updates.sumOf {
            val relevant = getRelevantOrders(ordering, it)

            if (!isInOrder(relevant, it)) return@sumOf 0
            return@sumOf it.get(floor(it.size / 2.0).toInt())
        }

fun partTwo(ordering: List<Order>, updates: List<Update>): Int {
    val fixed = mutableListOf<Update>()
    
    for (update in updates) {
        // Skip already ordered updates
        val relevant = getRelevantOrders(ordering, update)
        if (isInOrder(relevant, update)) continue

        val ordered = orderUpdate(relevant, update)
        fixed += ordered
    }
    
    return partOne(ordering, fixed.toList())
}
