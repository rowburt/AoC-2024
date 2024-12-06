application.mainClass.set("me.robert.aoc.MainKt")

plugins {
    kotlin("jvm").version("2.0.0")
    application
}

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain(21)
}