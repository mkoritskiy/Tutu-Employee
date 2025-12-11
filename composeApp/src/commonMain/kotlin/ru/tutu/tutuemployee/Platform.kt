package ru.tutu.tutuemployee

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform