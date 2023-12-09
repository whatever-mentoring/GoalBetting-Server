package com.whatever.raisedragon.common.config

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
class SecurityConfig {

    companion object {
        const val DEFAULT_ROLE_NAME = "USER"
    }

}