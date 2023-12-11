package com.whatever.raisedragon

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RaiseDragonApiApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.additional-location", "classpath:/domain-config/,classpath:/external-config/")
    runApplication<RaiseDragonApiApplication>(*args)
}