package me.robert.aoc

import java.io.File
import kotlin.math.abs

fun main() {
    // Load pairs from file
    val pairs = File("input.txt").readLines().associate ({
        val pair = it.split("   ")
        return@associate pair[0].toInt() to pair[1].toInt()
    }).toMap()
    
    // Split map and order both from small to large
    val keys = pairs.keys.sorted().toList()
    val values = pairs.values.sorted().toList()
    
    println("Sum of all distances: ${partOne(keys, values)}")
    println("Similarity of values: ${partTwo(keys, values)}")
}

fun partOne(keys: List<Int>, values: List<Int>): Int {
    var sum = 0
    
    // Calculate the sum distance between each key and value
    for (i in 0 until keys.size)
        sum += abs(keys[i] - values[i]).toInt()
    return sum
}

fun partTwo(keys: List<Int>, values: List<Int>): Int {
    var similarity = 0
    
    // Calculate the similarity of the lists based on the amount of times the key appears in the values list
    for (key in keys)
        similarity += key * values.count({it == key})
    return similarity
}