package com.whatever.raisedragon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class ShortsApiApplication

fun main(args: Array<String>) {
    runApplication<ShortsApiApplication>(*args)
}
