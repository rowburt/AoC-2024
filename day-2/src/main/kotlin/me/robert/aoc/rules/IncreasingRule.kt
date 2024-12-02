package me.robert.aoc.rules

import me.robert.aoc.Report

class IncreasingRule : Rule {
    
    override fun invoke(report: Report): Boolean {
        val increasing = report.first() < report.get(1)
        var previous = report.first()
        
        for (i in 1 until report.size) {
            val current = report[i]
            
            if (current == previous) return false
            if (increasing && current < previous) return false
            if (!increasing && current > previous) return false
            
            previous = current
        }
        
        return true
    }
}