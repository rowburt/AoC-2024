package me.robert.aoc.rules

import me.robert.aoc.Report

interface Rule {
    
    fun invoke(report: Report): Boolean
}