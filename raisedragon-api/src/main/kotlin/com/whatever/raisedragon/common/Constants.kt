package com.whatever.raisedragon.common

object Constants {

    val profile: String
        get() = System.getProperty("spring.profiles.active")
}