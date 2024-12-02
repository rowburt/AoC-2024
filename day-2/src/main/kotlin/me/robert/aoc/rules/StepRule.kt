package me.robert.aoc.rules

import kotlin.math.abs
import me.robert.aoc.Report

class StepRule : Rule {

    override fun invoke(report: Report): Boolean {
        var previous = report.first()

        for (i in 1 until report.size) {
            var diff = report.get(i) - previous
            if (abs(diff) > 3) return false
            previous = report.get(i)
        }

        return true
    }
}
