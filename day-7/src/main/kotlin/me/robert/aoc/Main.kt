package me.robert.aoc

import java.io.File

typealias Inputs = List<Long>

typealias Solution = List<Operator>

data class Test(val expected: Long, val inputs: Inputs)

fun Solution.compute(inputs: Inputs): Long? {
    if (this.size != inputs.size - 1) return null
    var sum = inputs[0]

    for (i in 1 until inputs.size) {
        val operator = this[i - 1]
        sum = operator.func.invoke(sum, inputs[i])
    }

    return sum
}

enum class Operator(val func: (Long, Long) -> Long) {
    Add({ a, b -> a + b }),
    Multiply({ a, b -> a * b }),
    Concatenate({ a, b -> "$a$b".toLong() })
}

fun main() {
    val tests = File("input.txt").readLines().map {
        val split = it.split(": ")
        val inputs = split.last().split(" ").map(String::toLong)
        return@map Test(split.first().toLong(), inputs)
    }

    println("Sum of all valid tests ${partOne(tests)}")
    println("Sum of all valid tests with added concatenate operator ${partTwo(tests)}")
}

fun solve(test: Test, allowed: List<Operator>, solution: Solution): Solution? {
    if (solution.size == test.inputs.size - 1) {
        return if (solution.compute(test.inputs) == test.expected) solution else null
    }

    for (op in allowed) {
        val new = listOf(*solution.toTypedArray(), op)
        return solve(test, allowed, new) ?: continue
    }

    return null
}

fun partOne(tests: List<Test>): Long {
    val allowed = listOf(Operator.Add, Operator.Multiply)
    return tests.filter { solve(it, allowed, listOf()) != null }.sumOf(Test::expected)
}

fun partTwo(tests: List<Test>): Long {
    val allowed = listOf(Operator.Add, Operator.Multiply, Operator.Concatenate)
    return tests.filter { solve(it, allowed, listOf()) != null }.sumOf(Test::expected)
}